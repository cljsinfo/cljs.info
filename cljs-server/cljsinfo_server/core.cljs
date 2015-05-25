(ns cljsinfo-server.core
  (:require
    [clojure.string :refer [replace]]
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.html :as html]
    [cljsinfo-server.latest-release :refer [fetch-latest-release!]]
    [cljsinfo-server.util :refer [hard-quit! js-log log ts-log]]
    [cljs.reader :refer [read-string]]))

(enable-console-print!)

;;------------------------------------------------------------------------------
;; Node libraries
;;------------------------------------------------------------------------------

(def fs             (js/require "fs-extra"))
(def js-compression (js/require "compression"))
(def js-express     (js/require "express"))
(def js-http        (js/require "http"))
(def js-request     (js/require "request"))

;;------------------------------------------------------------------------------
;; Docs
;;------------------------------------------------------------------------------

(def latest-docs-url
  "https://github.com/cljsinfo/api-docs/releases/download/docs-release/cljsdocs-full.edn")

(def docs (atom {}))

(defn- fetch-and-update-docs!
  "Fetches the latest docs from GitHub and updates the docs atom with them."
  []
  (ts-log "Fetching latest docs from GitHub...")
  (js-request latest-docs-url (fn [err js-resp body-txt]
    (if (or err (not= 200 (aget js-resp "statusCode")))
      (ts-log "Failed to fetch latest API docs from GitHub. Are you connected to the internet?")
      (do (.writeFile fs "docs.edn" body-txt (js-obj "encoding" "utf-8"))
          (reset! docs (read-string body-txt))
          (ts-log "Latest docs fetched, written to docs.edn, and docs atom updated."))))))

;; try to load the docs from docs.edn, else fetch the most recent ones from GitHub
(if-let [docs-string (try (.readFileSync fs "docs.edn" (js-obj "encoding" "utf-8"))
                          (catch js/Error err false))]
  (do (reset! docs (read-string docs-string))
      (ts-log "Loaded docs from docs.edn"))
  (fetch-and-update-docs!))

;; fetch fresh docs from GitHub on load (config option)
(when (true? (:fetch-docs-on-load? config))
  (fetch-and-update-docs!))

;; begin polling for doc updates (config option)
(when (number? (:fetch-docs-every-n-seconds config))
  (js/setInterval fetch-and-update-docs! (* 1000 (:fetch-docs-every-n-seconds config))))

;;------------------------------------------------------------------------------
;; Pages
;;------------------------------------------------------------------------------

;; TODO: this belongs in some sort of shared util namespace
(defn- decode-symbol-url [s]
  (-> s
      (replace "DOT"   ".")
      (replace "GT"    ">")
      (replace "LT"    "<")
      (replace "BANG"  "!" )
      (replace "QMARK" "?")
      (replace "SLASH" "/")
      (replace "STAR"  "*")
      (replace "PLUS"  "+")
      (replace "EQ"    "=")))

(defn- doc-page [js-req js-res]
  (let [ns-string (aget js-req "params" "namespace")
        symbol-string (decode-symbol-url (aget js-req "params" "symbol"))
        full-name (str ns-string "/" symbol-string)
        the-doc (get @docs full-name)]
    (if the-doc
      (.send js-res (html/doc-page the-doc))
      (.send js-res (html/not-found)))))

;;------------------------------------------------------------------------------
;; API
;;------------------------------------------------------------------------------

;; this endpoint is mainly for CI integration so we can push to the server to
;; update docs instead of having to poll GitHub
(defn- refresh-docs-api [js-req js-res]
  (let [key-from-config (:refresh-docs-key config)
        key-from-client (aget js-req "query" "key")]
    (if (and key-from-config
             (= key-from-client key-from-config))
      (do (fetch-and-update-docs!)
          (.send js-res "refreshing the docs..."))
      (.send js-res "wrong key"))))

;;------------------------------------------------------------------------------
;; Server Errors
;;------------------------------------------------------------------------------

(defn- port-already-in-use []
  (ts-log (str
    "Failed to start the server because port " (:port config) " is already in use. "
    "Are you already running an instance of the server?")))

(defn- unknown-failure [js-err]
  (ts-log "The server has died and I'm not sure why. Here's the error message that caused it:")
  (js-log js-err)
  (js-log "Hopefully you can figure it out. Goodbye and good luck!"))

(defn- on-server-error [js-err]
  (let [code (aget js-err "code")]
    (case code
      "EADDRINUSE" (port-already-in-use)
      (unknown-failure js-err))))

;;------------------------------------------------------------------------------
;; Main
;;------------------------------------------------------------------------------

(defn -main []
  (let [js-app (js-express)
        js-server (.createServer js-http js-app)]
    ;; configure express app
    (doto js-app
      ;; gzip everything
      (.use (js-compression))

      ;; static pages
      (.get "/"          #(.send %2 (html/homepage)))
      (.get "/community" #(.send %2 (html/community-page)))
      (.get "/docs"      #(.send %2 (html/docs-index)))
      (.get "/faq"       #(.send %2 (html/faq-page)))
      (.get "/getting-started" #(.send %2 (html/getting-started)))
      (.get "/rationale" #(.send %2 (html/rationale)))
      (.get "/tutorials" #(.send %2 (html/tutorials-index)))

      ;; docs pages
      (.get "/docs/:namespace/:symbol" doc-page)

      ;; api
      (.get "/api/refresh-docs" refresh-docs-api)

      ;; serve static files out of /public
      (.use (.static js-express (str js/__dirname "/public"))))

    ;; catch errors
    (.on js-server "error" on-server-error)

    ;; start server
    (if (:host config)
      (.listen js-server (:port config) (:host config))
      (.listen js-server (:port config)))

    (ts-log "cljs.info server listening on port " (:port config)))

  ;; fetch latest CLJS version at startup
  (fetch-latest-release!))

(set! *main-cli-fn* -main)
