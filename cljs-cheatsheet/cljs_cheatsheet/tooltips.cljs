(ns cljs-cheatsheet.tooltips
  (:require
    [clojure.string :refer [blank? replace split]]
    [cljs-cheatsheet.dom :refer [by-id get-element-box set-html!]]
    [cljs-cheatsheet.html :refer [inline-tooltip]]
    [cljs-cheatsheet.state :refer [active-tooltip mouse-position mousetrap-boxes]]
    [cljs-cheatsheet.util :refer [fetch-clj half js-log log point-inside-box?]]))

(def $ js/jQuery)
(def has-touch-events? (aget js/window "hasTouchEvents"))

(def info-icon-sel ".tooltip-link-0e91b")
(def links-sel ".fn-a8476, .inside-fn-c7607")

(def left-arrow-class "left-arr-42ea1")
(def right-arrow-class "right-arr-d3345")
(def arrow-classes (str left-arrow-class " " right-arrow-class))

(def min-brwsr-wdth-tt-algn 1460)
(def fade-speed 100)

;;------------------------------------------------------------------------------
;; Add to DOM
;;------------------------------------------------------------------------------

(defn- create-inline-tooltip! [tt]
  (.append ($ "body") (inline-tooltip tt)))

;;------------------------------------------------------------------------------
;; Hide and Show
;;------------------------------------------------------------------------------

(defn- fade-out-tooltip!
  ([tt]
    (fade-out-tooltip! tt false))
  ([tt destroy?]
    (let [$el ($ (str "#" (:id tt)))]
      (if destroy?
        (.fadeOut $el fade-speed #(.remove $el))
        (.fadeOut $el fade-speed)))))

(defn- fade-in-tooltip! [tt]
  (let [$el ($ (str "#" (:id tt)))]
    (.fadeIn $el fade-speed)))

;;------------------------------------------------------------------------------
;; Position
;;------------------------------------------------------------------------------

;; NOTE: I'm sure the two "position!" functions could be combined somehow

;; used to create a box around the icon
(def icon-mouseout-padding 16)

;; add some wiggle room around the edge of the tooltip border
(def tooltip-mouseout-buffer 4)

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
        _ (.css $tooltip-el #js {:display "none", :left 0, :top 0})

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
      :icon
        {:x1 (- icon-x icon-mouseout-padding)
         :x2 (+ icon-x icon-mouseout-padding)
         :y1 (- icon-y icon-mouseout-padding)
         ;; be a little more generous around the bottom of the tooltip icon
         :y2 (+ icon-y icon-mouseout-padding 12)}

      :tooltip
        {:x1 (- tooltip-left tooltip-mouseout-buffer)
         :x2 (+ tooltip-left tooltip-width tooltip-mouseout-buffer)
         :y1 (- tooltip-top tooltip-mouseout-buffer)
         :y2 (+ tooltip-top tooltip-height tooltip-mouseout-buffer)}})))

(def link-mousetrap-padding 2.5)
(def push-right 75)
(def push-right-further 150)
(def push-left 75)
(def push-left-further 160)

;; TODO: need to deal with tooltips tooltips at the bottom of the
;; page (flip up)
(defn- position-inline-tooltip! [tt]
  (let [$link-el (:$link-el tt)
        window-width (.width ($ js/window))
        link-offset (.offset $link-el)
        link-x (aget link-offset "left")
        link-y (aget link-offset "top")
        link-height (.outerHeight $link-el)
        link-width (.outerWidth $link-el)
        $tooltip-el ($ (str "#" (:id tt)))
        tooltip-height (.outerHeight $tooltip-el)
        tooltip-width (.outerWidth $tooltip-el)
        tooltip-left (- (+ link-x (half link-width)) (half tooltip-width))
        tooltip-right (+ tooltip-left tooltip-width)

        ;; TODO: all of this push left/right logic should probably be in it's
        ;; own function
        push-right? (and (neg? (- tooltip-left 10))
                         (pos? (- (+ tooltip-left push-right) 10)))
        push-right-further? (and (not push-right?)
                                 (neg? tooltip-left)
                                 (pos? (+ tooltip-left push-right-further)))
        push-left? (and (> (+ tooltip-right 10) window-width)
                        (< (- (+ tooltip-right 10) push-left) window-width))
        push-left-further? (and (not push-left?)
                                (> tooltip-right window-width))
        tooltip-left (cond
                       push-right? (+ tooltip-left push-right)
                       push-right-further? (+ tooltip-left push-right-further)
                       push-left? (- tooltip-left push-left)
                       push-left-further? (- tooltip-left push-left-further)
                       :else tooltip-left)
        tooltip-top (+ link-y link-height 5)]
    ;; add the correct arrow class
    (.addClass $tooltip-el
      (cond
        push-right? "push-right-6e671"
        push-right-further? "push-right-further-76f02"
        push-left? "push-left-267d7"
        push-left-further? "push-left-further-38c9b"
        :else "centered-53ffd"))

    ;; position the el
    (.css $tooltip-el (js-obj
      "left" tooltip-left
      "top"  tooltip-top))

    ;; save the bounds of the tooltip and link elements
    ;; NOTE: these numbers allow for a smidge of padding on the outside of the
    ;; link element
    (reset! mousetrap-boxes {
      :link
        {:x1 (- link-x link-mousetrap-padding)
         :x2 (+ link-x link-width link-mousetrap-padding)
         :y1 (- link-y link-mousetrap-padding)
         :y2 (+ link-y link-height 15)} ;; let them mouse down into the tooltip

      :tooltip
        {:x1 tooltip-left
         :x2 (+ tooltip-left tooltip-width)
         :y1 tooltip-top
         :y2 (+ tooltip-top tooltip-height)}})))

;;------------------------------------------------------------------------------
;; Tooltip Atoms
;;------------------------------------------------------------------------------

(defn- on-change-tooltip [_ _ old-tt new-tt]
  ;; close tooltip
  (when (and old-tt (not= old-tt new-tt))
    (fade-out-tooltip! old-tt (= :inline (:tt-type old-tt))))

  ;; open info tooltip
  (when (and new-tt (= (:tt-type new-tt) :info))
    (position-info-tooltip! new-tt)
    (fade-in-tooltip! new-tt))

  ;; open inline tooltip
  (when (and new-tt (= (:tt-type new-tt) :inline))
    (create-inline-tooltip! new-tt)
    (position-inline-tooltip! new-tt)
    (fade-in-tooltip! new-tt)))

(add-watch active-tooltip :change on-change-tooltip)

;;------------------------------------------------------------------------------
;; Watch Mouse Position
;;------------------------------------------------------------------------------

(defn- mouse-inside-tooltip? [m-pos [box1 box2]]
  (or (point-inside-box? m-pos box1)
      (point-inside-box? m-pos box2)))

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

;; TODO: cache docs in localStorage for some period of time?

(def docs (atom {}))

(fetch-clj "docs.json" #(reset! docs %))

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
        :tt-type :info }))))

(defn- mouseenter-link [js-evt]
  (let [$link-el ($ (aget js-evt "currentTarget"))
        full-name (.attr $link-el "data-full-name")
        tooltip-data (get @docs (keyword full-name))
        tooltip-already-showing? (and @active-tooltip
                                      (= full-name (:full-name @active-tooltip)))]
    (when (and tooltip-data
               (not tooltip-already-showing?))
      (reset! active-tooltip (merge tooltip-data {
        :id (random-uuid)
        :$link-el $link-el
        :tt-type :inline })))))

; (defn- touchend-body [js-evt]
;   (hide-all-info-tooltips!))

; (defn- touchend-icon [js-evt]
;   (.stopPropagation js-evt)
;   (when-let [tooltip-id (js-evt->tooltip-id js-evt)]
;     (let [icon-el (aget js-evt "currentTarget")]
;       (hide-all-info-tooltips!)
;       (show-info-tooltip! tooltip-id))))

;;------------------------------------------------------------------------------
;; Init and Events
;;------------------------------------------------------------------------------

;; TODO: touch events are not really polished yet
; (defn- add-touch-events! []
;   (-> ($ "body")
;     (.on "touchend" touchend-body)
;     (.on "touchend" info-icon-sel touchend-icon)))

(defn init!
  "Initialize tooltip events."
  []
  (-> ($ "body")
    (.on "mousemove" mousemove-body)
    (.on "mouseenter" info-icon-sel mouseenter-info-icon)
    (.on "mouseenter" links-sel mouseenter-link))
  ;; TODO: add these back
  ; (when has-touch-events?
  ;   (add-touch-events!))
  )
