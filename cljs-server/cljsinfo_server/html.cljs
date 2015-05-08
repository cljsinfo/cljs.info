(ns cljsinfo-server.html
  (:require-macros [hiccups.core :as hiccups])
  (:require
    [clojure.string :refer [capitalize replace split trim]]
    hiccups.runtime
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.util :refer [js-log log split-full-name]]))

(def fs     (js/require "fs-extra"))
(def marked (js/require "marked"))

;;------------------------------------------------------------------------------
;; URLs
;;------------------------------------------------------------------------------

(defn- url [path]
  (if-let [base-href (:base-href config)]
    (str (replace base-href #"/$" "") path)
    path))

;;------------------------------------------------------------------------------
;; Hashed Assets
;;------------------------------------------------------------------------------

(def js-assets (.readJsonSync fs "assets.json" (js-obj "throws" false)))
(def assets (if js-assets (js->clj js-assets) {}))

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
    [:title page-title " - cljs.info"]
    [:meta {:name "viewport" :content "width=1200, initial-scale=1"}]
    [:link {:rel "shortcut icon" :href "favicon.png" :type "image/png"}]
    [:link {:rel "stylesheet" :href (asset "/css/main.min.css")}]]
  "<body>")

(hiccups/defhtml site-footer
  ([]
    (site-footer nil))
  ([init-page]
    [:script {:src "/js/libs/jquery-2.1.1.min.js"}]
    [:script {:src "/js/libs/highlight-8.4.custom.min.js"}]
    ;; [:script "hljs.initHighlightingOnLoad();"]
    (if (:minified-client config)
      [:script {:src (asset "/js/client.min.js")}]
      [:script {:src (asset "/js/client.js")}])
    (if init-page
      [:script "if(window.CLJSINFO && CLJSINFO.init){CLJSINFO.init('" init-page "');}"]
      [:script "if(window.CLJSINFO && CLJSINFO.init){CLJSINFO.init();}"])
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

(hiccups/defhtml footer2 []
  [:div.ftr-outer-1f0f3
    [:div.ftr-inner-0483e
      [:div.nav-list-3a2bf
        [:h5.ftr-header-c261a "Documentation"]
        [:a {:href (url "/getting-started")} "Getting Started"]
        [:a {:href (url "/tutorials")} "Tutorials"]
        [:a {:href (url "/docs")} "Docs"]
        [:a {:href (url "/cheatsheet")} "Cheatsheet"]]
      [:div.nav-list-3a2bf
        [:h5.ftr-header-c261a "Learn"]
        [:a {:href (url "/rationale")} "Rationale"]
        [:a {:href (url "/faq")} "FAQ"]]
      [:div.nav-list-3a2bf
        [:h5.ftr-header-c261a "Community"]
        [:a {:href mailing-list-url} "Mailing List" [:i.fa.fa-external-link]]
        [:span.small-64bf7 "IRC: #clojurescript"]]
      [:div.nav-list-3a2bf
        [:h5.ftr-header-c261a "Contribute"]
        [:a {:href github-url} "GitHub" [:i.fa.fa-external-link]]
        [:a {:href issues-url} "JIRA / Issues" [:i.fa.fa-external-link]]]
      [:div.version-eb1a6
        [:p "Latest: 0.0-3196"]
        [:p "Released 4 days ago"]]
      [:div.clr-43e49]
      [:div.bottom-f931d
        [:p.small-1c732
          "ClojureScript is released under the "
          [:a {:href clojurescript-license-url} "Eclipse Public License 1.0"]
          " and is Copyright &copy; Rich Hickey."]
        [:p.small-1c732
          "cljs.info is released under the "
          [:a {:href cljsinfo-license-url} "MIT License"] "."]]]])

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

(hiccups/defhtml interop-blurb2 []
  [:h4 "100% JavaScript"]
  [:p "Use any JavaScript library from ClojureScript. Write a ClojureScript "
      "library to be used by JavaScript. 100% compatibility; no exceptions."])

(hiccups/defhtml code-organization-blurb []
  [:div.blurb-c4f14.right-e461e
    [:h4.hdr-e4d24 "Built-in Code Organization"]
    [:p.p-382d3
      "Namespace and package management support are built directly into the "
      "ClojureScript language. No more depending on an external library and getting "
      "everyone on your team to follow the same conventions. It just works."]
    ; [:a.more-link-1a354 {:href "/tutorials/namespaces"} "Namespaces &raquo;"]
    ])

(hiccups/defhtml code-organization-blurb2 []
  [:h4 "Built-in Code Organization"]
  [:p "Namespace and package management support are built directly into the "
      "ClojureScript language. No more depending on an external library and getting "
      "everyone on your team to follow the same conventions. It just works."])

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

(hiccups/defhtml whole-program-optimization-blurb2 []
  [:h4 "Whole Program Optimization"]
  [:p "ClojureScript uses the Google Closure Compiler"
      " in Advanced Optimizations mode to automatically remove dead code. "
      "Add as much code to your ClojureScript project as needed. The end "
      "result will always be as small as possible."])

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

(hiccups/defhtml state-blurb2 []
  [:h4 "Sane Approach to State"]
  [:p
    "Reasoning about state is simple in ClojureScript. All data is immutable by "
    "default, so you never have to worry about the value of something at one time vs "
    "another. When mutability is needed, ClojureScript distinguishes between setting "
    "and retrieving values so it's easy to keep track of what's going on in your "
    "program."])

(hiccups/defhtml macros-blurb []
  [:div.blurb-c4f14.left-1764b
    [:h4.hdr-e4d24 "Unrivaled Syntax Power"]
    [:p.p-382d3
      "Say goodbye to boilerplate code with macros! ClojureScript is a LISP and allows "
      "for programmatic access to its syntax at the lowest level. Write powerful "
      "libraries and extend the language exactly as your program requires."]
    ; [:a.more-link-1a354 {:href "/tutorials/macros"} "Macros &raquo;"]
    ])

(hiccups/defhtml macros-blurb2 []
  [:h4 "Unrivaled Syntax Power"]
  [:p
    "Say goodbye to boilerplate code with macros! ClojureScript is a LISP and allows "
    "for programmatic access to its syntax at the lowest level. Write powerful "
    "libraries and extend the language exactly as your program requires."])

(hiccups/defhtml community-blurb []
  [:div.blurb-c4f14.right-e461e
    [:h4.hdr-e4d24 "Great Language, Great People"]
    [:p.p-382d3
      "The ClojureScript community is part of the broader Clojure community, a popular "
      "language that runs on the JVM. Clojure programmers are known for being fun, "
      "helpful, and very smart. We're glad you're here."]
    ; [:a.more-link-1a354 {:href "/community"} "Community &raquo;"]
    ])

(hiccups/defhtml community-blurb2 []
  [:h4 "Great Language, Great People"]
  [:p
    "The ClojureScript community is part of the broader Clojure community, a popular "
    "language that runs on the JVM. Clojure programmers are known for being fun, "
    "helpful, and very smart. We're glad you're here."])

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

(hiccups/defhtml old-homepage []
  (site-head "ClojureScript - JavaScript made simple")
  (jumbotron)
  (top-nav-bar)
  (blurbs)
  ; (common-faqs)
  (footer)
  (site-footer))

(hiccups/defhtml header []
  [:header
    [:div.inner-24d98
      [:a {:href "/"} [:img.img-acd65 {:src "/img/cljs-logo.svg"}]]
      [:a.nav-link-18d62 {:href "/getting-started"} "Getting Started"]
      [:a.nav-link-18d62 {:href "/faq"} "FAQ"]
      [:a.nav-link-18d62 {:href "/docs"} "Documentation"]
      [:a.nav-link-18d62 {:href "/tutorials"} "Tutorials"]
      [:a.nav-link-18d62 {:href "/community"} "Community"]
      [:div.clr-43e49]]])

(hiccups/defhtml homepage-header []
  [:div.outer-a0feb
    [:div.inner-3064a
      [:img.logo-3ed24 {:src "img/cljs-logo.svg"}]
      [:div.nav-links-6320a
        [:a.nav-link-4db37 {:href (url "/getting-started")} "Get Started"]
        [:a.nav-link-4db37 {:href (url "/faq")} "FAQ"]
        [:a.nav-link-4db37 {:href (url "/docs")} "Documentation"]
        [:a.nav-link-4db37 {:href (url "/tutorials")} "Tutorials"]
        [:a.nav-link-4db37 {:href (url "/community")} "Community"]]]])

(hiccups/defhtml jumbotron2 []
  [:div.outer-a6683
    [:div.inner-959a0
      [:img.background-img-4ff34 {:src (url "/img/cheatsheet-background.png")}]
      [:div.left-958bf
        [:h1.big-title-c3dbd "ClojureScript"]
        [:p.blurb-ec58e
          "ClojureScript is a functional programming language that targets "
          "JavaScript. It comes with a rich set of data types, an extensive "
          "core library, and a novel approach to state."]
        [:p.blurb-ec58e
          "Programming for the browser" #_[:i.fa.fa-asterisk] " will never be "
          "the same."]]
      [:div.right-1b23f
        [:h2.tagline-aae79 "JavaScript made simple."]
        [:a.big-cta-7f0a0 {:href (url "/getting-started")} "Get Started"]
        [:br]
        [:a.secondary-btn-2b577 {:href (url "/rationale")} "Rationale"]
        [:a.secondary-btn-2b577 {:href (url "/docs")} "Docs"]]]])

;; I hope the Internet doesn't skewer me for using a table for layout here...
(hiccups/defhtml blurbs2 []
  [:div.outer-799fb
    [:div.inner-2b584
      [:table.blurbs-tbl-2ebec
        [:tbody
          [:tr
            [:td.cell-b7906.bottom-right-c760b (interop-blurb2)]
            [:td.cell-b7906.bottom-right-c760b (code-organization-blurb2)]
            [:td.right-cell-66dc9 (whole-program-optimization-blurb2)]]
          [:tr
            [:td.bottom-cell-280bb (state-blurb2)]
            [:td.bottom-cell-280bb.top-left-9deaa (macros-blurb2)]
            [:td.top-left-9deaa (community-blurb2)]]]]]])

(hiccups/defhtml homepage []
  (site-head "ClojureScript - JavaScript made simple")
  (homepage-header)
  (jumbotron2)
  (blurbs2)
  (footer2)
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
    (str "<h" lvl " class='question-4800e' id='" id "'>"
         "<a class='question-link-6d31a' href='#" id "'>"
         "<i class='fa fa-link faq-link-cdef1'></i>"
         html-without-id
         "</a>"
         "</h" lvl ">"))))

;; TODO: throw a useful error when faq.md does not exist
(def faq-html (marked (.readFileSync fs "md/faq.md" "utf-8")
                      #js {:renderer faq-renderer}))

(hiccups/defhtml faq-page []
  (site-head "Frequently Asked Questions")
  (top-nav-bar)
  [:div.body-outer-b72e9
    [:div.body-inner-e70fb faq-html]]
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

;; TODO: throw a useful error when rationale.md does not exist
(def rationale-html (marked (.readFileSync fs "md/rationale.md" "utf-8")))

(hiccups/defhtml rationale []
  (site-head "Rationale")
  (top-nav-bar)
  [:div.body-outer-b72e9
    [:div.body-inner-e70fb rationale-html]]
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

;;------------------------------------------------------------------------------
;; 404
;;------------------------------------------------------------------------------

(hiccups/defhtml not-found []
  [:h1 "Page not found."]
  [:p "TODO: make this page more useful"])

;;------------------------------------------------------------------------------
;; Doc Page
;;------------------------------------------------------------------------------

(def cljs-core-ns "cljs.core")

(defn- determine-arity [s]
  (if (not= (.indexOf s "&") -1)
    "V"
    (count (split s " "))))

(defn- build-signature-str [symbol-str s]
  (str "(" symbol-str " "
       (-> s
           (replace "[" "")
           (replace "]" ""))
       ")"))

(hiccups/defhtml signature-line [symbol-str idx s]
  [:div {:class (str "signature-line-b147a " (if (even? idx) "even-3c2da"
                                                             "odd-d1a05"))}
    (build-signature-str symbol-str s)])

(hiccups/defhtml signature [symbol-str sig]
  [:div.signature-7b710
    (map-indexed (partial signature-line symbol-str) sig)])

(hiccups/defhtml file-link [docs]
  (let [github-link (get docs "github")
        filename (get docs "filename")]
    [:a {:href (replace github-link #"#.+$" "")}
      (replace filename #"^clojurescript/src/" "")]))

(hiccups/defhtml format-lines-link [source-link]
  [:a {:href source-link}
    (-> source-link
        (replace #"^.+#" "")
        (replace "L" "")
        (replace "-" " - "))])

(defn- format-filename [filename]
  (replace filename #"^clojurescript/" ""))

(hiccups/defhtml docs-info-table [docs]
  [:table [:tbody
    [:tr
      [:td.label-8bc3b "Namespace"]
      [:td [:code (:ns docs)]]]
    [:tr
      [:td.label-8bc3b "Symbol"]
      [:td [:code (:name docs)]]]
    [:tr
      [:td.label-8bc3b "Type"]
      [:td (-> docs :type str capitalize)]]
    (when (:return-type docs)
      [:tr
        [:td.label-8bc3b "Return Type"]
        [:td (-> docs :return-type str capitalize)]])
    [:tr
      [:td.label-8bc3b "File"]
      [:td (-> docs :source-filename format-filename)]]
    [:tr
      [:td.label-8bc3b "Lines"]
      [:td (-> docs :source-link format-lines-link)]]]])

(hiccups/defhtml docs-source [docs]
  [:h2.section-99c9a "Source"
    [:a.source-link-352de
      {:href (:source-link docs)}
      "view on GitHub"]]
  [:pre
    [:code {:class "lang-clj"} (:source docs)]])

(hiccups/defhtml single-example [ex]
  (let [id (str "example-" (:id ex))]
    (list
      [:a {:href (str "#" id)} "[link]"]
      [:div.example-wrapper-7ea9f {:id id}
        (marked (:content ex))])))

(hiccups/defhtml examples [exs]
  [:h2.section-99c9a "Examples"]
  (map single-example exs))

(hiccups/defhtml doc-page-title [namespace-str symbol-str]
  [:h1.doc-title-6036e
    (when-not (= namespace-str cljs-core-ns)
      [:span.namespace-ca326 (str namespace-str "/")])
    symbol-str])

(defn doc-page [docs]
  (let [full-name (:full-name docs)
        namespace-str (:ns docs)
        symbol-str (:name docs)]
    (hiccups/html
      (site-head full-name)
      (header)
      [:div.blue-bar-3d910]
      [:div.body-outer-6bef5
        [:div.body-inner-9185d
          [:div.inner-left-0193f
            (doc-page-title namespace-str symbol-str)
            (signature symbol-str (:signature docs))
            [:div.description-71ed4 (marked (:description docs))]]
          [:div.inner-right-f3567 (docs-info-table docs)]
          [:div.clr-43e49]
          (when-let [exs (:examples docs)] (examples exs))
          (docs-source docs)]]
      (footer2)
      (site-footer "docs"))))
