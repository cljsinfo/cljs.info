(ns cljsinfo-server.core
  (:require
    [clojure.string :refer [replace]]
    [clojure.walk :refer [keywordize-keys]]
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.html :as html]
    [cljsinfo-server.util :refer [js-log log ts-log]]))

(enable-console-print!)

;;------------------------------------------------------------------------------
;; Node libraries
;;------------------------------------------------------------------------------

(def fs             (js/require "fs-extra"))
(def js-compression (js/require "compression"))
(def js-express     (js/require "express"))
(def js-http        (js/require "http"))

;;------------------------------------------------------------------------------
;; Pages
;;------------------------------------------------------------------------------

;; TODO: this needs to be improved / defensive if the files don't exist
;; just rolling with it for now
(defn- join-the-docs-for-now []
  (let [hand-docs (js->clj (js/require "./docs.json"))
        gen-docs  (js->clj (js/require "./generated-docs.json"))]
    ;; lol
    (merge-with merge gen-docs hand-docs)))

(def docs (join-the-docs-for-now))

(defn- decode-symbol-url [s]
  (-> s
      (replace "DOT"   ".")
      (replace "GT"    ">")
      (replace "LT"    "<")
      (replace "BANG"  "!" )
      (replace "QMARK" "?")
      (replace "STAR"  "*")
      (replace "PLUS"  "+")
      (replace "EQ"    "=")))

(defn- doc-page [js-req js-res]
  (let [ns-string (aget js-req "params" "namespace")
        symbol-string (decode-symbol-url (aget js-req "params" "symbol"))
        doc-key (str ns-string "/" symbol-string)
        the-doc (get docs doc-key)]
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
