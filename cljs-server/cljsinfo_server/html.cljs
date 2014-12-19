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
    [:title "cljs.info &raquo; " page-title]
    [:meta {:name "viewport" :content "width=device-width"}]
    [:link {:rel "shortcut icon" :href "favicon.png" :type "image/png"}]
    [:link {:rel "stylesheet" :href (asset "css/main.min.css")}]]
  "<body>")

(hiccups/defhtml site-footer
  ([] (site-footer nil))
  ([init-page]
    [:script {:src "js/libs/jquery-2.1.1.min.js"}]
    (if (:minified-client config)
      [:script {:src (asset "js/client.min.js")}]
      [:script {:src (asset "js/client.js")}])
    (when init-page
      [:script "CLJSINFO.init('" init-page "');"])
    "</body>"
    "</html>"))

;;------------------------------------------------------------------------------
;; Homepage
;;------------------------------------------------------------------------------

(hiccups/defhtml top-nav-bar []
  [:div.header-outer-a295e
    [:div.header-inner-e9e98
      [:div.left-1764b
        [:a.main-link-bb01a {:href ""} "cljs" [:span.info-9c06a ".info"]]]
      [:div.right-e461e
        [:a.nav-link-890a3 {:href "/getting-started"} "Getting Started"]
        [:a.nav-link-890a3 {:href "/faq"} "FAQ"]
        [:a.nav-link-890a3 {:href "/docs"} "Documentation"]
        [:a.nav-link-890a3 {:href "/tutorials"} "Tutorials"]
        [:a.nav-link-890a3 {:href "/community"} "Community"]]
      [:div.clr-43e49]]])

(hiccups/defhtml jumbotron []
  [:div.title-outer-16d0f
    [:div.title-inner-df992
      [:div.left-1764b
        [:h1.title-2febf "ClojureScript"]
        [:h2.sub-d57b3 "JavaScript made simple"]
        [:p.blurb-7fa5b
          "ClojureScript is a functional programming language that targets "
          "JavaScript. It comes with a rich set of data types, an extensive "
          "core library, and a novel approach to state."]
        [:p.blurb-7fa5b
          "Programming for the browser" [:i.fa.fa-asterisk] " will never be "
          "the same."]
        [:p.additional-c55c0
          [:i.fa.fa-asterisk] "ClojureScript also works with Node.js."]]
      [:div.jumbo-right-94c4b
        [:a.primary-btn-7fcef {:href "/getting-started"} "Get Started"]
        [:div.btns-a0ca1
          [:a.left-btn-2f03d {:href "/rationale"} "Rationale"]
          [:a.right-btn-33d5b {:href "/docs"} "Docs"]]
        [:div.version-974cf "Latest: 0.0-2496"]
        [:div.version-974cf "Released 4 days ago"]]
      [:div.clr-43e49]]])

(def closure-compiler-url "https://developers.google.com/closure/compiler/docs/compilation_levels")

(hiccups/defhtml blurbs []
  [:div.outer-5cceb
    [:div.inner-a5192
      [:div.row-382d3
        [:div.selling-point-c4f14.left-1764b
          [:h4.hdr-e4d24 "100% JavaScript"]
          [:p.p-382d3
            "Use any JavaScript library from ClojureScript. Write a ClojureScript "
            "library to be used by JavaScript. 100% compatibility; no exceptions."]
          ; [:a.more-link-1a354 {:href "/tutorials/interop"} "JavaScript Interop &raquo;"]
          ]
        [:div.selling-point-c4f14.right-e461e
          [:h4.hdr-e4d24 "Built-in Code Organization"]
          [:p.p-382d3
            "Namespace and package management support are built directly into the "
            "ClojureScript language. No more depending on an external library and getting "
            "everyone on your team to follow the same conventions. It just works."]
          ; [:a.more-link-1a354 {:href "/tutorials/namespaces"} "Namespaces &raquo;"]
          ]
        [:div.clr-43e49]]

      [:div.row-382d3
        [:div.selling-point-c4f14.left-1764b
          [:h4.hdr-e4d24 "Whole Program Optimization"]
          [:p.p-382d3
            "ClojureScript uses the " [:a {:href closure-compiler-url} "Google Closure Compiler"]
            " in Advanced Optimizations mode to automatically remove dead code. "
            "Add as much code to your ClojureScript project as needed. The end "
            "result will always be as small as possible."]
          ; [:a.more-link-1a354 {:href "/tutorials/compiling"} "Compiling &raquo;"]
          ]
        [:div.selling-point-c4f14.right-e461e
          [:h4.hdr-e4d24 "Sane Approach to State"]
          [:p.p-382d3
            "Reasoning about state is simple in ClojureScript. All data is immutable by "
            "default, so you never have to worry about the value of something at one time vs "
            "another. When mutability is needed, ClojureScript distinguishes between setting "
            "and retrieving values so it's easy to keep track of what's going on in your "
            "program."]
          ; [:a.more-link-1a354 {:href "/tutorials/state"} "Atoms &raquo;"]
          ]
        [:div.clr-43e49]]

      [:div.row-382d3
        [:div.selling-point-c4f14.left-1764b
          [:h4.hdr-e4d24 "Unrivaled Syntax Power"]
          [:p.p-382d3
            "Say goodbye to boilerplate code with macros! ClojureScript is a LISP and allows "
            "for programmatic access to its syntax at the lowest level. Write powerful "
            "libraries and extend the language exactly as your program requires."]
          ; [:a.more-link-1a354 {:href "/tutorials/macros"} "Macros &raquo;"]
          ]
        [:div.selling-point-c4f14.right-e461e
          [:h4.hdr-e4d24 "You're in good hands"]
          [:p.p-382d3
            "The ClojureScript community is part of the broader Clojure community, a popular "
            "language that runs on the JVM. Clojure programmers are known for being fun, "
            "helpful, and very smart. We're glad you're here."]
          ; [:a.more-link-1a354 {:href "/community"} "Community &raquo;"]
          ]
        [:div.clr-43e49]]]])

(hiccups/defhtml homepage []
  (site-head "ClojureScript - JavaScript made simple")
  (jumbotron)
  (top-nav-bar)
  (blurbs)
  (site-footer "homepage"))

;;------------------------------------------------------------------------------
;; Doc Pages
;;------------------------------------------------------------------------------

(hiccups/defhtml doc-body [d]
  [:div "docs body"]
  )

(hiccups/defhtml doc-page [d]
  (site-head "cljs.core/foo")
  (doc-body d)
  (site-footer))
