(ns cljsinfo-server.html
  (:require-macros [hiccups.core :as hiccups])
  (:require
    hiccups.runtime
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.util :as util]))

(def fs (js/require "fs"))

;;------------------------------------------------------------------------------
;; Hashed Assets
;;------------------------------------------------------------------------------

(def assets
  (if (.existsSync fs "assets.json")
    (js->clj (js/require "./assets.json"))
    {}))

(defn- asset [f]
  (get assets f f))

;;------------------------------------------------------------------------------
;; Site Head / Footer
;;------------------------------------------------------------------------------

(hiccups/defhtml site-head [page-title]
  "<!doctype html>"
  "<html lang='en-us'>"
  [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
    [:title "ClojureScript.info &raquo; " page-title]
    [:meta {:name "viewport" :content "width=device-width"}]
    [:link {:href "http://fonts.googleapis.com/css?family=Open+Sans:300,400,600"
            :rel "stylesheet"
            :type "text/css"}]
    [:link {:href "http://fonts.googleapis.com/css?family=Droid+Sans+Mono"
            :rel "stylesheet"
            :type "text/css"}]
    [:link {:rel "stylesheet" :href (asset "css/main.min.css")}]]
  "<body>")

(hiccups/defhtml site-footer
  ([] (site-footer false))
  ([init-page]
    [:script {:src "/js/libs/jquery-2.1.1.min.js"}]
    (if (:minified-client config)
      [:script {:src (asset "/js/client.min.js")}]
      [:script {:src (asset "/js/client.js")}])
    (if init-page
      [:script "CLJSINFO.init('" init-page "');"])
    "</body>"
    "</html>"))

;;------------------------------------------------------------------------------
;; Homepage
;;------------------------------------------------------------------------------

(hiccups/defhtml homepage []
  (site-head "Home")
  [:div.wrapper-cc101
    [:div.js-48d1f "JavaScript"]
    [:div.made-6bccb "made"]

    [:div.simple-ef853 "Simple"]
    ;;[:h1.title-0b151 [:span "JavaScript"] [:span.made-6bccb "made"] "Simple"]
    ]
  ;;[:h1 "Welcome to ClojureScript.info!"]
  (site-footer "homepage"))

;;------------------------------------------------------------------------------
;; Cheatsheet
;;------------------------------------------------------------------------------

(hiccups/defhtml cheatsheet []
  (site-head "Cheatsheet")
  [:div.wrapper-cc101
    [:div.header-2a8a6
      [:img.logo-6ced3 {:src "/img/clojure-logo.png" :alt "Clojure Logo"}]
      [:h1.title-7a29c "ClojureScript Cheatsheet"]
      [:input.search-70fb8 {:type "text" :placeholder "Search"}]
      [:label#toggleTooltips.tooltips-label-68aa0 [:i.fa.fa-check-square-o] "Show tooltips?"]
      [:div.clr-43e49]]

    [:h2.group-title-68f3c "Basics"]

    [:div.section-31efe
      [:h3.section-title-8ccf5 "Numbers"
        [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "numbers"}]]
      [:table.tbl-902f0
        [:tbody
          [:tr.odd-372e6
            [:td.label-9e0b7 "Create"]
            [:td.body-885f4
              [:span.literal-c3029 "\"abc\""]
              [:a.fn-a8476 "str"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Use"]
            [:td.body-885f4
              [:a.fn-a8476 "count"]
              [:a.fn-a8476 "get"]]]]]]

    [:div.section-31efe
      [:h3.section-title-8ccf5 "Strings"
        [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "strings"}]]
      [:table.tbl-902f0
        [:tbody
          [:tr.odd-372e6
            [:td.label-9e0b7 "Create"]
            [:td.body-885f4
              [:span.literal-c3029 "\"abc\""]
              [:a.fn-a8476 "str"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Use"]
            [:td.body-885f4
              [:a.fn-a8476 "count"]
              [:a.fn-a8476 "get"]]]]]]

    ;;[:div.clr-43e49]
    [:h2.group-title-68f3c "Collections"]

    [:div.section-31efe
      [:h3.section-title-8ccf5 "Vectors"
        [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "vectors"}]]
      [:table.tbl-902f0
        [:tbody
          [:tr.odd-372e6
            [:td.label-9e0b7 "Create"]
            [:td.body-885f4
              [:span.literal-block-5dec8 "[0 \"a\" :kwd]"]
              [:span.literal-block-5dec8 "(into [] my-coll)"]
              [:a.fn-a8476 "vector"]
              [:a.fn-a8476 "vec"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Examine"]
            [:td.body-885f4
              [:span.literal-block-5dec8 "(my-vec idx)"
                [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "vector-as-fn"}]]
              [:span.literal-block-5dec8 "(" [:a.inside-fn-c7607 "nth"] " my-vec idx)"]
              [:a.fn-a8476 "get"]
              [:a.fn-a8476 "peek"]]]
          [:tr.odd-372e6
            [:td.label-9e0b7 "'Change'"]
            [:td.body-885f4
              [:a.fn-a8476 "assoc"]
              [:a.fn-a8476 "conj"]
              [:a.fn-a8476 "pop"]
              [:a.fn-a8476 "subvec"]
              [:a.fn-a8476 "replace"]
              [:a.fn-a8476 "rseq"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Loop"]
            [:td.body-885f4
              [:a.fn-a8476 "mapv"]
              [:a.fn-a8476 "filterv"]
              [:a.fn-a8476 "reduce-kv"]]]]]]

    [:div.section-31efe
      [:h3.section-title-8ccf5 "Maps"
        [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "maps"}]]
      [:table.tbl-902f0
        [:tbody
          [:tr.odd-372e6
            [:td.label-9e0b7 "Create"]
            [:td.body-885f4
              [:span.literal-block-5dec8 "{:key1 \"a\" :key2 \"b\"}"]
              [:a.fn-a8476 "hash-map"]
              [:a.fn-a8476 "array-map"]
              [:a.fn-a8476 "zipmap"]
              [:a.fn-a8476 "sorted-map"]
              [:a.fn-a8476 "sorted-map-by"]
              [:a.fn-a8476 "frequencies"]
              [:a.fn-a8476 "group-by"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Examine"]
            [:td.body-885f4
              [:span.literal-block-5dec8 "(.-innerHTML el)"]
              [:span.literal-block-5dec8 "(" [:a.inside-fn-c7607 "aget"] " el \"innerHTML\")"]]]
          [:tr.odd-372e6
            [:td.label-9e0b7 "'Change'"]
            [:td.body-885f4
              [:span.literal-block-5dec8 "(set! (.-innerHTML el) \"Hi!\")"]
              [:span.literal-block-5dec8 "(" [:a.inside-fn-c7607 "aset"] " el \"innerHTML\" \"Hi!\")"]]]]]]

    [:h2.group-title-68f3c "Sequences"]

    [:div.section-31efe
      [:h3.section-title-8ccf5 "JavaScript Interop" [:i.fa.fa-info-circle.tooltip-link-0e91b]]
      [:table.tbl-902f0
        [:tbody
          [:tr.odd-372e6
            [:td.label-9e0b7 "Create Native"]
            [:td.body-885f4
              [:a.fn-a8476 "array"]
              [:a.fn-a8476 "js-obj"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Property Access"]
            [:td.body-885f4
              [:span.literal-block-5dec8 "(.-innerHTML el)"]
              [:span.literal-block-5dec8 "(" [:a.inside-fn-c7607 "aget"] " el \"innerHTML\")"]]]
          [:tr.odd-372e6
            [:td.label-9e0b7 "Property Setting"]
            [:td.body-885f4
              [:span.literal-block-5dec8 "(set! (.-innerHTML el) \"Hi!\")"]
              [:span.literal-block-5dec8 "(" [:a.inside-fn-c7607 "aset"] " el \"innerHTML\" \"Hi!\")"]]]]]]


    [:div.clr-43e49]
  ]

  [:div#tooltip-strings.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:h4.tt-title-02d39 "Strings"]
    [:p "ClojureScript Strings are JavaScript Strings and have all of the native"
      " methods and properties that a JavaScript String has."]
    [:p "ClojureScript Strings must be defined using double quotes."]
    [:p "The " [:code "clojure.string"] " namespace provides a host of useful functions"
      " for dealing with strings."]]

  [:div#tooltip-vectors.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:h4.tt-title-02d39 "Vectors"]
    [:p "All vectors are collections and support the generic collection functions."]
    [:p "All vectors are also sequences and support the generic sequence functions."]
    [:p "A ClojureScript Vector is not the same thing as a JavaScript Array."
      " ie: " [:code "(.indexOf my-vec)"] " will not work on a ClojureScript Vector."]]

  [:div#tooltip-vector-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    ;;[:h4.tt-title-02d39 "Vectors"]
    [:p "A Vector can be used as a function in order to access it's elements."]
    ;;[:p "All vectors are also sequences and support the generic sequence functions."]
    ; [:p "A ClojureScript Vector is not the same thing as a JavaScript Array."
    ;   " ie: " [:code "(.indexOf my-vec)"] " will not work on a ClojureScript Vector."]
      ]

  [:div#tooltip-maps.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:h4.tt-title-02d39 "Maps"]
    [:p "Maps - or hash maps - are a powerful data structure used often in ClojureScript programs."]
    [:p "In JavaScript, Objects are commonly used as a de-facto hash map using strings as keys. "
      "A key in a ClojureScript Map can be any value, although commonly keywords are used."]
    [:p "All maps are collections and support the generic collection functions."]
    [:p "Maps are not a sequence, but most generic sequence functions can be used on a map. "
      "Sequence functions used on a map will return a sequence."]
  ]

  (site-footer "cheatsheet"))