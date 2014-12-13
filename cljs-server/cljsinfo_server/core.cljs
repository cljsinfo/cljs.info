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
;; Pages
;;------------------------------------------------------------------------------

(defn- homepage [_js-req js-res]
  (.send js-res (html/homepage)))

(defn- doc-page [_js-req js-res]
  (.send js-res (html/doc-page)))

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

      ;; pages
      (.get "/" homepage)
      (.get "/docs/foo" doc-page)

      ;; serve static files out of /public
      (.use (.static js-express (str js/__dirname "/public"))))

    ;; start server
    (if (:host config)
      (.listen server (:port config) (:host config))
      (.listen server (:port config)))

    (ts-log "cljs.info server listening on port " (:port config))))

(set! *main-cli-fn* -main)