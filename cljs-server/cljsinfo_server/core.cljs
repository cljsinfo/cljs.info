(ns cljsinfo-server.core
  (:require
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.html :as html]
    [cljsinfo-server.util :refer [js-log log ts-log]]))

(enable-console-print!)

;;------------------------------------------------------------------------------
;; Node libraries
;;------------------------------------------------------------------------------

(def js-compression (js/require "compression"))
(def js-express     (js/require "express"))
(def js-http        (js/require "http"))

;;------------------------------------------------------------------------------
;; Main
;;------------------------------------------------------------------------------

(defn -main [& args]
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

      (.get "/old-homepage" #(.send %2 (html/old-homepage)))

      ;; TODO: docs
      ;; (.get "/docs/foo" doc-page)

      ;; serve static files out of /public
      (.use (.static js-express (str js/__dirname "/public"))))

    ;; start server
    (if (:host config)
      (.listen server (:port config) (:host config))
      (.listen server (:port config)))

    (ts-log "cljs.info server listening on port " (:port config))))

(set! *main-cli-fn* -main)
