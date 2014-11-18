(ns cljs-cheatsheet.dom
  (:require
    goog.dom
    [cljs-cheatsheet.util :as util]))

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