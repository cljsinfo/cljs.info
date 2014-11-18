(ns cljs-cheatsheet.tooltips
  (:require
    [clojure.set :refer [difference]]
    [clojure.string :refer [replace]]
    [cljs-cheatsheet.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljs-cheatsheet.util :refer [js-log log uuid]]))

(def $ js/jQuery)
(def has-touch-events? (aget js/window "hasTouchEvents"))
(def tooltip-icon-sel ".tooltip-link-0e91b")
(def tooltip-sel ".tooltip-53dde")
(def left-arrow-class "left-arr-42ea1")
(def right-arrow-class "right-arr-d3345")
(def arrow-classes (str left-arrow-class " " right-arrow-class))
(def animation-speed "fast")

;;------------------------------------------------------------------------------
;; Helper
;;------------------------------------------------------------------------------

(defn- js-evt->tooltip-id
  "Returns a tooltip-id from a JS event or nil if the tooltip was not found."
  [js-evt]
  (let [current-target (aget js-evt "currentTarget")
        tooltip-num (.attr ($ current-target) "data-tooltip-id")
        tooltip-id (str "tooltip-" tooltip-num)
        tooltip-el (by-id tooltip-id)]
    (if tooltip-el
      tooltip-id)))

; (defn- pinned?
;   "Returns whether or not a tooltip is pinned."
;   [tt-el]
;   (= "true" (.attr ($ tt-el) "data-pinned")))

; (defn- pin-down! [tooltip-id]
;   )

; (defn- remove-pin! [tooltip-id]
;   )

(defn- within-bounds? [m-pos el-coords]
  (let [x (:x m-pos)
        y (:y m-pos)]
    (and (>= x (:x1 el-coords))
         (<= x (:x2 el-coords))
         (>= y (:y1 el-coords))
         (<= y (:y2 el-coords)))))

(defn- mouse-inside-tooltip? [m-pos tooltip-coords]
  (or (within-bounds? m-pos (:icon-bounds tooltip-coords))
      (within-bounds? m-pos (:tooltip-bounds tooltip-coords))))

;; create a box around the icon
(def icon-mouseout-padding 20)

(defn- icon-bounds [icon-el]
  (let [$icon-el ($ icon-el)
        icon-height (.height $icon-el)
        icon-width (.width $icon-el)
        icon-coords (.offset $icon-el)
        icon-x (+ (aget icon-coords "left") (/ icon-width 2))
        icon-y (+ (aget icon-coords "top") (/ icon-height 2))]
    {:x1 (- icon-x icon-mouseout-padding)
     :x2 (+ icon-x icon-mouseout-padding)
     :y1 (- icon-y icon-mouseout-padding)
     :y2 (+ icon-y icon-mouseout-padding 10) ;; be a little more generous around
                                             ;; the bottom of the tooltip arrow
     }))

(defn- tooltip-bounds [tooltip-id]
  (let [$tooltip-el ($ (str "#" tooltip-id))
        coords (.offset $tooltip-el)
        height (-> (.css $tooltip-el "height") (replace "px" "") int)
        width (-> (.css $tooltip-el "width") (replace "px" "") int)]
    {:x1 (aget coords "left")
     :x2 (+ (aget coords "left") width)
     :y1 (aget coords "top")
     :y2 (+ (aget coords "top") height)}))

;;------------------------------------------------------------------------------
;; Show / Hide Tooltips
;;------------------------------------------------------------------------------

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
        flip? (> (+ icon-x tooltip-width 50) browser-width)
        left (if flip? (- icon-x tooltip-width 30)
                       (+ icon-x 18))
        top (- icon-y 22)]
    (.removeClass $tooltip-el arrow-classes)
    (if flip?
      (.addClass $tooltip-el right-arrow-class)
      (.addClass $tooltip-el left-arrow-class))
    (.css $tooltip-el (js-obj
      "left" left
      "top" top))
    (.fadeIn $tooltip-el animation-speed)))

(defn- hide-all-tooltips! []
  (.fadeOut ($ tooltip-sel) animation-speed))

;;------------------------------------------------------------------------------
;; Hovered Tooltip Coordinates
;;------------------------------------------------------------------------------

(def hovered-tooltip-coords (atom nil))

;;------------------------------------------------------------------------------
;; Mouse Position
;;------------------------------------------------------------------------------

(def mouse (atom nil))

(defn- on-change-mouse [_ _ _ m-pos]
  (when (and @hovered-tooltip-coords
             (not (mouse-inside-tooltip? m-pos @hovered-tooltip-coords)))
    (hide-all-tooltips!)
    (reset! hovered-tooltip-coords nil)))

(add-watch mouse :change on-change-mouse)

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

; (defn- on-click [js-evt]
;   (if-let [tooltip-id (js-evt->tooltip-id js-evt)]
;     (if (pinned? (by-id tooltip-id))
;       (remove-pin! tooltip-id)
;       (pin-down! tooltip-id))))

(defn- mousemove-body [js-evt]
  (reset! mouse {
    :x (aget js-evt "pageX")
    :y (aget js-evt "pageY")}))

(defn- on-mouseenter-icon [js-evt]
  (when-let [tooltip-id (js-evt->tooltip-id js-evt)]
    (let [icon-el (aget js-evt "currentTarget")]
      (show-tooltip! icon-el tooltip-id)
      (reset! hovered-tooltip-coords {
        :tooltip-bounds (tooltip-bounds tooltip-id)
        :icon-bounds (icon-bounds icon-el)}))))

(defn- on-touchend-body [js-evt]
  (hide-all-tooltips!))

(defn- on-touchend-icon [js-evt]
  (.stopPropagation js-evt)
  (if-let [tooltip-id (js-evt->tooltip-id js-evt)]
    (show-tooltip! (aget js-evt "currentTarget") tooltip-id)))

;;------------------------------------------------------------------------------
;; Init and Events
;;------------------------------------------------------------------------------

;; TODO: touch events are not really polished yet
(defn- add-touch-events! []
  (-> ($ "body")
    (.on "touchend" on-touchend-body)
    (.on "touchend" tooltip-icon-sel on-touchend-icon)))

(defn init!
  "Initialize tooltip events."
  []
  (-> ($ "body")
    (.on "mousemove" mousemove-body)
    (.on "mouseenter" tooltip-icon-sel on-mouseenter-icon))
  (when has-touch-events?
    (add-touch-events!)))