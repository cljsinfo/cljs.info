(ns cljsinfo-server.core
  (:require
    [cljs.reader :refer [read-string]]
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.docs :refer [docs fetch-and-update-docs!]]
    [cljsinfo-server.html :as html]
    [cljsinfo-server.util :refer [hard-quit! js-log log ts-log]]
    [clojure.string :refer [replace]]))

(enable-console-print!)

;;------------------------------------------------------------------------------
;; Node libraries
;;------------------------------------------------------------------------------

(def js-compression (js/require "compression"))
(def js-express     (js/require "express"))
(def js-http        (js/require "http"))

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

    (ts-log "cljs.info server listening on port " (:port config))))

(set! *main-cli-fn* -main)
