(ns cljsinfo-server.core
  (:require
    [clojure.string :refer [replace]]
    [clojure.walk :refer [keywordize-keys]]
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.html :as html]
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

(def latest-docs-url "https://github.com/cljsinfo/api-docs/releases/download/docs-release/cljsdocs-full.edn")

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
;; Main
;;------------------------------------------------------------------------------

(defn -main []
  (let [app (js-express)
        server (.createServer js-http app)]
    ;; configure express app
    (doto app
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

      ;; serve static files out of /public
      (.use (.static js-express (str js/__dirname "/public"))))

    ;; start server
    (if (:host config)
      (.listen server (:port config) (:host config))
      (.listen server (:port config)))

    (ts-log "cljs.info server listening on port " (:port config))))

(set! *main-cli-fn* -main)
