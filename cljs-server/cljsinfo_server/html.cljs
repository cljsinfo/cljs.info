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
    [:link {:href "http://fonts.googleapis.com/css?family=PT+Serif:400,700"
            :rel "stylesheet"
            :type "text/css"}]
    [:link {:href "http://fonts.googleapis.com/css?family=Open+Sans:400,300"
            :rel "stylesheet"
            :type "text/css"}]
    [:link {:href "http://fonts.googleapis.com/css?family=Source+Code+Pro"
            :rel "stylesheet"
            :type "text/css"}]
    [:link {:href "http://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700"
            :rel "stylesheet"
            :type "text/css"}]
    [:link {:rel "stylesheet" :href (asset "css/main.min.css")}]]
  "<body>")

(hiccups/defhtml site-footer []
  "</body>"
  "</html>")

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
  (site-footer))

;;------------------------------------------------------------------------------
;; Cheatsheet
;;------------------------------------------------------------------------------

(hiccups/defhtml cheatsheet []
  (site-head "Cheatsheet")
  [:div.wrapper-cc101
    [:h1 "ClojureScript Cheatsheet"]

    [:div.section-31efe
      [:h3.section-title-8ccf5 "Strings" [:span.info-a8579 "info"]]
      [:table
        [:tbody
          [:tr.odd-372e6
            [:td.label-9e0b7 "Create"]
            [:td
              [:span.literal-c3029 "\"abc\""]
              [:a.fn-a8476 "str"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Use"]
            [:td
              [:a.fn-a8476 "count"]
              [:a.fn-a8476 "get"]]]]]]

    [:div.section-31efe
      [:h3.section-title-8ccf5
        ;;[:span.big-literal-3beb2 "[]"]
        "Vectors"
        [:span.info-a8579 "info"]]
      [:table
        [:tbody
          [:tr.odd-372e6
            [:td.label-9e0b7 "Create"]
            [:td
              [:span.literal-c3029 "[1 \"b\" :c]"]
              [:span.literal-c3029 "(into [] coll)"]
              [:a.fn-a8476 "vector"]
              [:a.fn-a8476 "vec"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Examine"]
            [:td
              [:span.literal-c3029.block-4a5f9 "(my-vec idx)"]
              [:span.literal-c3029.block-4a5f9 "(" [:a.inside-fn-c7607 "nth"] " my-vec idx)"]
              [:a.fn-a8476 "get"]
              [:a.fn-a8476 "peek"]]]
          [:tr.odd-372e6
            [:td.label-9e0b7 "'Change'"]
            [:td
              [:a.fn-a8476 "assoc"]
              [:a.fn-a8476 "conj"]
              [:a.fn-a8476 "pop"]
              [:a.fn-a8476 "subvec"]
              [:a.fn-a8476 "replace"]
              [:a.fn-a8476 "rseq"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Loop"]
            [:td
              [:a.fn-a8476 "mapv"]
              [:a.fn-a8476 "filterv"]
              [:a.fn-a8476 "reduce-kv"]]]]]]

    [:div.section-31efe
      [:h3.section-title-8ccf5
        "JavaScript Interop"
        [:span.info-a8579 "info"]]
      [:table
        [:tbody
          [:tr.odd-372e6
            [:td.label-9e0b7 "Data"]
            [:td
              [:a.fn-a8476 "array"]
              [:a.fn-a8476 "js-obj"]]]
          [:tr.even-ff837
            [:td.label-9e0b7 "Property" [:br] "Access"]
            [:td
              [:span.literal-c3029.block-4a5f9 "(.-innerHTML el)"]
              [:span.literal-c3029 "(" [:a.inside-fn-c7607 "aget"] " el \"innerHTML\")"]]]
          [:tr.odd-372e6
            [:td.label-9e0b7 "Property" [:br] "Setting"]
            [:td
              [:span.literal-c3029.block-4a5f9 "(set! (.-innerHTML el) \"Hi!\")"]
              [:span.literal-c3029 "(" [:a.inside-fn-c7607 "aset"] " el \"innerHTML\" \"Hi!\")"]]]

        ]]]


                ]
  (site-footer))