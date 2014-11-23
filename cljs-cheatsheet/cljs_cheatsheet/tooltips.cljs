(ns cljs-cheatsheet.tooltips
  (:require
    [clojure.string :refer [blank? replace split]]
    [clojure.walk :refer [keywordize-keys]]
    [cljs-cheatsheet.dom :refer [by-id hide-el! set-html! show-el!]]
    [cljs-cheatsheet.html :refer [symbol-tooltip-inner]]
    [cljs-cheatsheet.util :refer [half js-log log uuid]]))

(def $ js/jQuery)
(def has-touch-events? (aget js/window "hasTouchEvents"))

(def info-icon-sel ".tooltip-link-0e91b")
(def info-tooltip-sel ".tooltip-53dde")
(def left-arrow-class "left-arr-42ea1")
(def right-arrow-class "right-arr-d3345")
(def arrow-classes (str left-arrow-class " " right-arrow-class))

(def symbol-link-sel ".fn-a8476, .inside-fn-c7607")
(def symbol-tooltip-id "symbolTooltip")
(def symbol-tooltip-sel (str "#" symbol-tooltip-id))

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
(def tooltip-mouseout-buffer 4)

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
     :tooltip-box {:x1 (- tooltip-left tooltip-mouseout-buffer)
                   :x2 (+ tooltip-left tooltip-width tooltip-mouseout-buffer)
                   :y1 (- tooltip-top tooltip-mouseout-buffer)
                   :y2 (+ tooltip-top tooltip-height tooltip-mouseout-buffer)}}))

;; TODO: need to deal with tooltips on the edge of the page
;; and tooltips at the bottom of the page (flip up)
(defn- position-symbol-tooltip! [$link-el]
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
    ;; NOTE: these numbers allow for a smidge of padding on the outside of the
    ;; link element
    {:link-box {:x1 (- link-x 1)
                :x2 (+ link-x link-width 2)
                :y1 link-y
                :y2 (+ link-y link-height 20)} ;; let them mouse down into the tooltip
     :tooltip-box {:x1 tooltip-left
                   :x2 (+ tooltip-left tooltip-width)
                   :y1 tooltip-top
                   :y2 (+ tooltip-top tooltip-height)}}))

(defn- show-info-tooltip! [tooltip-id]
  (show-el! tooltip-id))

(defn- hide-all-info-tooltips! []
  (.hide ($ info-tooltip-sel)))

;;------------------------------------------------------------------------------
;; Tooltip Atoms
;;------------------------------------------------------------------------------

(def info-tooltip (atom nil))
(def symbol-tooltip (atom nil))

;;------------------------------------------------------------------------------
;; Mouse Position
;;------------------------------------------------------------------------------

;; NOTE: this might belong in it's own state namespace?

(def mouse (atom nil))

(defn- on-change-mouse [_ _ _ m-pos]
  ;; close info tooltips
  (when (and @info-tooltip
             (not (mouse-inside-tooltip? m-pos (vals @info-tooltip))))
    (hide-all-info-tooltips!)
    (reset! info-tooltip nil))

  ;; close symbol tooltips
  (when (and @symbol-tooltip
             (not (mouse-inside-tooltip? m-pos (vals @symbol-tooltip))))
    (hide-el! symbol-tooltip-id)
    (reset! symbol-tooltip nil)))

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
        (hide-all-info-tooltips!)
        (reset! info-tooltip (merge tooltip-position {:tooltip-id tooltip-id}))
        (show-info-tooltip! tooltip-id)))))

(defn- mouseenter-symbol-link [js-evt]
  (let [link-el (aget js-evt "currentTarget")
        $link-el ($ link-el)
        full-name (.attr $link-el "data-full-name")
        tooltip-data (keywordize-keys (get @docs full-name))
        tooltip-currently-active? @symbol-tooltip]
    (when (and tooltip-data
               (not tooltip-currently-active?))
      (set-html! symbol-tooltip-id (symbol-tooltip-inner tooltip-data))
      (reset! symbol-tooltip (merge (position-symbol-tooltip! $link-el)
                                {:full-name full-name}))
      (show-el! symbol-tooltip-id))))

(defn- touchend-body [js-evt]
  (hide-all-info-tooltips!))

(defn- touchend-icon [js-evt]
  (.stopPropagation js-evt)
  (when-let [tooltip-id (js-evt->tooltip-id js-evt)]
    (let [icon-el (aget js-evt "currentTarget")]
      (hide-all-info-tooltips!)
      (position-info-tooltip! icon-el tooltip-id)
      (show-info-tooltip! tooltip-id))))

;;------------------------------------------------------------------------------
;; Init and Events
;;------------------------------------------------------------------------------

;; TODO: touch events are not really polished yet
(defn- add-touch-events! []
  (-> ($ "body")
    (.on "touchend" touchend-body)
    (.on "touchend" info-icon-sel touchend-icon)))

(defn init!
  "Initialize tooltip events."
  []
  (-> ($ "body")
    (.on "mousemove" mousemove-body)
    (.on "mouseenter" info-icon-sel mouseenter-info-icon)
    (.on "mouseenter" symbol-link-sel mouseenter-symbol-link))
  (when has-touch-events?
    (add-touch-events!)))
