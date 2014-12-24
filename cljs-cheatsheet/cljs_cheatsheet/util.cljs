(ns cljs-cheatsheet.util
  (:require
    [clojure.walk :refer [keywordize-keys]]
    [cognitect.transit :as transit]))

;;------------------------------------------------------------------------------
;; Util Functions
;;------------------------------------------------------------------------------

(defn log
  "Log a Clojure thing."
  [thing]
  (js/console.log (pr-str thing)))

(defn js-log
  "Log a JavaScript thing."
  [thing]
  (js/console.log thing))

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

(defn half [n]
  (/ n 2))

(defn extract-namespace [full-name]
  (let [first-slash-pos (.indexOf full-name "/")]
    (subs full-name 0 first-slash-pos)))

(defn extract-symbol [full-name]
  (let [first-slash-pos (.indexOf full-name "/")]
    (subs full-name (inc first-slash-pos))))

(defn split-full-name [r]
  (let [ns1 (extract-namespace r)
        symbol-name (extract-symbol r)]
    {:full-name r
     :namespace ns1
     :symbol symbol-name}))

(defn point-inside-box? [point box]
  (let [px (:x point)
        py (:y point)]
    (and (>= px (:x1 box))
         (<= px (:x2 box))
         (>= py (:y1 box))
         (<= py (:y2 box)))))

;;------------------------------------------------------------------------------
;; AJAX
;;------------------------------------------------------------------------------

(def transit-json-rdr (transit/reader :json))

(defn- http-success? [status]
  (and (>= status 200)
       (< status 400)))

(defn- fetch-clj-success [js-evt success-fn error-fn]
  (let [status (aget js-evt "target" "status")]
    (if-not (http-success? status)
      (error-fn)
      (let [response-text (aget js-evt "target" "responseText")]
        (if-let [clj-result (try (transit/read transit-json-rdr response-text)
                                 (catch js/Error _error nil))]
          (success-fn (keywordize-keys clj-result))
          (error-fn))))))

(defn fetch-clj
  "Makes an AJAX request to an HTTP GET endpoint expecting JSON.
   Parses JSON into CLJ using transit.cljs and keywordizes map keys.
   transit.cljs is faster than using js->clj: http://tinyurl.com/ntgxyt8"
  ([url success-fn]
    (fetch-clj url success-fn (fn [] nil)))
  ([url success-fn error-fn]
    (doto (js/XMLHttpRequest.)
      (.addEventListener "load" #(fetch-clj-success % success-fn error-fn))
      (.addEventListener "error" error-fn)
      (.addEventListener "abort" error-fn)
      (.open "get" url)
      (.send))))
