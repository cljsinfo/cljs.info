(ns cljs-cheatsheet.tooltips
  (:require
    [clojure.string :refer [replace]]
    [cljs-cheatsheet.dom :refer [by-id]]
    [cljs-cheatsheet.util :refer [js-log log]]))

(def $ js/jQuery)
(def has-touch-events? (aget js/window "hasTouchEvents"))
(def tooltip-icon-sel ".tooltip-link-0e91b")
(def tooltip-sel ".tooltip-53dde")
(def left-arrow-class "left-arr-42ea1")
(def right-arrow-class "right-arr-d3345")
(def arrow-classes (str left-arrow-class " " right-arrow-class))

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

;;------------------------------------------------------------------------------
;; Position, Show, Hide Tooltips
;;------------------------------------------------------------------------------

;; used to create a box around the icon
(def icon-mouseout-padding 16)

;; add some wiggle room around the edge of the tooltip border
(def tooltip-mouseout-padding 4)

(defn- position-tooltip! [icon-el tooltip-id]
  (let [$icon-el ($ icon-el)
        icon-height (.height $icon-el)
        icon-width (.width $icon-el)
        icon-coords (.offset $icon-el)
        icon-x (+ (aget icon-coords "left") (/ icon-width 2))
        icon-y (+ (aget icon-coords "top") (/ icon-height 2))
        browser-width (.width ($ js/window))
        $tooltip-el ($ (str "#" tooltip-id))
        ;; this little hack prevents bugs with the tooltip width calculation
        ;; when it is near the edge of the page
        _ (.css $tooltip-el (js-obj "display" "none" "left" 0 "top" 0))
        tooltip-height (-> (.css $tooltip-el "height") (replace "px" "") int)
        tooltip-width (-> (.css $tooltip-el "width") (replace "px" "") int)
        flip? (> (+ icon-x tooltip-width 30) browser-width)
        tooltip-left (if flip? (- icon-x tooltip-width 11)
                               (+ icon-x 18))
        tooltip-top (- icon-y 22)]
    ;; toggle arrow classes
    (.removeClass $tooltip-el arrow-classes)
    (if flip?
      (.addClass $tooltip-el right-arrow-class)
      (.addClass $tooltip-el left-arrow-class))

    ;; position the element
    (.css $tooltip-el (js-obj
      "left" tooltip-left
      "top" tooltip-top))

    ;; return the bounds of the tooltip
    {:icon-bounds {:x1 (- icon-x icon-mouseout-padding)
                   :x2 (+ icon-x icon-mouseout-padding)
                   :y1 (- icon-y icon-mouseout-padding)
                   ;; be a little more generous around the bottom of the tooltip icon
                   :y2 (+ icon-y icon-mouseout-padding 12)}
     :tooltip-bounds {:x1 (- tooltip-left tooltip-mouseout-padding)
                      :x2 (+ tooltip-left tooltip-width tooltip-mouseout-padding)
                      :y1 (- tooltip-top tooltip-mouseout-padding)
                      :y2 (+ tooltip-top tooltip-height tooltip-mouseout-padding)}}))

(def fade-in-speed 150)
(def fade-out-speed 100)

(defn- show-tooltip! [tooltip-id]
  (let [$tooltip-el ($ (str "#" tooltip-id))]
    (.fadeIn $tooltip-el fade-in-speed)))

(defn- hide-all-tooltips-instant! []
  (.hide ($ tooltip-sel)))

(defn- hide-all-tooltips! []
  (.fadeOut ($ tooltip-sel) fade-out-speed))

;;------------------------------------------------------------------------------
;; Hovered Tooltip Position
;;------------------------------------------------------------------------------

(def hovered-tooltip (atom nil))

;;------------------------------------------------------------------------------
;; Mouse Position
;;------------------------------------------------------------------------------

(def mouse (atom nil))

(defn- on-change-mouse [_ _ _ m-pos]
  (when (and @hovered-tooltip
             (not (mouse-inside-tooltip? m-pos @hovered-tooltip)))
    (hide-all-tooltips!)
    (reset! hovered-tooltip nil)))

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
  (let [icon-el (aget js-evt "currentTarget")
        tooltip-id (js-evt->tooltip-id js-evt)]
    (when (and tooltip-id
               (not= tooltip-id (:tooltip-id @hovered-tooltip)))
      (let [tooltip-position (position-tooltip! icon-el tooltip-id)]
        (hide-all-tooltips-instant!)
        (reset! hovered-tooltip (merge tooltip-position
                                       {:tooltip-id tooltip-id}))
        (show-tooltip! tooltip-id)))))

(defn- on-touchend-body [js-evt]
  (hide-all-tooltips!))

(defn- on-touchend-icon [js-evt]
  (.stopPropagation js-evt)
  (when-let [tooltip-id (js-evt->tooltip-id js-evt)]
    (let [icon-el (aget js-evt "currentTarget")]
      (hide-all-tooltips-instant!)
      (position-tooltip! icon-el tooltip-id)
      (show-tooltip! tooltip-id))))

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