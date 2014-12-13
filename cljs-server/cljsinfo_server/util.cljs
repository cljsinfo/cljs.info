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

(defn uuid []
  "Create a UUID."
  []
  (apply
   str
   (map
    (fn [x]
      (if (= x \0)
        (.toString (bit-or (* 16 (.random js/Math)) 0) 16)
        x))
    "00000000-0000-4000-0000-000000000000")))