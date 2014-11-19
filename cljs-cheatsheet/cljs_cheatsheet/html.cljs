(ns cljs-cheatsheet.html
  (:require-macros [hiccups.core :as hiccups])
  (:require
    hiccups.runtime
    [clojure.string :refer [replace]]
    [cljs-cheatsheet.util :refer [js-log log]]))

(def html-encode js/goog.string.htmlEscape)
(def uri-encode js/encodeURIComponent)

(def clj-string "clojure.string")
(def clj-set "clojure.set")

;;------------------------------------------------------------------------------
;; Helpers
;;------------------------------------------------------------------------------

(hiccups/defhtml tt-icon
  ([tt-id] (tt-icon tt-id nil))
  ([tt-id style]
    [:span.tooltip-link-0e91b
      {:data-tooltip-id tt-id
       :style (if style style)}
      "&#xf05a;"])) ;; NOTE: this is FontAwesome's "fa-info-circle"

(hiccups/defhtml literal [n]
  [:span.literal-c3029 n])

(defn- nme->cljdocs-url [nme]
  (-> nme
    (replace "?" "_q")
    uri-encode))

(defn- docs-href [nme nme-space]
  (str "http://clojuredocs.org/"
       (uri-encode nme-space) "/"
       (nme->cljdocs-url nme)))

(hiccups/defhtml fn-link
  ([nme] (fn-link nme "clojure.core"))
  ([nme nme-space]
    [:a.fn-a8476
      {:href (docs-href nme nme-space)}
      (html-encode nme)]))

(hiccups/defhtml inside-fn-link
  ([nme] (inside-fn-link nme "clojure.core"))
  ([nme nme-space]
    [:a.inside-fn-c7607
      {:href (docs-href nme nme-space)}
      (html-encode nme)]))

;;------------------------------------------------------------------------------
;; Sections
;;------------------------------------------------------------------------------

(hiccups/defhtml basics-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Basics"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Define" (tt-icon "define" "margin: 0;")]
          [:td.body-885f4
            (fn-link "def")
            (fn-link "defn")
            (fn-link "defn-")
            (fn-link "let")
            (fn-link "declare")
            (fn-link "ns")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Branch" (tt-icon "branch" "margin: 0; padding-right: 0;")]
          [:td.body-885f4
            (fn-link "if")
            (fn-link "if-not")
            (fn-link "when")
            (fn-link "when-not")
            (fn-link "when-let")
            (fn-link "when-first")
            (fn-link "if-let")
            (fn-link "cond")
            (fn-link "condp")
            (fn-link "case")
            (fn-link "when-some")
            (fn-link "if-some")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Compare"]
          [:td.body-885f4
            (fn-link "and" )
            (fn-link "or")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Loop"]
          [:td.body-885f4
            (fn-link "map")
            (fn-link "map-indexed")
            (fn-link "reduce")
            (fn-link "for")
            (fn-link "doseq")
            (fn-link "dotimes")
            (fn-link "while")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (fn-link "true?")
            (fn-link "false?")
            (fn-link "instance?")
            (fn-link "nil?")
            (fn-link "some?")]]]]])

(hiccups/defhtml functions-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "#( ) Functions" (tt-icon "functions")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            [:div.row-5dec8 "#(...) &rarr; (fn [args] (...))"
              (tt-icon "function-shorthand")]
            (fn-link "fn")
            (fn-link "defn")
            (fn-link "defn-")
            (fn-link "definline")
            (fn-link "identity")
            (fn-link "constantly")
            (fn-link "memfn")
            (fn-link "comp")
            (fn-link "complement")
            (fn-link "partial")
            (fn-link "juxt")
            (fn-link "memoize")
            (fn-link "fnil")
            (fn-link "every-pred")
            (fn-link "some-fn")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Call"]
          [:td.body-885f4
            (fn-link "apply")
            (fn-link "->")
            (fn-link "->>")
            (fn-link "as->")
            (fn-link "cond->")
            (fn-link "cond->>")
            (fn-link "some->")
            (fn-link "some->>")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (fn-link "fn?")
            (fn-link "ifn?")]]]]])

(hiccups/defhtml numbers-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Numbers" (tt-icon "numbers")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Literals"]
          [:td.body-885f4
            (literal "7")
            (literal "3.14")
            (literal "-1.2e3")
            (literal "0x0000ff")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Arithmetic"]
          [:td.body-885f4
            (fn-link "+")
            (fn-link "-")
            (fn-link "*")
            (fn-link "/")
            (fn-link "quot")
            (fn-link "rem")
            (fn-link "mod")
            (fn-link "inc")
            (fn-link "dec")
            (fn-link "max")
            (fn-link "min")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Compare"]
          [:td.body-885f4
            (fn-link "=")
            (fn-link "==")
            (fn-link "not=")
            (fn-link "<")
            (fn-link ">")
            (fn-link "<=")
            (fn-link ">=")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Cast"]
          [:td.body-885f4
            (fn-link "int")
            (fn-link "float")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (fn-link "zero?")
            (fn-link "pos?")
            (fn-link "neg?")
            (fn-link "even?")
            (fn-link "odd?")
            (fn-link "number?")
            (fn-link "integer?")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Random"]
          [:td.body-885f4
            (fn-link "rand")
            (fn-link "rand-int")]]]]])

(hiccups/defhtml strings-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "\" \" Strings" (tt-icon "strings")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            (literal "\"abc\"")
            (fn-link "str")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Use"]
          [:td.body-885f4
            (literal "(.-length my-str)")
            (fn-link "count")
            (fn-link "get")
            (fn-link "subs")
            (literal "(clojure.string/)")
            (fn-link "join" clj-string)
            (fn-link "escape" clj-string)
            (fn-link "split" clj-string)
            (fn-link "split-lines" clj-string)
            (fn-link "replace" clj-string)
            (fn-link "replace-first" clj-string)
            (fn-link "reverse" clj-string)]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Regex"]
          [:td.body-885f4
            [:span.literal-c3029 "#\"" [:span {:style "font-style:italic"} "pattern"] "\""]
            (fn-link "re-find")
            (fn-link "re-seq")
            (fn-link "re-matches")
            (fn-link "re-pattern")
            (literal "(clojure.string/)")
            (fn-link "replace" clj-string)
            (fn-link "replace-first" clj-string)]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Letters"]
          [:td.body-885f4
            (literal "(clojure.string/)")
            (fn-link "capitalize" clj-string)
            (fn-link "lower-case" clj-string)
            (fn-link "upper-case" clj-string)]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Trim"]
          [:td.body-885f4
            (literal "(clojure.string/)")
            (fn-link "trim" clj-string)
            (fn-link "trim-newline" clj-string)
            (fn-link "triml" clj-string)
            (fn-link "trimr" clj-string)]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (fn-link "char")
            (fn-link "string?")
            (literal "(clojure.string/)")
            (fn-link "blank?" clj-string)]]]]])

(hiccups/defhtml atoms-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Atoms / State" (tt-icon "atoms")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            (fn-link "atom")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Get Value"]
          [:td.body-885f4
            [:span.literal-c3029 "@my-atom &rarr; (" (inside-fn-link "deref") " my-atom)"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Set Value"]
          [:td.body-885f4
            (fn-link "swap!")
            (fn-link "reset!")
            (fn-link "compare-and-set!")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Watch"]
          [:td.body-885f4
            (fn-link "add-watch")
            (fn-link "remove-watch")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Validators"]
          [:td.body-885f4
            (fn-link "set-validator!")
            (fn-link "get-validator")]]]]])

(hiccups/defhtml js-interop-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "JavaScript Interop"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create Object"]
          [:td.body-885f4
            (literal "#js {}")
            (fn-link "js-obj")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Create Array"]
          [:td.body-885f4
            (literal "#js []")
            (fn-link "array")
            (fn-link "make-array")
            (fn-link "aclone")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Get Property"]
          [:td.body-885f4
            [:div.row-5dec8 "(.-innerHTML el)"]
            [:div.row-5dec8 "(" (inside-fn-link "aget") " el \"innerHTML\")"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Set Property"]
          [:td.body-885f4
            [:div.row-5dec8 "(" (inside-fn-link "set!") " (.-innerHTML el) \"Hi!\")"]
            [:div.row-5dec8 "(" (inside-fn-link "aset") " el \"innerHTML\" \"Hi!\")"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Convert Between"]
          [:td.body-885f4
            (fn-link "clj->js")
            (fn-link "js->clj")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Type Tests"]
          [:td.body-885f4
            (fn-link "array?")
            (fn-link "fn?")
            (fn-link "number?")
            (fn-link "object?")
            (fn-link "string?")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Exceptions"]
          [:td.body-885f4
            (fn-link "try")
            (fn-link "catch")
            (fn-link "finally")
            (fn-link "throw")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "External Library"]
          [:td.body-885f4
            [:div.row-5dec8 "(js/alert \"Hello world!\")"]
            [:div.row-5dec8 "(js/console.log my-obj)"]
            [:div.row-5dec8 "(.html (js/jQuery \"#myDiv\") \"Hi!\")"]]]]]])

(hiccups/defhtml collections-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Collections" (tt-icon "collections")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "General"]
          [:td.body-885f4
            (fn-link "count")
            (fn-link "empty")
            (fn-link "not-empty")
            (fn-link "into")
            (fn-link "conj")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Content Tests"]
          [:td.body-885f4
            (fn-link "distinct?")
            (fn-link "empty?")
            (fn-link "every?")
            (fn-link "not-every?")
            (fn-link "some")
            (fn-link "not-any?")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Capabilities"]
          [:td.body-885f4
            (fn-link "sequential?")
            (fn-link "associative?")
            (fn-link "sorted?")
            (fn-link "counted?")
            (fn-link "reversible?")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Type Tests"]
          [:td.body-885f4
            (fn-link "coll?")
            (fn-link "list?")
            (fn-link "vector?")
            (fn-link "set?")
            (fn-link "map?")
            (fn-link "seq?")]]]]])

(hiccups/defhtml lists-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "( ) Lists" (tt-icon "lists")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            (literal "'()")
            (fn-link "list")
            (fn-link "list*")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Examine"]
          [:td.body-885f4
            (fn-link "first")
            (fn-link "nth")
            (fn-link "peek")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            (fn-link "cons")
            (fn-link "conj")
            (fn-link "rest")
            (fn-link "pop")]]]]])

(hiccups/defhtml vectors-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "[ ] Vectors" (tt-icon "vectors")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            (literal "[]")
            (fn-link "vector")
            (fn-link "vec")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Examine"]
          [:td.body-885f4
            [:div.row-5dec8
              "(my-vec idx) &rarr; (" (inside-fn-link "nth") " my-vec idx)"
              (tt-icon "vector-as-fn")]
            (fn-link "get")
            (fn-link "peek")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            (fn-link "assoc")
            (fn-link "pop")
            (fn-link "subvec")
            (fn-link "replace")
            (fn-link "conj")
            (fn-link "rseq")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Loop"]
          [:td.body-885f4
            (fn-link "mapv")
            (fn-link "filterv")
            (fn-link "reduce-kv")]]]]])

(hiccups/defhtml sets-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "#{ } Sets" (tt-icon "sets")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            (literal "#{}")
            (fn-link "set")
            (fn-link "hash-set")
            (fn-link "sorted-set")
            (fn-link "sorted-set-by")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Examine"]
          [:td.body-885f4
            [:div.row-5dec8
              "(my-set itm) &rarr; (" (inside-fn-link "get") " my-set itm)"
              (tt-icon "set-as-fn")]
            (fn-link "contains?")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            (fn-link "conj")
            (fn-link "disj")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Set Ops"]
          [:td.body-885f4
            (literal "(clojure.set/)")
            (fn-link "union" clj-set)
            (fn-link "difference" clj-set)
            (fn-link "intersection" clj-set)
            (fn-link "select" clj-set)]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (literal "(clojure.set/)")
            (fn-link "subset?" clj-set)
            (fn-link "superset?" clj-set)]]]]])

(hiccups/defhtml maps-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "{ } Maps" (tt-icon "maps")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            [:div.row-5dec8 "{:key1 \"a\" :key2 \"b\"}"]
            (fn-link "hash-map")
            (fn-link "array-map")
            (fn-link "zipmap")
            (fn-link "sorted-map")
            (fn-link "sorted-map-by")
            (fn-link "frequencies")
            (fn-link "group-by")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Examine"]
          [:td.body-885f4
            [:div.row-5dec8
              "(:key my-map) &rarr; (" (inside-fn-link "get") " my-map :key)"
              (tt-icon "keywords-as-fn")]
            (fn-link "get-in")
            (fn-link "contains?")
            (fn-link "find")
            (fn-link "keys")
            (fn-link "vals")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            (fn-link "assoc")
            (fn-link "assoc-in")
            (fn-link "dissoc")
            (fn-link "merge")
            (fn-link "merge-with")
            (fn-link "select-keys")
            (fn-link "update-in")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Entry"]
          [:td.body-885f4
            (fn-link "key")
            (fn-link "val")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Sorted Maps"]
          [:td.body-885f4
            (fn-link "rseq")
            (fn-link "subseq")
            (fn-link "rsubseq")]]]]])

(hiccups/defhtml create-seq-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Create a Seq"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "From Collection"]
          [:td.body-885f4
            (fn-link "seq")
            (fn-link "vals")
            (fn-link "keys")
            (fn-link "rseq")
            (fn-link "subseq")
            (fn-link "rsubseq")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Producer Functions"]
          [:td.body-885f4
            (fn-link "lazy-seq")
            (fn-link "repeatedly")
            (fn-link "iterate")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "From Constant"]
          [:td.body-885f4
            (fn-link "repeat")
            (fn-link "range")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "From Other"]
          [:td.body-885f4
            (fn-link "re-seq")
            (fn-link "tree-seq")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "From Sequence"]
          [:td.body-885f4
            (fn-link "keep")
            (fn-link "keep-indexed")]]]]])

(hiccups/defhtml seq-in-out-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Seq in, Seq out" (tt-icon "sequences")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Get Shorter"]
          [:td.body-885f4
            (fn-link "distinct")
            (fn-link "filter")
            (fn-link "remove")
            (fn-link "take-nth")
            (fn-link "for")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Get Longer"]
          [:td.body-885f4
            (fn-link "cons")
            (fn-link "conj")
            (fn-link "concat")
            (fn-link "lazy-cat")
            (fn-link "mapcat")
            (fn-link "cycle")
            (fn-link "interleave")
            (fn-link "interpose")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Get From Tail"]
          [:td.body-885f4
            (fn-link "rest")
            (fn-link "nthrest")
            (fn-link "next")
            (fn-link "fnext")
            (fn-link "nnext")
            (fn-link "drop")
            (fn-link "drop-while")
            (fn-link "take-last")
            (fn-link "for")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Get From Head"]
          [:td.body-885f4
            (fn-link "take")
            (fn-link "take-while")
            (fn-link "butlast")
            (fn-link "drop-last")
            (fn-link "for")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            (fn-link "conj")
            (fn-link "concat")
            (fn-link "distinct")
            (fn-link "flatten")
            (fn-link "group-by")
            (fn-link "partition")
            (fn-link "partition-all")
            (fn-link "partition-by")
            (fn-link "split-at")
            (fn-link "split-with")
            (fn-link "filter")
            (fn-link "remove")
            (fn-link "replace")
            (fn-link "shuffle")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Rearrange"]
          [:td.body-885f4
            (fn-link "reverse")
            (fn-link "sort")
            (fn-link "sort-by")
            (fn-link "compare")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Process Items"]
          [:td.body-885f4
            (fn-link "map")
            (fn-link "pmap")
            (fn-link "map-indexed")
            (fn-link "mapcat")
            (fn-link "for")
            (fn-link "replace")
            (fn-link "seque")]]]]])

(hiccups/defhtml use-seq-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Using a Seq"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Extract Item"]
          [:td.body-885f4
            (fn-link "first")
            (fn-link "second")
            (fn-link "last")
            (fn-link "rest")
            (fn-link "next")
            (fn-link "ffirst")
            (fn-link "nfirst")
            (fn-link "fnext")
            (fn-link "nnext")
            (fn-link "nth")
            (fn-link "nthnext")
            (fn-link "rand-nth")
            (fn-link "when-first")
            (fn-link "max-key")
            (fn-link "min-key")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Construct Collection"]
          [:td.body-885f4
            (fn-link "zipmap")
            (fn-link "into")
            (fn-link "reduce")
            (fn-link "reductions")
            (fn-link "set")
            (fn-link "vec")
            (fn-link "into-array")
            (fn-link "to-array-2d")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Pass to Function"]
          [:td.body-885f4
            (fn-link "apply")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Search"]
          [:td.body-885f4
            (fn-link "some")
            (fn-link "filter")]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Force Evaluation"]
          [:td.body-885f4
            (fn-link "doseq")
            (fn-link "dorun")
            (fn-link "doall")]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Check For Forced"]
          [:td.body-885f4
            (fn-link "realized?")]]]]])

(hiccups/defhtml bitwise-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Bitwise"]
    [:div.solo-section-d5309
      (fn-link "bit-and")
      (fn-link "bit-or")
      (fn-link "bit-xor")
      (fn-link "bit-not")
      (fn-link "bit-flip")
      (fn-link "bit-set")
      (fn-link "bit-shift-right")
      (fn-link "bit-shift-left")
      (fn-link "bit-and-not")
      (fn-link "bit-clear")
      (fn-link "bit-test")
      (fn-link "unsigned-bit-shift-right")]])

;; TODO: create "Export to JavaScript" section
;; include ^:export and goog.exportSymbol functions
;; and a sentence about how it works

;;------------------------------------------------------------------------------
;; Info Tooltips
;;------------------------------------------------------------------------------

(hiccups/defhtml basics-tooltips []

  [:div#tooltip-define.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Everything in ClojureScript is immutable by default, meaning that the "
      "value of a symbol cannot be changed after it is defined."]]

  [:div#tooltip-branch.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "In conditional statements, everything evaluates to " [:code "true"]
      " except for " [:code "false"] " and " [:code "nil"] "."]
    [:p.info-2e4f9
      "This is much simpler than JavaScript, which has complex rules for "
      "truthiness."]
    [:table.tbl-3160a
      [:thead
        [:tr
          [:th.tbl-hdr-e0564 "Name"]
          [:th.tbl-hdr-e0564 "Code"]
          [:th.tbl-hdr-e0564 "Boolean Value"]]]
      [:tbody
        [:tr.dark-odd-7aff7
          [:td.cell-e6fd2.right-border-c1b54 "Empty string"]
          [:td.cell-e6fd2.right-border-c1b54 [:code "\"\""]]
          [:td.cell-e6fd2 [:code "true"]]]
        [:tr.dark-even-6cd97
          [:td.cell-e6fd2.right-border-c1b54 "Zero"]
          [:td.cell-e6fd2.right-border-c1b54 [:code "0"]]
          [:td.cell-e6fd2 [:code "true"]]]
        [:tr.dark-odd-7aff7
          [:td.cell-e6fd2.right-border-c1b54 "Not a number"]
          [:td.cell-e6fd2.right-border-c1b54 [:code "js/NaN"]]
          [:td.cell-e6fd2 [:code "true"]]]
        [:tr.dark-even-6cd97
          [:td.cell-e6fd2.right-border-c1b54 "Empty vector"]
          [:td.cell-e6fd2.right-border-c1b54 [:code "[]"]]
          [:td.cell-e6fd2 [:code "true"]]]
        [:tr.dark-odd-7aff7
          [:td.cell-e6fd2.right-border-c1b54 "Empty array"]
          [:td.cell-e6fd2.right-border-c1b54 [:code "(array)"]]
          [:td.cell-e6fd2 [:code "true"]]]
        [:tr.dark-even-6cd97
          [:td.cell-e6fd2.right-border-c1b54 "False"]
          [:td.cell-e6fd2.right-border-c1b54 [:code "false"]]
          [:td.cell-e6fd2 [:code "false"]]]
        [:tr.dark-odd-7aff7
          [:td.cell-e6fd2.right-border-c1b54 "Nil"]
          [:td.cell-e6fd2.right-border-c1b54 [:code "nil"]]
          [:td.cell-e6fd2 [:code "false"]]]]]]

  [:div#tooltip-numbers.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "All ClojureScript Numbers are IEEE 754 Double Precision floating point. "
      "The same as JavaScript."]]

  [:div#tooltip-atoms.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Atoms provide a way to manage state in a ClojureScript program."]
    [:p.info-2e4f9
      "Unlike JavaScript, everything in ClojureScript is immutable by default. "
      "This means that you cannot change the value of something after it has "
      "been defined."]
    [:p.info-2e4f9
      "Atoms allow for mutability and distinguish between setting and reading "
      "a value, which makes state easier to reason about."]
    [:p.info-2e4f9
      "Watcher functions execute when a value changes, providing a powerful UI "
      "pattern when your value maps to interface state."]]

  [:div#tooltip-functions.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "ClojureScript Functions are JavaScript Functions and can be called and "
      "used in all the ways that JavaScript Functions can."]
    [:p.info-2e4f9
      "The core library provides many useful higher-order functions and there "
      "is a convenient shorthand for creating anonymous functions."]]

  [:div#tooltip-function-shorthand.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "The " [:code "#()"] " function shorthand is a convenient way to write a "
      "small function definition and is often used to pass closures from one "
      "scope to another."]
    [:p.info-2e4f9
      [:code "#()"] " forms cannot be nested and it is idiomatic to keep them short."]
    [:table.exmpl-tbl-42d9f
      [:thead
        [:tr
          [:th.tbl-hdr-e0564 "Shorthand"]
          [:th.tbl-hdr-e0564 "Expands To"]]]
      [:tbody
        [:tr.dark-odd-7aff7
          [:td.code-72fa0.right-border-c1b54 "#(str \"Hello \" %)"]
          [:td.code-72fa0 [:pre "(fn [n]\n  (str \"Hello \" n))"]]]
        [:tr.dark-even-6cd97
          [:td.code-72fa0.right-border-c1b54 "#(my-fn %1 %2 %3)"]
          [:td.code-72fa0 [:pre "(fn [a b c]\n  (my-fn a b c))"]]]
        [:tr.dark-odd-7aff7
          [:td.code-72fa0.right-border-c1b54 "#(* % (apply + %&amp;))"]
          [:td.code-72fa0 [:pre {:style "font-size:10px"}
            "(fn [x &amp; the-rest]\n"
            "  (* x (apply + the-rest)))"]]]]]]

  [:div#tooltip-strings.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "ClojureScript Strings are JavaScript Strings and have all of the native "
      "methods and properties that a JavaScript String has."]
    [:p.info-2e4f9
      "ClojureScript Strings must be defined using double quotes."]
    [:p.info-2e4f9
      "The " [:code "clojure.string"] " namespace provides many useful "
      "functions for dealing with strings."]])

(hiccups/defhtml collections-tooltips []
  [:div#tooltip-collections.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "ClojureScript provides four collection types: lists, vectors, sets, and "
      "maps. "
      "Each of these data types has unique strengths and are used heavily in "
      "most programs."]
    [:p.info-2e4f9
      "All collections are immutable and persistent, which means they preserve "
      "the previous version(s) of themselves when they are modified. "
      "Creating a \"changed\" version of any collection is an efficient "
      "operation."]
    [:p.info-2e4f9
      "Collections can be represented literally:"]
    [:table.tbl-3160a
      [:thead
        [:tr
          [:th.tbl-hdr-e0564 "Collection"]
          [:th.tbl-hdr-e0564 "Literal Form"]]]
      [:tbody
        [:tr.dark-odd-7aff7
          [:td.cell-e6fd2.right-border-c1b54 "List"]
          [:td.cell-e6fd2 [:code "()"]]]
        [:tr.dark-even-6cd97
          [:td.cell-e6fd2.right-border-c1b54 "Vector"]
          [:td.cell-e6fd2 [:code "[]"]]]
        [:tr.dark-odd-7aff7
          [:td.cell-e6fd2.right-border-c1b54 "Set"]
          [:td.cell-e6fd2 [:code "#{}"]]]
        [:tr.dark-even-6cd97
          [:td.cell-e6fd2.right-border-c1b54 "Map"]
          [:td.cell-e6fd2 [:code "{}"]]]]]]

  [:div#tooltip-lists.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Lists are a sequence of values, similar to a vector."]
    [:p.info-2e4f9
      "Most literal lists represent a function call."]
    [:p.info-2e4f9
      [:code "(a b c)"] " is a list of three things, and it also means "
      "\"call the function " [:em "a"] " with two arguments: " [:em "b"]
      " and " [:em "c"] "\""]]

  [:div#tooltip-vectors.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Vectors are collections of values that are indexed by sequential integers."]
    [:p.info-2e4f9
      "Though similar, a JavaScript Array is not the same thing as a "
      "ClojureScript vector. "
      "ie: " [:code "(.indexOf my-vec)"] " will not work on a vector."]]

  [:div#tooltip-vector-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "A vector can be used as a function to access its elements."]]

  [:div#tooltip-sets.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9 "Sets are collections of unique values."]]

  [:div#tooltip-set-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "A set can be used as a function to access its elements."]]

  [:div#tooltip-maps.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "A map is a collection that maps keys to values. "
      "Accessing a value in a map using a key is very fast."]
    [:p.info-2e4f9
      "In JavaScript, Objects are commonly used as a de facto map using "
      "strings as keys. "
      "A key in a ClojureScript map can be any value, although keywords are "
      "commonly used."]]

  [:div#tooltip-keywords-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Keywords can be used as a function to get a value from a map. "
      "They are commonly used as map keys for this reason."]])

(hiccups/defhtml sequences-tooltips []
  [:div#tooltip-sequences.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Many core algorithms are defined in terms of sequences. A sequence is "
      "an interface to a list structure that allows for algorithms to be "
      "written in a generic way."]
    [:p.info-2e4f9
      "Every sequence is a collection, and every collection can be converted "
      "into a sequence using the " [:code "seq"] " function. In fact, this is "
      "what happens internally when a collection is passed to a sequence "
      "function."]
    [:p.info-2e4f9
      "Most of the sequence functions are lazy, which means that they consume "
      "their elements incrementally as needed. For example, it is possible to "
      "have an infinite sequence."]
    [:p.info-2e4f9
      "You can force a sequence to evaluate all its elements with the "
      [:code "doall"] " function. This is useful when you want to see the "
      "results of a side-effecting function over an entire sequence."]])

(hiccups/defhtml info-tooltips []
  (basics-tooltips)
  (collections-tooltips)
  (sequences-tooltips))

;;------------------------------------------------------------------------------
;; Header and Footer
;;------------------------------------------------------------------------------

(hiccups/defhtml header []
  [:div.header-2a8a6
    [:img.logo-6ced3 {:src "/img/clojure-logo.png" :alt "Clojure Logo"}]
    [:h1.title-7a29c "ClojureScript Cheatsheet"]
    [:input#searchInput.search-70fb8 {:type "text" :placeholder "Search"}]
    ; [:label#toggleTooltips.tooltips-label-68aa0
    ;   [:i.fa.fa-check-square-o] "Show tooltips?"]
    [:div.clr-43e49]])

;; TODO: text for footer:
;; "Please copy, improve, and share this work."
(hiccups/defhtml footer []
  [:div.footer-2137e {:style "display:none"} "TODO: write me"])

;;------------------------------------------------------------------------------
;; Section Layouts
;;------------------------------------------------------------------------------

(hiccups/defhtml three-col-layout []
  [:div.lrg-wrapper-cc101

    [:div.group-2be36
      [:h2.group-title-68f3c "Basics"]
      [:div.lrg-col-left-d5f6d
        (basics-section)
        (functions-section)]
      [:div.lrg-col-cntr-bb1aa
        (numbers-section)
        (strings-section)]
      [:div.lrg-col-right-0f4a3
        (atoms-section)
        (js-interop-section)]]

    [:div.group-2be36
      [:h2.group-title-68f3c "Collections"]
      [:div.lrg-col-left-d5f6d
        (collections-section)
        (lists-section)]
      [:div.lrg-col-cntr-bb1aa
        (vectors-section)
        (sets-section)]
      [:div.lrg-col-right-0f4a3
        (maps-section)]]

    [:div.group-2be36
      [:h2.group-title-68f3c "Sequences"]
      [:div.lrg-col-left-d5f6d (seq-in-out-section)]
      [:div.lrg-col-cntr-bb1aa (use-seq-section)]
      [:div.lrg-col-right-0f4a3 (create-seq-section)]]

    [:div.group-2be36
      [:h2.group-title-68f3c "Misc"]
      [:div.lrg-col-left-d5f6d
        (bitwise-section)]]

    [:div.clr-43e49]])

(hiccups/defhtml two-col-layout []
  [:div.med-wrapper-87a24

    [:div.group-2be36
      [:h2.group-title-68f3c "Basics"]
      [:div.med-col-left-06d5d
        (basics-section)
        (numbers-section)
        (js-interop-section)]
      [:div.med-col-right-eb5c2
        (functions-section)
        (strings-section)
        (atoms-section)]]

    [:div.group-2be36
      [:h2.group-title-68f3c "Collections"]
      [:div.med-col-left-06d5d
        (collections-section)
        (lists-section)
        (maps-section)]
      [:div.med-col-right-eb5c2
        (vectors-section)
        (sets-section)]]

    [:div.group-2be36
      [:h2.group-title-68f3c "Sequences"]
      [:div.med-col-left-06d5d
        (seq-in-out-section)]
      [:div.med-col-right-eb5c2
        (use-seq-section)
        (create-seq-section)]]

    [:div.group-2be36
      [:h2.group-title-68f3c "Misc"]
      [:div.med-col-left-06d5d
        (bitwise-section)]]

    [:div.clr-43e49]])

(hiccups/defhtml one-col-layout []
  [:div.sml-wrapper-7eff3

    [:div.group-2be36
      [:h2.group-title-68f3c "Basics"]
      (basics-section)
      (functions-section)
      (numbers-section)
      (strings-section)
      (js-interop-section)
      (atoms-section)]

    [:div.group-2be36
      [:h2.group-title-68f3c "Collections"]
      (collections-section)
      (lists-section)
      (vectors-section)
      (sets-section)
      (maps-section)]

    [:div.group-2be36
      [:h2.group-title-68f3c "Sequences"]
      (seq-in-out-section)
      (use-seq-section)
      (create-seq-section)]

    [:div.group-2be36
      [:h2.group-title-68f3c "Misc"]
      (bitwise-section)]

    [:div.clr-43e49]])

;;------------------------------------------------------------------------------
;; Body
;;------------------------------------------------------------------------------

(hiccups/defhtml body []
  (header)
  (three-col-layout)
  (two-col-layout)
  (one-col-layout)
  (footer)
  (info-tooltips))