(ns cljs-cheatsheet.dom
  (:require
    goog.dom
    [cljs-cheatsheet.util :refer [js-log log]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Some Native DOM Helper Functions
;;------------------------------------------------------------------------------

(defn by-id [id]
  (.getElementById js/document id))

(defn element? [el]
  (goog.dom/isElement el))

(defn get-value [id]
  (aget (by-id id) "value"))

(defn set-html! [id html]
  (aset (by-id id) "innerHTML" html))

(defn- set-value! [id v]
  (aset (by-id id) "value" v))

(defn show-el! [id]
  (aset (by-id id) "style" "display" ""))

(defn hide-el! [id]
  (aset (by-id id) "style" "display" "none"))

(defn toggle-display! [id]
  (let [el (by-id id)
        display (aget el "style" "display")]
    (if (= display "none")
      (show-el! id)
      (hide-el! id))))

;; NOTE: surely there must be a jQuery or Google Closure function that does
;; this already?
(defn get-element-box [el]
  (let [$el ($ el)
        o (.offset $el)
        x (aget o "left")
        y (aget o "top")
        height (.outerHeight $el)
        width (.outerWidth $el)]
    {:x1 x
     :x2 (+ x width)
     :y1 y
     :y2 (+ y height)}))
