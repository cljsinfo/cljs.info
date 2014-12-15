(ns cljsinfo-client.threading-macro
  (:require-macros [hiccups.core :as hiccups])
  (:require
    hiccups.runtime
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

(defn- half [x]
  (/ x 2))

(defn- twice [x]
  (* 2 x))

(defn- one? [x]
  (= 1 x))

(def reg-x-unit 40)
(def reg-y-unit (twice reg-x-unit))

(def small-x-unit 10)
(def small-y-unit (twice small-x-unit))

(def red-symbol-class "red-ccca5")

;;------------------------------------------------------------------------------
;; Helpers
;;------------------------------------------------------------------------------

;; TODO: I'm sure this could be combined with the functions below
(defn- max-x-in-frame [f]
  (->> f
       vals
       (remove nil?)
       (map first)
       (apply max)))

(defn- max-y-in-frame [f]
  (->> f
       vals
       (remove nil?)
       (map second)
       (apply max)))

(defn- max-x-value-in-frames [frames]
  (->> frames
       (map vals)
       (apply concat)
       (remove nil?)
       (map first)
       (apply max)))

(defn- max-y-value-in-frames [frames]
  (->> frames
       (map vals)
       (apply concat)
       (remove nil?)
       (map second)
       (apply max)))

(defn- coords->style [x y small?]
  (let [x-unit (if small? small-x-unit reg-x-unit)
        y-unit (if small? small-y-unit reg-y-unit)]
    (str "left:" (* x-unit (dec x)) "px;"
         "top:" (* y-unit (dec y)) "px;")))

(defn- grid
  ([x y]
    (grid x y false))
  ([x y small?]
    {:style (coords->style x y small?)}))

;;------------------------------------------------------------------------------
;; Thread First 1
;;------------------------------------------------------------------------------

(def thread-first-1 {
  :name "Generic Thread First"
  :chars {
    :op1  "("
    :arrow ["->" red-symbol-class]
    :a    "a"

    :op2 "("
    :b "b"
    :c "c"
    :d "d"
    :cp2 ")"

    :op3 "("
    :x   "x"
    :y   "y"
    :z   "z"
    :cp3 ")"

    :cp1 ")"
  }
  :frames [
    { ;; Frame 1
      :op1   [1 1]
      :arrow [2 1]
      :a     [5 1]

      :op2 [5 2]
      :b   [6 2]
      :c   [8 2]
      :d   [10 2]
      :cp2 [11 2]

      :op3 [5 3]
      :x   [6 3]
      :y   [8 3]
      :z   [10 3]
      :cp3 [11 3]

      :cp1 [12 3]
    }
    { ;; Frame 2
      :op1   [1 1]
      :arrow [2 1]
      :a     [5 1]

      :op2 [5 2]
      :b   [6 2]
      :c   [10 2]
      :d   [12 2]
      :cp2 [13 2]

      :op3 [5 3]
      :x   [6 3]
      :y   [8 3]
      :z   [10 3]
      :cp3 [11 3]

      :cp1 [12 3]
    }
    { ;; Frame 3
      :op1   [1 1]
      :arrow [2 1]

      :op2 [5 2]
      :b   [6 2]
      :a   [8 2]
      :c   [10 2]
      :d   [12 2]
      :cp2 [13 2]

      :op3 [5 3]
      :x   [6 3]
      :y   [8 3]
      :z   [10 3]
      :cp3 [11 3]

      :cp1 [12 3]
    }
    { ;; Frame 4
      :op1   [1 1]
      :arrow [2 1]

      :op2 [5 1]
      :b [6 1]
      :a [8 1]
      :c [10 1]
      :d [12 1]
      :cp2 [13 1]

      :op3 [5 2]
      :x [6 2]
      :y [8 2]
      :z [10 2]
      :cp3 [11 2]

      :cp1 [12 2]
    }
    { ;; Frame 5
      :op1   [1 1]
      :arrow [2 1]

      :op2 [5 1]
      :b [6 1]
      :a [8 1]
      :c [10 1]
      :d [12 1]
      :cp2 [13 1]

      :op3 [5 2]
      :x [6 2]
      :y [18 2]
      :z [20 2]
      :cp3 [21 2]

      :cp1 [22 2]
    }
    { ;; Frame 6
      :op1   [1 1]
      :arrow [2 1]

      :op2 [8 2]
      :b [9 2]
      :a [11 2]
      :c [13 2]
      :d [15 2]
      :cp2 [16 2]

      :op3 [5 2]
      :x [6 2]
      :y [18 2]
      :z [20 2]
      :cp3 [21 2]

      :cp1 [22 2]
    }
    { ;; Frame 7
      :op1 nil
      :arrow nil

      :op2 [8 2]
      :b [9 2]
      :a [11 2]
      :c [13 2]
      :d [15 2]
      :cp2 [16 2]

      :op3 [5 2]
      :x [6 2]
      :y [18 2]
      :z [20 2]
      :cp3 [21 2]

      :cp1 nil
    }
    { ;; Frame 8
      :op1 nil
      :arrow nil

      :op3 [1 1]
      :x [2 1]
      :op2 [4 1]
      :b [5 1]
      :a [7 1]
      :c [9 1]
      :d [11 1]
      :cp2 [12 1]
      :y [14 1]
      :z [16 1]
      :cp3 [17 1]

      :cp1 nil
    }
  ]})

;;------------------------------------------------------------------------------
;; Thread First 2
;;------------------------------------------------------------------------------

(def thread-first-2 {
  :name "Thread First with Keywords"
  :chars {
    :op1  "("
    :op2  "("
    :op3  "("
    :op4  "("

    :cp1 ")"
    :cp2 ")"
    :cp3 ")"
    :cp4 ")"

    :arrow ["->" red-symbol-class]
    :m "m"

    :kwd-a ":a"
    :kwd-b ":b"
    :kwd-c ":c"
  }
  :frames [
    { ;; Frame 1
      :op1 [1 1]
      :arrow [2 1]
      :m [5 1]
      :kwd-a [7 1]
      :kwd-b [10 1]
      :kwd-c [13 1]
      :cp1 [15 1]

      :op2 nil
      :op3 nil
      :op4 nil
      :cp2 nil
      :cp3 nil
      :cp4 nil
    }
    { ;; Frame 2
      :op1 [1 1]
      :arrow [2 1]
      :m [5 1]
      :kwd-a [5 2]
      :kwd-b [5 3]
      :kwd-c [5 4]
      :cp1 [7 4]

      :op2 nil
      :op3 nil
      :op4 nil
      :cp2 nil
      :cp3 nil
      :cp4 nil
    }
    { ;; Frame 3
      :op1 [1 1]
      :arrow [2 1]
      :m [5 1]
      :op2 [5 2]
      :kwd-a [6 2]
      :cp2 [8 2]
      :kwd-b [5 3]
      :kwd-c [5 4]
      :cp1 [7 4]

      :op3 nil
      :op4 nil
      :cp3 nil
      :cp4 nil
    }
    { ;; Frame 4
      :op1 [1 1]
      :arrow [2 1]
      :m [5 1]
      :op2 [5 2]
      :kwd-a [6 2]
      :cp2 [10 2]
      :kwd-b [5 3]
      :kwd-c [5 4]
      :cp1 [7 4]

      :op3 nil
      :op4 nil
      :cp3 nil
      :cp4 nil
    }
    { ;; Frame 5
      :op1 [1 1]
      :arrow [2 1]
      :op2 [5 2]
      :kwd-a [6 2]
      :m [9 2]
      :cp2 [10 2]
      :kwd-b [5 3]
      :kwd-c [5 4]
      :cp1 [7 4]

      :op3 nil
      :op4 nil
      :cp3 nil
      :cp4 nil
    }
    { ;; Frame 6
      :op1 [1 1]
      :arrow [2 1]
      :op2 [5 1]
      :kwd-a [6 1]
      :m [9 1]
      :cp2 [10 1]
      :kwd-b [5 2]
      :kwd-c [5 3]
      :cp1 [7 3]

      :op3 nil
      :op4 nil
      :cp3 nil
      :cp4 nil
    }
    { ;; Frame 7
      :op1 [1 1]
      :arrow [2 1]
      :op2 [5 1]
      :kwd-a [6 1]
      :m [9 1]
      :cp2 [10 1]

      :op3 [5 2]
      :kwd-b [6 2]
      :cp3 [8 2]

      :kwd-c [5 3]
      :cp1 [7 3]

      :op4 nil
      :cp4 nil
    }
    { ;; Frame 8
      :op1 [1 1]
      :arrow [2 1]
      :op2 [5 1]
      :kwd-a [6 1]
      :m [9 1]
      :cp2 [10 1]

      :op3 [5 2]
      :kwd-b [6 2]
      :cp3 [15 2]

      :kwd-c [5 3]
      :cp1 [7 3]

      :op4 nil
      :cp4 nil
    }
    { ;; Frame 9
      :op1 [1 1]
      :arrow [2 1]

      :op3 [5 2]
      :kwd-b [6 2]
      :op2 [9 2]
      :kwd-a [10 2]
      :m [13 2]
      :cp2 [14 2]
      :cp3 [15 2]

      :kwd-c [5 3]
      :cp1 [7 3]

      :op4 nil
      :cp4 nil
    }
    { ;; Frame 10
      :op1 [1 1]
      :arrow [2 1]

      :op3 [5 1]
      :kwd-b [6 1]
      :op2 [9 1]
      :kwd-a [10 1]
      :m [13 1]
      :cp2 [14 1]
      :cp3 [15 1]

      :kwd-c [5 2]
      :cp1 [7 2]

      :op4 nil
      :cp4 nil
    }
    { ;; Frame 11
      :op1 [1 1]
      :arrow [2 1]

      :op3 [5 1]
      :kwd-b [6 1]
      :op2 [9 1]
      :kwd-a [10 1]
      :m [13 1]
      :cp2 [14 1]
      :cp3 [15 1]

      :op4 [5 2]
      :kwd-c [6 2]
      :cp4 [8 2]
      :cp1 [9 2]
    }
    { ;; Frame 12
      :op1 [1 1]
      :arrow [2 1]

      :op3 [5 1]
      :kwd-b [6 1]
      :op2 [9 1]
      :kwd-a [10 1]
      :m [13 1]
      :cp2 [14 1]
      :cp3 [15 1]

      :op4 [5 2]
      :kwd-c [6 2]
      :cp4 [20 2]
      :cp1 [21 2]
    }
    { ;; Frame 13
      :op1 [1 1]
      :arrow [2 1]

      :op4 [5 2]
      :kwd-c [6 2]
      :op3 [9 2]
      :kwd-b [10 2]
      :op2 [13 2]
      :kwd-a [14 2]
      :m [17 2]
      :cp2 [18 2]
      :cp3 [19 2]
      :cp4 [20 2]
      :cp1 [21 2]
    }
    { ;; Frame 14
      :op1 nil
      :arrow nil

      :op4 [5 2]
      :kwd-c [6 2]
      :op3 [9 2]
      :kwd-b [10 2]
      :op2 [13 2]
      :kwd-a [14 2]
      :m [17 2]
      :cp2 [18 2]
      :cp3 [19 2]
      :cp4 [20 2]

      :cp1 nil
    }
    { ;; Frame 15
      :op1 nil
      :arrow nil

      :op4 [1 1]
      :kwd-c [2 1]
      :op3 [5 1]
      :kwd-b [6 1]
      :op2 [9 1]
      :kwd-a [10 1]
      :m [13 1]
      :cp2 [14 1]
      :cp3 [15 1]
      :cp4 [16 1]

      :cp1 nil
    }
  ]
  })

;;------------------------------------------------------------------------------
;; Animations
;;------------------------------------------------------------------------------

(def animations [
  thread-first-1
  thread-first-2
  ])

;;------------------------------------------------------------------------------
;; Markup
;;------------------------------------------------------------------------------

(hiccups/defhtml option [idx a]
  [:option {:value idx} (:name a)])

(hiccups/defhtml dropdown []
  [:select#animationSelect
    (map-indexed option animations)])

(hiccups/defhtml big-char [[k v]]
  (let [the-char (if (vector? v) (first v) v)
        extra-class (if (vector? v) (second v))]
    [:div
      {:class (str "big-char-6b108" (when extra-class (str " " extra-class)))
       :id (str "bigChar-" (name k))}
      the-char]))

(hiccups/defhtml big-line-number [n]
  [:div.big-num-c7cf6 (grid -0.5 n) n])

(hiccups/defhtml big-screen-chars [a]
  (let [frames (:frames a)
        max-y (max-y-value-in-frames frames)
        chars (:chars a)]
    (list
      (map big-line-number (range 1 (inc max-y)))
      (map big-char chars))))

(hiccups/defhtml frame-char [chars [k v]]
  (when v
    (let [ch1 (k chars)
          ch2 (if (vector? ch1) (first ch1) ch1)
          extra-class (if (vector? ch1) (second ch1))]
      [:div
        (merge {:class (str "small-char-3142f" (when extra-class
                                                 (str " " extra-class)))}
               (grid (first v) (second v) true))
        ch2])))

(hiccups/defhtml frame-line-number [n]
  [:div.small-num-59b3c (grid -0.5 n true) n])

(hiccups/defhtml frame-html [chars max-x max-y frame-idx frame]
  (let [width (* max-x small-x-unit)
        height (* max-y small-y-unit)]
    [:div.small-frame-ac2ae
      {:data-frame-index frame-idx
       :id (str "frame-" frame-idx)
       :style (str "height: " height "px; width: " width "px")}
      [:span.step-e483d (str "Step " (inc frame-idx))]
      (map (partial frame-char chars) frame)
      (map frame-line-number (range 1 (inc max-y)))]))

(hiccups/defhtml frames-html [animation]
  (let [chars (:chars animation)
        frames (:frames animation)
        max-x (max-x-value-in-frames frames)
        width (* max-x reg-x-unit)
        max-y (max-y-value-in-frames frames)
        height (* max-y small-y-unit)]
    (list
      (map-indexed (partial frame-html chars max-x max-y) frames)
      [:div.clr-43e49])))

(hiccups/defhtml buttons [a]
  [:button#pauseButton.btn-db258
    {:style "display:none"}
    [:i.fa.fa-pause] "Pause"]
  [:button#playButton.btn-db258
    [:i.fa.fa-play] "Play"])

;;------------------------------------------------------------------------------
;; Animation
;;------------------------------------------------------------------------------

(def main-animation-speed 800)

;; TODO: this function could be cleaner
(defn- animate-char! [[k v]]
  (let [$el ($ (str "#bigChar-" (name k)))
        el-hidden? (= "0" (.css $el "opacity"))
        fade-out? (nil? v)
        fade-in? (and el-hidden? (not fade-out?))
        left (if-not fade-out? (* reg-x-unit (dec (first v))))
        top (if-not fade-out? (* reg-y-unit (dec (second v))))]
    ;; position the character instantly if we are fading in
    (when fade-in?
      (.css $el (js-obj "left" left "top" top)))

    ;; animate
    (.velocity $el
      (cond
        fade-out?
          (js-obj "opacity" 0)
        fade-in?
          (js-obj "opacity" 1)
        :else
          (js-obj "left" left
                  "opacity" 1
                  "top" top))
      (js-obj "duration" main-animation-speed))))

(defn- animate-to-position! [pos]
  (doall (map animate-char! pos)))

(defn- set-char! [[k v]]
  (let [fade-out? (nil? v)
        $el ($ (str "#bigChar-" (name k)))]
    (.css $el
      (if fade-out?
        (js-obj "opacity" 0)
        (js-obj "left" (* reg-x-unit (dec (first v)))
                "opacity" 1
                "top" (* reg-y-unit (dec (second v))))))))

(defn- set-position-instant! [pos]
  (doall (map set-char! pos)))

;;------------------------------------------------------------------------------
;; Current Animation
;;------------------------------------------------------------------------------

(def current-animation (atom nil))

(defn- load-animation! [a]
  (set-html! "bigScreen" (big-screen-chars a))
  (set-html! "framesContainer" (frames-html a))
  (set-html! "buttonsContainer" (buttons a))
  (let [max-y (max-y-value-in-frames (:frames a))
        big-screen-height (+ (* max-y reg-y-unit) (half reg-y-unit))]
    (.height ($ "#bigScreen") big-screen-height)))

(defn- on-change-animation [_kwd _atom old-a new-a]
  (load-animation! new-a))

(add-watch current-animation :change on-change-animation)

;;------------------------------------------------------------------------------
;; Current Frame
;;------------------------------------------------------------------------------

(def current-frame-index (atom 0))
(def active-frame-class "active-frame-938e5")

(defn- on-change-frame [_kwd _atom old-idx new-idx]
  (let [animation @current-animation
        frames (:frames animation)
        new-frame (nth frames new-idx)]
    ;; toggle active frame
    (.removeClass ($ (str "#frame-" old-idx)) active-frame-class)
    (.addClass    ($ (str "#frame-" new-idx)) active-frame-class)

    ;; animate if the frames are adjacent, else set position instantly
    (if (one? (js/Math.abs (- new-idx old-idx)))
      (animate-to-position! new-frame)
      (set-position-instant! new-frame))))

(add-watch current-frame-index :change on-change-frame)

;;------------------------------------------------------------------------------
;; Play / Pause Button
;;------------------------------------------------------------------------------

(def playing? (atom false))
(def time-between-rounds (* main-animation-speed 2))

(defn- on-last-frame? []
  (= (count (:frames @current-animation))
     (inc @current-frame-index)))

(defn- next-animation-step! []
  (let [last-frame? (on-last-frame?)]
    (when (and @playing?
               (not last-frame?))
      (swap! current-frame-index inc)
      (js/setTimeout next-animation-step! time-between-rounds))

    ;; animation is over
    (when last-frame?
      (reset! playing? false))))

(defn- on-change-playing [_kwd _atom _old p?]
  ;; toggle play / pause buttons
  (if p?
    (do (hide-el! "playButton")
        (show-el! "pauseButton"))
    (do (show-el! "playButton")
        (hide-el! "pauseButton")))

  ;; reset to frame 0 if we are on the last frame
  (when (and p?
             (on-last-frame?))
    (reset! current-frame-index 0))

  ;; kick off the animation
  (when p?
    (js/setTimeout next-animation-step! main-animation-speed)))

(add-watch playing? :change on-change-playing)

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- change-animation-select []
  (let [anim-index (int (.val ($ "#animationSelect")))
        anim (nth animations anim-index false)]
    (when anim
      (reset! current-animation anim)
      (reset! current-frame-index 0))))

(defn- click-frame [js-evt]
  (let [$current-target ($ (aget js-evt "currentTarget"))
        frame-index (int (.attr $current-target "data-frame-index"))]
    ;; TODO: verify that frame-index is valid here
    (when-not (= frame-index @current-frame-index)
      (reset! current-frame-index frame-index))))

(defn- click-play-btn [js-evt]
  (reset! playing? true))

(defn- click-pause-btn [js-evt]
  (reset! playing? false))

(def events-added? (atom false))

;; NOTE: this is a "run once" function
(defn- add-events! []
  (when-not @events-added?
    (doto ($ "body")
      (.on "change" "#animationSelect" change-animation-select)
      (.on "click" ".small-frame-ac2ae" click-frame)
      (.on "click" "#playButton" click-play-btn)
      (.on "click" "#pauseButton" click-pause-btn))
    (reset! events-added? true)))

;;------------------------------------------------------------------------------
;; Init
;;------------------------------------------------------------------------------

(defn init!
  "Initialize the threading macro page."
  []
  (set-html! "top" (dropdown))
  (add-events!)
  (.change ($ "#animationSelect")))
