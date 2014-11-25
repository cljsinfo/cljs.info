(ns cljs-cheatsheet.tooltips
  (:require
    [clojure.string :refer [blank? replace split]]
    [clojure.walk :refer [keywordize-keys]]
    [cljs-cheatsheet.dom :refer [by-id set-html!]]
    [cljs-cheatsheet.html :refer [inline-tooltip]]
    [cljs-cheatsheet.state :refer [active-tooltip mouse-position mousetrap-boxes]]
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

(def fade-in-speed 125)
(def fade-out-speed 100)

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

(defn- show-info-tooltip! [tooltip-id]
  (let [$tooltip-el ($ (str "#" tooltip-id))]
    (.fadeIn $tooltip-el fade-in-speed)))

(defn- hide-all-info-tooltips! []
  (.fadeOut ($ info-tooltip-sel) fade-out-speed))

;;------------------------------------------------------------------------------
;; Add to DOM
;;------------------------------------------------------------------------------

(defn- create-info-tooltip! [tt]
  (.append ($ "body") (str
    "<div id='" (:id tt) "'>"
    (:html tt)
    "</div>")))

(defn- create-inline-tooltip! [tt]
  (.append ($ "body") (inline-tooltip tt)))

;;------------------------------------------------------------------------------
;; Hide and Show
;;------------------------------------------------------------------------------

(defn- fade-and-destroy-tooltip! [tt]
  (let [id (:id tt)
        sel (str "#" id)
        $el ($ sel)]
    ;; TODO: change how this works
    (if (= :inline (:type tt))
      (.fadeOut $el fade-out-speed #(.remove $el))
      (.fadeOut $el fade-out-speed))))

(defn- fade-in-tooltip! [tt]
  (let [id (:id tt)
        sel (str "#" id)
        $el ($ sel)]
    (.fadeIn $el fade-in-speed)))

;;------------------------------------------------------------------------------
;; Position
;;------------------------------------------------------------------------------

(defn- position-info-tooltip! [tt]
  (let [$icon-el (:$icon-el tt)
        icon-height (.height $icon-el)
        icon-width (.width $icon-el)
        icon-coords (.offset $icon-el)
        icon-x (+ (aget icon-coords "left") (/ icon-width 2))
        icon-y (+ (aget icon-coords "top") (/ icon-height 2))
        browser-width (.width ($ js/window))
        $tooltip-el ($ (str "#" (:id tt)))

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

    ;; save the bounds of the tooltip
    (reset! mousetrap-boxes {
      :icon-box
        {:x1 (- icon-x icon-mouseout-padding)
         :x2 (+ icon-x icon-mouseout-padding)
         :y1 (- icon-y icon-mouseout-padding)
         ;; be a little more generous around the bottom of the tooltip icon
         :y2 (+ icon-y icon-mouseout-padding 12)}

      :tooltip-box
        {:x1 (- tooltip-left tooltip-mouseout-buffer)
         :x2 (+ tooltip-left tooltip-width tooltip-mouseout-buffer)
         :y1 (- tooltip-top tooltip-mouseout-buffer)
         :y2 (+ tooltip-top tooltip-height tooltip-mouseout-buffer)}})))

;; TODO: need to deal with tooltips on the edge of the page
;; and tooltips at the bottom of the page (flip up)
(defn- position-inline-tooltip! [tt]
  (let [$link-el (:$link-el tt)
        offset (.offset $link-el)
        link-x (aget offset "left")
        link-y (aget offset "top")
        link-height (.outerHeight $link-el)
        link-width (.outerWidth $link-el)
        $tooltip-el ($ (str "#" (:id tt)))
        tooltip-height (.outerHeight $tooltip-el)
        tooltip-width (.outerWidth $tooltip-el)
        tooltip-left (- (+ link-x (half link-width)) (half tooltip-width))
        tooltip-top (+ link-y link-height 4)]
    ;; position the el
    (.css $tooltip-el (js-obj
      "left" tooltip-left
      "top" tooltip-top))

    ;; save the bounds of the tooltip and link elements
    ;; NOTE: these numbers allow for a smidge of padding on the outside of the
    ;; link element    
    (reset! mousetrap-boxes {
      :link
        {:x1 (- link-x 1)
         :x2 (+ link-x link-width 2)
         :y1 link-y
         :y2 (+ link-y link-height 20)} ;; let them mouse down into the tooltip

      :tooltip
        {:x1 tooltip-left
         :x2 (+ tooltip-left tooltip-width)
         :y1 tooltip-top
         :y2 (+ tooltip-top tooltip-height)}})))

;;------------------------------------------------------------------------------
;; Tooltip Atoms
;;------------------------------------------------------------------------------

(def info-tooltip (atom nil))

(defn- on-change-tooltip [_ _ old-tt new-tt]
  ;; close tooltip
  (when (and old-tt (not= old-tt new-tt))
    (fade-and-destroy-tooltip! old-tt))

  ;; open info tooltip
  (when (and new-tt (= (:type new-tt) :info))
    ;; TODO: change this to add / remove the info tooltips from the DOM as needed
    ;; (create-info-tooltip! new-tt)
    (position-info-tooltip! new-tt)
    (fade-in-tooltip! new-tt))

  ;; open inline tooltip
  (when (and new-tt (= (:type new-tt) :inline))
    (create-inline-tooltip! new-tt)
    (position-inline-tooltip! new-tt)
    (fade-in-tooltip! new-tt)))

(add-watch active-tooltip :change on-change-tooltip)

;;------------------------------------------------------------------------------
;; Watch Mouse Position
;;------------------------------------------------------------------------------

(defn- on-change-mouse-position [_ _ _ pos]
  ;; hide tooltip when the mouse goes outside the box(es)
  (when (and @active-tooltip
             (not (mouse-inside-tooltip? pos (vals @mousetrap-boxes))))
    (reset! active-tooltip nil)
    (reset! mousetrap-boxes nil)))

(add-watch mouse-position :change on-change-mouse-position)

;;------------------------------------------------------------------------------
;; Docs for Inline Tooltips
;;------------------------------------------------------------------------------

;; TODO
;; - switch to transit.cljs
;; - save to localStorage for some period of time?

(def docs (atom {}))

(defn- fetch-docs-success [js-response]
  (reset! docs (js->clj js-response)))

(defn- fetch-docs! []
  (.ajax $ (js-obj
    "success" fetch-docs-success
    "url" "/docs.json")))

(fetch-docs!)

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- mousemove-body [js-evt]
  (reset! mouse-position {
    :x (aget js-evt "pageX")
    :y (aget js-evt "pageY")}))

(defn- mouseenter-info-icon [js-evt]
  (let [icon-el (aget js-evt "currentTarget")
        $icon-el ($ icon-el)
        info-id (.attr $icon-el "data-info-id")
        tooltip-already-showing? (and @active-tooltip
                                      (= info-id (:info-id @active-tooltip)))]
    (when (and info-id
               (not tooltip-already-showing?))
      (reset! active-tooltip {
        :$icon-el $icon-el
        :id (str "tooltip-" info-id)
        :info-id info-id
        :type :info }))))

(defn- mouseenter-link [js-evt]
  (let [link-el (aget js-evt "currentTarget")
        $link-el ($ link-el)
        full-name (.attr $link-el "data-full-name")
        tooltip-data (keywordize-keys (get @docs full-name))
        tooltip-already-showing? (and @active-tooltip
                                      (= full-name (:full-name @active-tooltip)))]
    (when (and tooltip-data
               (not tooltip-already-showing?))
      (reset! active-tooltip (merge tooltip-data {
        :id (uuid)
        :$link-el $link-el
        :type :inline })))))

(defn- touchend-body [js-evt]
  (hide-all-info-tooltips!))

(defn- touchend-icon [js-evt]
  (.stopPropagation js-evt)
  (when-let [tooltip-id (js-evt->tooltip-id js-evt)]
    (let [icon-el (aget js-evt "currentTarget")]
      (hide-all-info-tooltips!)
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
    (.on "mouseenter" symbol-link-sel mouseenter-link))
  ;; TODO: add these back
  ; (when has-touch-events?
  ;   (add-touch-events!))
  )
