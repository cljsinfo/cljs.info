(ns cljsinfo-server.util)

(def js-moment (js/require "moment"))

;;------------------------------------------------------------------------------
;; Util Functions
;;------------------------------------------------------------------------------

(defn log
  "Log a Clojure thing."
  [thing]
  (.log js/console (pr-str thing)))

(defn js-log
  "Log a JavaScript thing."
  [& js-things]
  (apply (.-log js/console) js-things))

(defn- now []
  (.format (js-moment) "YYYY-MM-DD HH:mm:ss"))

;; TODO: investigate using a proper logging library
;; - https://github.com/flatiron/winston
;; - https://github.com/trentm/node-bunyan
(defn ts-log
  "Timestampped log."
  [& msgs]
  (js-log (str "[" (now) "] " (apply str msgs))))

(defn hard-quit!
  "Goodbye for real. Do not pass Go. Do not collect $200."
  []
  (.exit js/process))
