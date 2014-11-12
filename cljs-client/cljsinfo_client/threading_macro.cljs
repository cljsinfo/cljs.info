(ns cljsinfo-client.threading-macro
  (:require-macros [hiccups.core :as hiccups])
  (:require
    hiccups.runtime
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; HTML
;;------------------------------------------------------------------------------

(def x-unit 50)
(def y-unit 100)

(def position-1 {
  :openParen1 [1 1]

  :arrow1 [2 1]
  :arrow2 [3 1]
  :symbolA [5 1]

  :openParen2 [3 2]
  :symbolB [4 2]
  :symbolC [6 2]
  :symbolD [8 2]
  :closeParen2 [9 2]

  :openParen3 [3 3]
  :symbolX [4 3]
  :symbolY [6 3]
  :symbolZ [8 3]
  :closeParen3 [9 3]

  :closeParen1 [10 3]})

(def position-2 {
  :openParen1 [1 1]

  :arrow1 [2 1]
  :arrow2 [3 1]

  :openParen2 [3 2]
  :symbolB [4 2]
  :symbolA [6 2]
  :symbolC [8 2]
  :symbolD [10 2]
  :closeParen2 [11 2]

  :openParen3 [3 3]
  :symbolX [4 3]
  :symbolY [6 3]
  :symbolZ [8 3]
  :closeParen3 [9 3]

  :closeParen1 [10 3]})

(def position-3 {
  :openParen1 [1 1]

  :arrow1 [2 1]
  :arrow2 [3 1]

  :openParen2 [5 1]
  :symbolB [6 1]
  :symbolA [8 1]
  :symbolC [10 1]
  :symbolD [12 1]
  :closeParen2 [13 1]

  :openParen3 [3 2]
  :symbolX [4 2]
  :symbolY [6 2]
  :symbolZ [8 2]
  :closeParen3 [9 2]

  :closeParen1 [10 2]})

(def position-4 {
  :openParen1 [1 1]

  :arrow1 [2 1]
  :arrow2 [3 1]

  :openParen3 [3 2]
  :symbolX [4 2]
  :openParen2 [6 2]
  :symbolB [7 2]
  :symbolA [9 2]
  :symbolC [11 2]
  :symbolD [13 2]
  :closeParen2 [14 2]
  :symbolY [16 2]
  :symbolZ [18 2]
  :closeParen3 [19 2]

  :closeParen1 [20 2]})

(def position-5 {
  :openParen1 :fade-out

  :arrow1 :fade-out
  :arrow2 :fade-out

  :openParen3 [3 2]
  :symbolX [4 2]
  :openParen2 [6 2]
  :symbolB [7 2]
  :symbolA [9 2]
  :symbolC [11 2]
  :symbolD [13 2]
  :closeParen2 [14 2]
  :symbolY [16 2]
  :symbolZ [18 2]
  :closeParen3 [19 2]

  :closeParen1 :fade-out})

(def position-6 {
  :openParen3 [1 1]
  :symbolX [2 1]
  :openParen2 [4 1]
  :symbolB [5 1]
  :symbolA [7 1]
  :symbolC [9 1]
  :symbolD [11 1]
  :closeParen2 [12 1]
  :symbolY [14 1]
  :symbolZ [16 1]
  :closeParen3 [17 1]})

(def animation-duration 800)

(defn- animate-single [[k v]]
  (let [fade-out? (= v :fade-out)
        sel (str "#" (name k))
        $el ($ sel)]
    (.velocity $el
      (if fade-out?
        (js-obj "opacity" 0)
        (js-obj "left" (* x-unit (dec (first v)))
                "top" (* y-unit (dec (second v)))))
      (js-obj "duration" animation-duration))))

(defn- animate-to-position [pos]
  (doall (map animate-single pos)))



(defn- coords->style [x y]
  (str "left: " (* x-unit (dec x)) "px; "
       "top: " (* y-unit (dec y)) "px; "
       "height: " y-unit "px; "
       "width: " x-unit "px; "))

(defn- grid [x y]
  {:style (coords->style x y)})

(hiccups/defhtml shell []
  [:div#openParen1 (grid 1 1) "("]

  [:div#arrow1.arrow (grid 2 1) "-"]
  [:div#arrow2.arrow (grid 3 1) ">"]
  [:div#symbolA (grid 5 1) "a"]

  [:div#openParen2 (grid 3 2) "("]
  [:div#symbolB (grid 4 2) "b"]
  [:div#symbolC (grid 6 2) "c"]
  [:div#symbolD (grid 8 2) "d"]
  [:div#closeParen2 (grid 9 2) ")"]

  [:div#openParen3 (grid 3 3) "("]
  [:div#symbolX (grid 4 3) "x"]
  [:div#symbolY (grid 6 3) "y"]
  [:div#symbolZ (grid 8 3) "z"]
  [:div#closeParen3 (grid 9 3) ")"]

  [:div#closeParen1 (grid 10 3) ")"]
  )

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------


;;------------------------------------------------------------------------------
;; Init
;;------------------------------------------------------------------------------

(def time-between 1200)

(defn- round [n]
  (* n (+ animation-duration time-between)))

(defn- animate! []
  (js/setTimeout #(animate-to-position position-2) (round 1))
  (js/setTimeout #(animate-to-position position-3) (round 2))
  (js/setTimeout #(animate-to-position position-4) (round 3))
  (js/setTimeout #(animate-to-position position-5) (round 4))
  (js/setTimeout #(animate-to-position position-6) (round 5))
  )

(defn init!
  "Initialize the threading macro page."
  []
  (set-html! "threadingMacroBody" (shell))
  (animate!)
  )