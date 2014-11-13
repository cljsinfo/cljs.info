(ns cljsinfo-client.threading-macro
  (:require-macros [hiccups.core :as hiccups])
  (:require
    hiccups.runtime
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(declare grid)

(def $ js/jQuery)

(def x-unit 50)
(def y-unit 100)

;; labels:
;; "thread-first"
;; "thread-last"

;;------------------------------------------------------------------------------
;; Example 1
;;------------------------------------------------------------------------------

(hiccups/defhtml ex1-html []
  [:div#line1.line-num "1"]
  [:div#line2.line-num "2"]
  [:div#line3.line-num "3"]

  [:div#openParen1 "("]

  [:div#arrow1.arrow "-"]
  [:div#arrow2.arrow ">"]
  [:div#symbolA "a"]

  [:div#openParen2 "("]
  [:div#symbolB "b"]
  [:div#symbolC "c"]
  [:div#symbolD "d"]
  [:div#closeParen2 ")"]

  [:div#openParen3 "("]
  [:div#symbolX "x"]
  [:div#symbolY "y"]
  [:div#symbolZ "z"]
  [:div#closeParen3 ")"]

  [:div#closeParen1 ")"]
  )

(def ex1-pos1 {
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

(def ex1-pos2 {
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

(def ex1-pos3 {
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

(def ex1-pos4 {
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

(def ex1-pos5 {
  :openParen1 nil

  :arrow1 nil
  :arrow2 nil

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

  :closeParen1 nil})

(def ex1-pos6 {
  :openParen1 nil
  :arrow1 nil
  :arrow2 nil

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
  :closeParen3 [17 1]

  :closeParen1 nil})

;;------------------------------------------------------------------------------
;; Example 2
;;------------------------------------------------------------------------------

(hiccups/defhtml ex2-html []
  [:div#line1.line-num (grid -1 1) "1"]
  [:div#line2.line-num (grid -1 2) "2"]
  [:div#line3.line-num (grid -1 3) "3"]

  [:div#openParen1 "("]

  [:div#arrow1.arrow "-"]
  [:div#arrow2.arrow ">"]

  [:div#symbolM "m"]

  [:div#kwdA1 ":"]
  [:div#kwdA2 "a"]

  [:div#kwdB1 ":"]
  [:div#kwdB2 "b"]

  [:div#kwdC1 ":"]
  [:div#kwdC2 "c"]

  [:div#openParen2 "("]
  [:div#closeParen2 ")"]

  [:div#openParen3 "("]
  [:div#closeParen3 ")"]

  [:div#closeParen1  ")"]
  )

(def ex2-pos1 {
  :openParen1 [1 1]
  :arrow1 [2 1]
  :arrow2 [3 1]

  :symbolM [5 1]

  :kwdA1 [7 1]
  :kwdA2 [8 1]

  :kwdB1 [9 1]
  :kwdB2 [10 1]

  :kwdC1 [12 1]
  :kwdC2 [13 1]

  :openParen2 nil
  :closeParen2 nil

  :openParen3 nil
  :closeParen3 nil

  :closeParen1 [14 1]
  })

;;------------------------------------------------------------------------------
;; Animation
;;------------------------------------------------------------------------------

(def animation-duration 800)

(defn- animate-single [[k v]]
  (let [fade-out? (nil? v)
        sel (str "#" (name k))
        $el ($ sel)]
    (.velocity $el
      (if fade-out?
        (js-obj "opacity" 0)
        (js-obj "left" (* x-unit (dec (first v)))
                "opacity" 1
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

(defn- set-single! [[k v]]
  (let [fade-out? (nil? v)
        sel (str "#" (name k))
        $el ($ sel)]
    (.css $el
      (if fade-out?
        (js-obj "opacity" 0)
        (js-obj
          "left" (* x-unit (dec (first v)))
          "opacity" 1
          "top" (* y-unit (dec (second v))))))))

(defn- set-position-instant! [pos]
  (doall (map set-single! pos)))



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
  ; (js/setTimeout #(animate-to-position ex1-pos2) (round 1))
  ; (js/setTimeout #(animate-to-position ex1-pos3) (round 2))
  ; (js/setTimeout #(animate-to-position ex1-pos4) (round 3))
  ; (js/setTimeout #(animate-to-position ex1-pos5) (round 4))
  ; (js/setTimeout #(animate-to-position ex1-pos6) (round 5))

  ; (set-position-instant! ex1-pos6)
  ; (js/setTimeout #(animate-to-position ex1-pos5) (round 2))
  ; (js/setTimeout #(animate-to-position ex1-pos4) (round 3))
  ; (js/setTimeout #(animate-to-position ex1-pos3) (round 4))
  ; (js/setTimeout #(animate-to-position ex1-pos2) (round 5))
  ; (js/setTimeout #(animate-to-position ex1-pos1) (round 6))


  (set-position-instant! ex2-pos1)


  )

(defn init!
  "Initialize the threading macro page."
  []
  ;(set-html! "threadingMacroBody" (ex1-html))
  (set-html! "threadingMacroBody" (ex2-html))
  (animate!)
  )