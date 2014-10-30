  (ns cljsinfo-client.tooltips
  (:require
    [clojure.set :refer [difference]]
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Helper
;;------------------------------------------------------------------------------

(defn- evt->tt-num
  "Returns a tooltip-id from a JS event or nil if the tooltip was not found."
  [js-evt]
  (let [current-target (aget js-evt "currentTarget")
        tooltip-num (.attr ($ current-target) "data-tooltip-id")
        tooltip-id (str "tooltip-" tooltip-num)
        tooltip-el (by-id tooltip-id)]
    (if tooltip-el
      tooltip-id)))

(defn- pinned?
  "Returns whether or not a tooltip is pinned."
  [tt-el]
  (= "true" (.attr ($ tt-el) "data-pinned")))

(defn- pin-down! [tooltip-id]
  ;; TODO: write me
  )

(defn- remove-pin! [tooltip-id]
  ;; TODO: write me
  )

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- on-click [js-evt]
  (if-let [tooltip-id (evt->tt-num js-evt)]
    (if (pinned? (by-id tooltip-id))
      (remove-pin! tooltip-id)
      (pin-down! tooltip-id))))

(defn- on-mouseenter [js-evt]
  (if-let [tooltip-id (evt->tt-num js-evt)]
    (let [current-target (aget js-evt "currentTarget")
          target-coords (.offset ($ current-target))]
      (.css ($ (str "#" tooltip-id)) target-coords)
      (show-el! tooltip-id))))

(defn- on-mouseleave [js-evt]
  (if-let [tooltip-id (evt->tt-num js-evt)]
    (hide-el! tooltip-id)))

;;------------------------------------------------------------------------------
;; Init
;;------------------------------------------------------------------------------

(defn init!
  "Initialize tooltip events."
  []
  (doto ($ "body")
    (.on "click" ".tooltip-link-0e91b" on-click)
    (.on "mouseenter" ".tooltip-link-0e91b" on-mouseenter)
    (.on "mouseleave" ".tooltip-link-0e91b" on-mouseleave)))