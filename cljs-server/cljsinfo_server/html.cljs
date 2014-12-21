(ns cljsinfo-server.html
  (:require-macros [hiccups.core :as hiccups])
  (:require
    [clojure.string :refer [replace trim]]
    hiccups.runtime
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.util :refer [js-log log]]))

(def fs (js/require "fs"))
(def marked (js/require "marked"))

;;------------------------------------------------------------------------------
;; URLs
;;------------------------------------------------------------------------------

(defn- url [path]
  (if-let [base-href (:base-href config)]
    (str base-href "/" path)
    path))

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
    (if init-page
      [:script "CLJSINFO.init('" init-page "');"]
      [:script "CLJSINFO.init();"])
    "</body>"
    "</html>"))

;;------------------------------------------------------------------------------
;; Footer
;;------------------------------------------------------------------------------

(def github-url "https://github.com/clojure/clojurescript")
(def issues-url "http://dev.clojure.org/jira/browse/CLJS")
(def mailing-list-url "http://groups.google.com/group/clojurescript")

(hiccups/defhtml footer-docs-list []
  [:div.col-ace4b
    [:h5.hdr-856fa "Documentation"]
    [:ul
      [:li [:a.ftr-link-67c8e {:href (url "/getting-started")} "Getting Started"]]
      [:li [:a.ftr-link-67c8e {:href (url "/tutorials")} "Tutorials"]]
      [:li [:a.ftr-link-67c8e {:href (url "/docs")} "Docs"]]
      [:li [:a.ftr-link-67c8e {:href (url "/cheatsheet")} "Cheatsheet"]]]])

(hiccups/defhtml footer-learn-list []
  [:div.col-ace4b
    [:h5.hdr-856fa "Learn"]
    [:ul
      [:li [:a.ftr-link-67c8e {:href (url "/rationale")} "Rationale"]]
      [:li [:a.ftr-link-67c8e {:href (url "/faq")} "FAQ"]]]])

(hiccups/defhtml footer-community-list []
  [:div.col-ace4b
    [:h5.hdr-856fa "Community"]
    [:ul
      [:li [:a.ftr-link-67c8e {:href mailing-list-url}
        "Mailing List" [:i.fa.fa-external-link]]]
      ;; TODO: either make this a link or figure out how to style it as a non-link
      [:li [:a.ftr-link-67c8e {:href "#"} "IRC: #clojurescript"]]]])

(hiccups/defhtml footer-contribute-list []
  [:div.col-ace4b
    [:h5.hdr-856fa "Contribute"]
    [:ul
      [:li [:a.ftr-link-67c8e {:href github-url}
        "GitHub" [:i.fa.fa-external-link]]]
      [:li [:a.ftr-link-67c8e {:href issues-url}
        "JIRA / Issues" [:i.fa.fa-external-link]]]]])

(def cljsinfo-license-url "https://github.com/oakmac/cljs.info/blob/master/LICENSE.md")
(def clojurescript-license-url "https://github.com/clojure/clojurescript#license")

(hiccups/defhtml footer-bottom []
  [:div.bottom-31b43
    [:div.left-1764b
      [:p.small-14fbc
        "ClojureScript is released under the "
        [:a {:href clojurescript-license-url} "Eclipse Public License 1.0"]
        " and is Copyright &copy; Rich Hickey."]
      [:p.small-14fbc
        "cljs.info is released under the "
        [:a {:href cljsinfo-license-url} "MIT License"] "."]]
    [:div.right-e461e
      [:a.ftr-home-link-2c3b4 {:href (url "/")} "cljs" [:span.ftr-info-a5716 ".info"]]]
    [:div.clr-43e49]])

(hiccups/defhtml footer []
  [:div.footer-outer-5c647
    [:div.footer-inner-022c2
      (footer-docs-list)
      (footer-learn-list)
      (footer-community-list)
      (footer-contribute-list)
      [:div.clr-43e49]
      (footer-bottom)]])

;;------------------------------------------------------------------------------
;; Homepage
;;------------------------------------------------------------------------------

(hiccups/defhtml top-nav-bar []
  [:div.header-outer-a295e
    [:div.header-inner-e9e98
      [:div.left-1764b
        [:a.main-link-bb01a {:href (url "/")} "cljs" [:span.info-9c06a ".info"]]]
      [:div.right-e461e
        [:a.nav-link-890a3 {:href (url "/getting-started")} "Getting Started"]
        [:a.nav-link-890a3 {:href (url "/faq")} "FAQ"]
        [:a.nav-link-890a3 {:href (url "/docs")} "Documentation"]
        [:a.nav-link-890a3 {:href (url "/tutorials")} "Tutorials"]
        [:a.nav-link-890a3 {:href (url "/community")} "Community"]]
      [:div.clr-43e49]]])

(hiccups/defhtml jumbotron []
  [:div.title-outer-16d0f
    [:div.title-inner-df992
      [:div.jumbo-left-80c52
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
        [:a.get-started-518a6 {:href (url "/getting-started")} "Get Started"]
        [:div.btns-a0ca1
          [:a.left-btn-2f03d {:href (url "/rationale")} "Rationale"]
          [:a.right-btn-33d5b {:href (url "/docs")} "Docs"]
          [:div.clr-43e49]]
        ;; TODO: make these dynamic
        [:div.version-974cf "Latest: 0.0-2496"]
        [:div.version-974cf "Released 4 days ago"]]
      [:div.clr-43e49]]])

(def closure-compiler-url "https://developers.google.com/closure/compiler/docs/compilation_levels")

(hiccups/defhtml interop-blurb []
  [:div.blurb-c4f14.left-1764b
    [:h4.hdr-e4d24 "100% JavaScript"]
    [:p.p-382d3
      "Use any JavaScript library from ClojureScript. Write a ClojureScript "
      "library to be used by JavaScript. 100% compatibility; no exceptions."]
    ; [:a.more-link-1a354 {:href "/tutorials/interop"} "JavaScript Interop &raquo;"]
    ])

(hiccups/defhtml code-organization-blurb []
  [:div.blurb-c4f14.right-e461e
    [:h4.hdr-e4d24 "Built-in Code Organization"]
    [:p.p-382d3
      "Namespace and package management support are built directly into the "
      "ClojureScript language. No more depending on an external library and getting "
      "everyone on your team to follow the same conventions. It just works."]
    ; [:a.more-link-1a354 {:href "/tutorials/namespaces"} "Namespaces &raquo;"]
    ])

(hiccups/defhtml whole-program-optimization-blurb []
  [:div.blurb-c4f14.left-1764b
    [:h4.hdr-e4d24 "Whole Program Optimization"]
    [:p.p-382d3
      "ClojureScript uses the " [:a {:href closure-compiler-url} "Google Closure Compiler"]
      " in Advanced Optimizations mode to automatically remove dead code. "
      "Add as much code to your ClojureScript project as needed. The end "
      "result will always be as small as possible."]
    ; [:a.more-link-1a354 {:href "/tutorials/compiling"} "Compiling &raquo;"]
    ])

(hiccups/defhtml state-blurb []
  [:div.blurb-c4f14.right-e461e
    [:h4.hdr-e4d24 "Sane Approach to State"]
    [:p.p-382d3
      "Reasoning about state is simple in ClojureScript. All data is immutable by "
      "default, so you never have to worry about the value of something at one time vs "
      "another. When mutability is needed, ClojureScript distinguishes between setting "
      "and retrieving values so it's easy to keep track of what's going on in your "
      "program."]
    ; [:a.more-link-1a354 {:href "/tutorials/state"} "Atoms &raquo;"]
    ])

(hiccups/defhtml macros-blurb []
  [:div.blurb-c4f14.left-1764b
    [:h4.hdr-e4d24 "Unrivaled Syntax Power"]
    [:p.p-382d3
      "Say goodbye to boilerplate code with macros! ClojureScript is a LISP and allows "
      "for programmatic access to its syntax at the lowest level. Write powerful "
      "libraries and extend the language exactly as your program requires."]
    ; [:a.more-link-1a354 {:href "/tutorials/macros"} "Macros &raquo;"]
    ])

(hiccups/defhtml community-blurb []
  [:div.blurb-c4f14.right-e461e
    [:h4.hdr-e4d24 "Great Language, Great People"]
    [:p.p-382d3
      "The ClojureScript community is part of the broader Clojure community, a popular "
      "language that runs on the JVM. Clojure programmers are known for being fun, "
      "helpful, and very smart. We're glad you're here."]
    ; [:a.more-link-1a354 {:href "/community"} "Community &raquo;"]
    ])

(hiccups/defhtml blurbs []
  [:div.outer-5cceb
    [:div.inner-a5192
      [:div.row-4e0be
        (interop-blurb)
        (code-organization-blurb)
        [:div.clr-43e49]]

      [:div.row-4e0be
        (whole-program-optimization-blurb)
        (state-blurb)
        [:div.clr-43e49]]

      [:div.row-4e0be
        (macros-blurb)
        (community-blurb)
        [:div.clr-43e49]]]])

(hiccups/defhtml common-faqs []
  [:div.faqs-outer-dac3a
    [:div.faqs-inner-e9036
      "faqs"
      ]])

(hiccups/defhtml homepage []
  (site-head "ClojureScript - JavaScript made simple")
  (jumbotron)
  (top-nav-bar)
  (blurbs)
  ; (common-faqs)
  (footer)
  (site-footer))

;;------------------------------------------------------------------------------
;; FAQ
;;------------------------------------------------------------------------------

(def faq-renderer (marked.Renderer.))

(aset faq-renderer "heading" (fn [raw-html lvl]
  (let [id (-> raw-html
               trim
               (replace #"^%" "")
               (replace #"%.+$" ""))
        html-without-id (-> raw-html
                            (replace (str "%" id "%") "")
                            trim)]
    (str "<h" lvl " id='" id "'>"
         "<a href='#" id "'>"
         html-without-id
         "</a>"
         "</h" lvl ">"))))

;; TODO: need to throw a useful error when faq.md does not exist
(def faq-html (marked (.readFileSync fs "md/faq.md" "utf-8")
                      #js {:renderer faq-renderer}))

(hiccups/defhtml faq-page []
  (site-head "Frequently Asked Questions")
  (top-nav-bar)
  [:div.faq-outer-b72e9
    [:div.faq-inner-e70fb faq-html]]
  (footer)
  (site-footer))

;;------------------------------------------------------------------------------
;; Getting Started
;;------------------------------------------------------------------------------

(hiccups/defhtml getting-started []
  (site-head "Getting Started")
  (top-nav-bar)
  "TODO: getting started"
  (footer)
  (site-footer))

;;------------------------------------------------------------------------------
;; Doc Pages
;;------------------------------------------------------------------------------

(hiccups/defhtml docs-index []
  (site-head "Documentation")
  (top-nav-bar)
  "TODO: docs"
  (footer)
  (site-footer))

;;------------------------------------------------------------------------------
;; Rationale
;;------------------------------------------------------------------------------

(hiccups/defhtml rationale []
  (site-head "Rationale")
  (top-nav-bar)
  "TODO: rationale"
  (footer)
  (site-footer))

;;------------------------------------------------------------------------------
;; Tutorials
;;------------------------------------------------------------------------------

(hiccups/defhtml tutorials-index []
  (site-head "Tutorials")
  (top-nav-bar)
  "TODO: tutorials"
  (footer)
  (site-footer))

;;------------------------------------------------------------------------------
;; Community
;;------------------------------------------------------------------------------

(hiccups/defhtml community-page []
  (site-head "Community")
  (top-nav-bar)
  "TODO: community"
  (footer)
  (site-footer))
