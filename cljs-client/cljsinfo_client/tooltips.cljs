  (ns cljsinfo-client.tooltips
  (:require
    [clojure.set :refer [difference]]
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

(def has-touch-events? (aget js/window "hasTouchEvents"))

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
;; Show / Hide Tooltips
;;------------------------------------------------------------------------------

(def left-arrow-class "left-arr-42ea1")
(def right-arrow-class "right-arr-d3345")
(def arrow-classes (str left-arrow-class " " right-arrow-class))

(defn- show-tooltip! [icon-el tooltip-id]
  (let [$icon-el ($ icon-el)
        icon-height (.height $icon-el)
        icon-width (.width $icon-el)
        icon-coords (.offset $icon-el)
        icon-x (+ (aget icon-coords "left") (/ icon-width 2))
        icon-y (+ (aget icon-coords "top") (/ icon-height 2))
        browser-width (.width ($ js/window))
        $tooltip-el ($ (str "#" tooltip-id))
        tooltip-width (.width $tooltip-el)
        flip? (> (+ icon-x tooltip-width 50) browser-width)]
    (.removeClass $tooltip-el arrow-classes)
    (if flip?
      (.addClass $tooltip-el right-arrow-class)
      (.addClass $tooltip-el left-arrow-class))
    (.css $tooltip-el (js-obj
      "display" ""
      "left" icon-x
      "marginLeft" (if flip? (- 0 tooltip-width 31) 18)
      "top" (- icon-y 22)))))

(def tooltip-sel ".tooltip-53dde")

(defn- hide-all-tooltips! []
  (.hide ($ tooltip-sel)))

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- on-click [js-evt]
  (if-let [tooltip-id (evt->tt-num js-evt)]
    (if (pinned? (by-id tooltip-id))
      (remove-pin! tooltip-id)
      (pin-down! tooltip-id))))

(defn- on-mouseenter [js-evt]
  (when-let [tooltip-id (evt->tt-num js-evt)]
    (show-tooltip! (aget js-evt "currentTarget") tooltip-id)))

;; TODO: IE fires the mouseleave event while the mouse cursor is still inside
;; the tooltip icon, causing a very distracting flicker effect.
;; Need to find a fix. Maybe capture the x/y of everything on mouseenter and
;; add a "listenOnce" event on the body for the exit?
;; https://github.com/oakmac/clojurescript.info/issues/11
(defn- on-mouseleave [js-evt]
  (if-let [tooltip-id (evt->tt-num js-evt)]
    (hide-el! tooltip-id)))

(defn- on-touchend-body [js-evt]
  (hide-all-tooltips!))

(defn- on-touchend-icon [js-evt]
  (.stopPropagation js-evt)
  (if-let [tooltip-id (evt->tt-num js-evt)]
    (show-tooltip! (aget js-evt "currentTarget") tooltip-id)))

;;------------------------------------------------------------------------------
;; Init and Events
;;------------------------------------------------------------------------------

(def tt-link-sel ".tooltip-link-0e91b")

(defn- add-touch-events! []
  (doto ($ "body")
    (.on "touchend" on-touchend-body)
    (.on "touchend" tt-link-sel on-touchend-icon)))

(defn init!
  "Initialize tooltip events."
  []
  (doto ($ "body")
    (.on "click" tt-link-sel on-click)
    (.on "mouseenter" tt-link-sel on-mouseenter)
    (.on "mouseleave" tt-link-sel on-mouseleave))
  (when has-touch-events?
    (add-touch-events!)))