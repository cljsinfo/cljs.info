(ns cljs-cheatsheet.tooltips
  (:require
    [clojure.string :refer [blank? replace split]]
    [clojure.walk :refer [keywordize-keys]]
    [cljs-cheatsheet.dom :refer [by-id hide-el! set-html! show-el!]]
    [cljs-cheatsheet.html :refer [fn-tooltip-inner]]
    [cljs-cheatsheet.util :refer [js-log log uuid]]))

(def $ js/jQuery)
(def has-touch-events? (aget js/window "hasTouchEvents"))
(def tooltip-icon-sel ".tooltip-link-0e91b")
(def tooltip-sel ".tooltip-53dde")
(def fn-link-sel ".fn-a8476, .inside-fn-c7607")
(def left-arrow-class "left-arr-42ea1")
(def right-arrow-class "right-arr-d3345")
(def symbol-tooltip-id "fnTooltip")
(def symbol-tooltip-sel (str "#" symbol-tooltip-id))
(def arrow-classes (str left-arrow-class " " right-arrow-class))

;;------------------------------------------------------------------------------
;; Helper
;;------------------------------------------------------------------------------

;; TODO: move this to util
(defn- half [n]
  (/ n 2))

(defn- js-evt->tooltip-id
  "Returns a tooltip-id from a JS event or nil if the tooltip was not found."
  [js-evt]
  (let [current-target (aget js-evt "currentTarget")
        tooltip-num (.attr ($ current-target) "data-tooltip-id")
        tooltip-id (str "tooltip-" tooltip-num)
        tooltip-el (by-id tooltip-id)]
    (if tooltip-el
      tooltip-id)))

(defn- within-bounds? [m-pos el-coords]
  (let [x (:x m-pos)
        y (:y m-pos)]
    (and (>= x (:x1 el-coords))
         (<= x (:x2 el-coords))
         (>= y (:y1 el-coords))
         (<= y (:y2 el-coords)))))

;; TODO: this function could be more general
(defn- mouse-inside-tooltip? [m-pos [box1 box2]]
  (or (within-bounds? m-pos box1)
      (within-bounds? m-pos box2)))

;;------------------------------------------------------------------------------
;; Position, Show, Hide Tooltips
;;------------------------------------------------------------------------------

;; NOTE: I'm sure the two "position!" functions could be combined somehow

;; used to create a box around the icon
(def icon-mouseout-padding 16)

;; add some wiggle room around the edge of the tooltip border
(def tooltip-mouseout-padding 4)

(defn- position-info-tooltip! [icon-el tooltip-id]
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
        _ (.css $tooltip-el (js-obj "display" "none"
                                    "left" 0
                                    "top" 0))

        tooltip-height (.outerHeight $tooltip-el)
        tooltip-width (.outerWidth $tooltip-el)
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
    {:icon-box {:x1 (- icon-x icon-mouseout-padding)
                :x2 (+ icon-x icon-mouseout-padding)
                :y1 (- icon-y icon-mouseout-padding)
                ;; be a little more generous around the bottom of the tooltip icon
                :y2 (+ icon-y icon-mouseout-padding 12)}
     :tooltip-box {:x1 (- tooltip-left tooltip-mouseout-padding)
                   :x2 (+ tooltip-left tooltip-width tooltip-mouseout-padding)
                   :y1 (- tooltip-top tooltip-mouseout-padding)
                   :y2 (+ tooltip-top tooltip-height tooltip-mouseout-padding)}}))

(def link-margin-right-padding 10)

;; TODO: need to deal with tooltips on the edge of the page
;; and tooltips at the bottom of the page (flip up)
(defn- position-fn-tooltip! [$link-el]
  (let [offset (.offset $link-el)
        link-x (aget offset "left")
        link-y (aget offset "top")
        link-height (.outerHeight $link-el)
        link-width (.outerWidth $link-el)
        $tooltip-el ($ symbol-tooltip-sel)
        tooltip-height (.outerHeight $tooltip-el)
        tooltip-width (.outerWidth $tooltip-el)
        tooltip-left (- (+ link-x (half link-width)) (half tooltip-width))
        tooltip-top (+ link-y (half link-height) 15)]
    ;; position the el
    (.css $tooltip-el (js-obj
      "left" tooltip-left
      "top" tooltip-top))

    ;; return bounds
    {:link-box {:x1 link-x
                :x2 (+ link-x link-width)
                :y1 link-y
                :y2 (+ link-y link-height 20)}
     :tooltip-box {:x1 tooltip-left
                   :x2 (+ tooltip-left tooltip-width)
                   :y1 tooltip-top
                   :y2 (+ tooltip-top tooltip-height)}}))

(defn- show-tooltip! [tooltip-id]
  (let [$tooltip-el ($ (str "#" tooltip-id))]
    (.show $tooltip-el)))

(defn- hide-all-tooltips-instant! []
  (.hide ($ tooltip-sel)))

(defn- hide-all-tooltips! []
  (.hide ($ tooltip-sel)))

;;------------------------------------------------------------------------------
;; Tooltip Atoms
;;------------------------------------------------------------------------------

(def info-tooltip (atom nil))
(def fn-tooltip (atom nil))

;;------------------------------------------------------------------------------
;; Mouse Position
;;------------------------------------------------------------------------------

;; NOTE: this might belong in it's own state namespace?

(def mouse (atom nil))

(defn- on-change-mouse [_ _ _ m-pos]
  ;; close info tooltips
  (when (and @info-tooltip
             (not (mouse-inside-tooltip? m-pos (vals @info-tooltip))))
    (hide-all-tooltips!)
    (reset! info-tooltip nil))

  ;; close symbol tooltips
  (when (and @fn-tooltip
             (not (mouse-inside-tooltip? m-pos (vals @fn-tooltip))))
    (hide-el! symbol-tooltip-id)
    (reset! fn-tooltip nil)))

(add-watch mouse :change on-change-mouse)

;;------------------------------------------------------------------------------
;; Symbol Tooltips
;;------------------------------------------------------------------------------

;; TODO: there is more to do here, but this is simple enough for now

(def docs (atom {}))

(defn- fetch-docs-success [js-response]
  (reset! docs (js->clj js-response)))

(defn- fetch-docs! []
  (.ajax $ (js-obj
    "url" "/docs.json"
    "success" fetch-docs-success)))

(fetch-docs!)

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- mousemove-body [js-evt]
  (reset! mouse {
    :x (aget js-evt "pageX")
    :y (aget js-evt "pageY")}))

(defn- mouseenter-info-icon [js-evt]
  (let [icon-el (aget js-evt "currentTarget")
        tooltip-id (js-evt->tooltip-id js-evt)]
    (when (and tooltip-id
               (not= tooltip-id (:tooltip-id @info-tooltip)))
      (let [tooltip-position (position-info-tooltip! icon-el tooltip-id)]
        (hide-all-tooltips-instant!)
        (reset! info-tooltip (merge tooltip-position {:tooltip-id tooltip-id}))
        (show-tooltip! tooltip-id)))))

(defn- mouseenter-fn-link [js-evt]
  (let [link-el (aget js-evt "currentTarget")
        $link-el ($ link-el)
        full-fn-name (.attr $link-el "data-fn-name")
        tooltip-data (keywordize-keys (get @docs full-fn-name))
        tooltip-currently-active? @fn-tooltip]
    (when (and tooltip-data
               (not tooltip-currently-active?))
      (set-html! symbol-tooltip-id (fn-tooltip-inner tooltip-data))
      (reset! fn-tooltip (position-fn-tooltip! $link-el))
      (show-el! symbol-tooltip-id))))

(defn- touchend-body [js-evt]
  (hide-all-tooltips!))

(defn- touchend-icon [js-evt]
  (.stopPropagation js-evt)
  (when-let [tooltip-id (js-evt->tooltip-id js-evt)]
    (let [icon-el (aget js-evt "currentTarget")]
      (hide-all-tooltips-instant!)
      (position-info-tooltip! icon-el tooltip-id)
      (show-tooltip! tooltip-id))))

;;------------------------------------------------------------------------------
;; Init and Events
;;------------------------------------------------------------------------------

;; TODO: touch events are not really polished yet
(defn- add-touch-events! []
  (-> ($ "body")
    (.on "touchend" touchend-body)
    (.on "touchend" tooltip-icon-sel touchend-icon)))

(defn init!
  "Initialize tooltip events."
  []
  (-> ($ "body")
    (.on "mousemove" mousemove-body)
    (.on "mouseenter" tooltip-icon-sel mouseenter-info-icon)
    (.on "mouseenter" fn-link-sel mouseenter-fn-link))
  (when has-touch-events?
    (add-touch-events!)))
