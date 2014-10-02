(ns cljsinfo-server.core
  (:require
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.html :as html]
    [cljsinfo-server.util :as util]))

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

      ;; serve static files out of /public
      (.use (.static js-express (str js/__dirname "/public"))))

    ;; start server
    (if (:host config)
      (.listen server (:port config) (:host config))
      (.listen server (:port config)))

    (util/tlog "clojurescript.info server listening on port " (:port config))))

(set! *main-cli-fn* -main)