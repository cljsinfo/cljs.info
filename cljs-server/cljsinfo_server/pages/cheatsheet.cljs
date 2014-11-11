(ns cljsinfo-server.pages.cheatsheet
  (:require-macros [hiccups.core :as hiccups])
  (:require
    hiccups.runtime
    [cljsinfo-server.util :refer [js-log log]]))

;;------------------------------------------------------------------------------
;; Helpers
;;------------------------------------------------------------------------------

(hiccups/defhtml tt-icon [tt-id]
  [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id tt-id}])

(hiccups/defhtml fn-link [nme href]
  [:a.fn-a8476 #_{:href href} nme])

(defn- fns-list [fns]
  (map (fn [x] (fn-link x "/#")) fns))

;;------------------------------------------------------------------------------
;; Sections
;;------------------------------------------------------------------------------

(hiccups/defhtml basics-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Basics"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Define"]
          [:td.body-885f4
            (fns-list ["def" "defn" "defn-" "let" "declare" "ns"])]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Branch"]
          [:td.body-885f4
            (fns-list ["if" "if-not" "when" "when-not" "when-let" "when-first"
              "if-let" "cond" "condp" "case" "when-some" "if-some"])]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Compare"]
          [:td.body-885f4
            (fns-list ["and" "or"])]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Loop"]
          [:td.body-885f4
            (fns-list ["map" "map-indexed" "reduce" "for" "doseq" "dotimes" "while"])]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (fns-list ["true?" "false?" "instance?" "nil?" "some?"])]]]]])

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
            (fns-list [
              "fn"
              "defn"
              "defn-"
              "definline"
              "identity"
              "constantly"
              "memfn"
              "comp"
              "complement"
              "partial"
              "juxt"
              "memoize"
              "fnil"
              "every-pred"
              "some-fn"])]
        [:tr.even-ff837
          [:td.label-9e0b7 "Call"]
          [:td.body-885f4
            (fns-list ["apply" "-&gt;" "-&gt;&gt;" "as-&gt;" "cond-&gt;"
              "cond-&gt;&gt;" "some-&gt;" "some-&gt;&gt;"])]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (fns-list ["fn?" "ifn?"])]]]]]])

(hiccups/defhtml numbers-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Numbers" (tt-icon "numbers")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Literals"]
          [:td.body-885f4
            [:span.literal-c3029 "7"]
            [:span.literal-c3029 "3.14"]
            [:span.literal-c3029 "-1.2e3"]
            [:span.literal-c3029 "0x0000ff"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Arithmetic"]
          [:td.body-885f4
            (fns-list ["+" "-" "*" "/" "quot" "rem" "mod" "inc" "dec" "max" "min"])]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Compare"]
          [:td.body-885f4
            (fns-list ["=" "==" "not=" "&lt;" "&gt;" "&lt;=" "&gt;="])]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Cast"]
          [:td.body-885f4
            (fns-list ["int" "float"])]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (fns-list ["zero?" "pos?" "neg?" "even?" "odd?" "number?" "integer?"])]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Random"]
          [:td.body-885f4
            [:a.fn-a8476 "rand"]
            [:a.fn-a8476 "rand-int"]]]]]])

(hiccups/defhtml strings-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "\" \" Strings" (tt-icon "strings")]
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
            [:span.literal-c3029 "(.-length my-str)"]
            [:a.fn-a8476 "count"]
            [:a.fn-a8476 "get"]
            [:a.fn-a8476 "subs"]
            [:span.literal-c3029 "(clojure.string/)"]
            [:a.fn-a8476 "join"]
            [:a.fn-a8476 "escape"]
            [:a.fn-a8476 "split"]
            [:a.fn-a8476 "split-lines"]
            [:a.fn-a8476 "replace"]
            [:a.fn-a8476 "replace-first"]
            [:a.fn-a8476 "reverse"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Regex"]
          [:td.body-885f4
            [:span.literal-c3029 "#\"" [:span {:style "font-style:italic"} "pattern"] "\""]
            [:a.fn-a8476 "re-find"]
            [:a.fn-a8476 "re-seq"]
            [:a.fn-a8476 "re-matches"]
            [:a.fn-a8476 "re-pattern"]
            [:span.literal-c3029 "(clojure.string/)"]
            [:a.fn-a8476 "replace"]
            [:a.fn-a8476 "replace-first"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Letters"]
          [:td.body-885f4
            [:span.literal-c3029 "(clojure.string/)"]
            [:a.fn-a8476 "capitalize"]
            [:a.fn-a8476 "lower-case"]
            [:a.fn-a8476 "upper-case"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Trim"]
          [:td.body-885f4
            [:span.literal-c3029 "(clojure.string/)"]
            [:a.fn-a8476 "trim"]
            [:a.fn-a8476 "trim-newline"]
            [:a.fn-a8476 "triml"]
            [:a.fn-a8476 "trimr"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            [:a.fn-a8476 "char"]
            [:a.fn-a8476 "string?"]
            [:span.literal-c3029 "(clojure.string/)"]
            [:a.fn-a8476 "blank?"]]]]]])

(hiccups/defhtml atoms-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Atoms / State" (tt-icon "atoms")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            [:a.fn-a8476 "atom"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Get Value"]
          [:td.body-885f4
            [:span.literal-c3029 "@my-atom &rarr; (" [:span.inside-fn-c7607 "deref"] " my-atom)"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Set Value"]
          [:td.body-885f4
            [:a.fn-a8476 "swap!"]
            [:a.fn-a8476 "reset!"]
            [:a.fn-a8476 "compare-and-set!"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Watch"]
          [:td.body-885f4
            [:a.fn-a8476 "add-watch"]
            [:a.fn-a8476 "remove-watch"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Validators"]
          [:td.body-885f4
            [:a.fn-a8476 "set-validator!"]
            [:a.fn-a8476 "get-validator"]]]]]])

(hiccups/defhtml js-interop-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "JavaScript Interop"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create Object"]
          [:td.body-885f4
            [:span.literal-c3029 "#js {}"]
            [:a.fn-a8476 "js-obj"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Create Array"]
          [:td.body-885f4
            [:span.literal-c3029 "#js []"]
            [:a.fn-a8476 "array"]
            [:a.fn-a8476 "make-array"]
            [:a.fn-a8476 "aclone"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Property Access"]
          [:td.body-885f4
            [:div.row-5dec8 "(.-innerHTML el)"]
            [:div.row-5dec8 "(" [:a.inside-fn-c7607 "aget"] " el \"innerHTML\")"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Property Setting"]
          [:td.body-885f4
            [:div.row-5dec8 "(" [:a.inside-fn-c7607 "set!"] " (.-innerHTML el) \"Hi!\")"]
            [:div.row-5dec8 "(" [:a.inside-fn-c7607 "aset"] " el \"innerHTML\" \"Hi!\")"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Convert Between"]
          [:td.body-885f4
            [:a.fn-a8476 "clj-&gt;js"]
            [:a.fn-a8476 "js-&gt;clj"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Type Tests"]
          [:td.body-885f4
            [:a.fn-a8476 "array?"]
            [:a.fn-a8476 "fn?"]
            [:a.fn-a8476 "number?"]
            [:a.fn-a8476 "object?"]
            [:a.fn-a8476 "string?"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Exceptions"]
          [:td.body-885f4
            [:a.fn-a8476 "try"]
            [:a.fn-a8476 "catch"]
            [:a.fn-a8476 "finally"]
            [:a.fn-a8476 "throw"]]]
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
            [:a.fn-a8476 "count"]
            [:a.fn-a8476 "empty"]
            [:a.fn-a8476 "not-empty"]
            [:a.fn-a8476 "into"]
            [:a.fn-a8476 "conj"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Content Tests"]
          [:td.body-885f4
            [:a.fn-a8476 "distinct?"]
            [:a.fn-a8476 "empty?"]
            [:a.fn-a8476 "every?"]
            [:a.fn-a8476 "not-every?"]
            [:a.fn-a8476 "some"]
            [:a.fn-a8476 "not-any?"]
            ]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Capabilities"]
          [:td.body-885f4
            [:a.fn-a8476 "sequential?"]
            [:a.fn-a8476 "associative?"]
            [:a.fn-a8476 "sorted?"]
            [:a.fn-a8476 "counted?"]
            [:a.fn-a8476 "reversible?"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Type Tests"]
          [:td.body-885f4
            [:a.fn-a8476 "coll?"]
            [:a.fn-a8476 "list?"]
            [:a.fn-a8476 "vector?"]
            [:a.fn-a8476 "set?"]
            [:a.fn-a8476 "map?"]
            [:a.fn-a8476 "seq?"]]]]]])

(hiccups/defhtml lists-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "( ) Lists" (tt-icon "lists")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            [:span.literal-c3029 "'()"]
            [:a.fn-a8476 "list"]
            [:a.fn-a8476 "list*"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Examine"]
          [:td.body-885f4
            [:a.fn-a8476 "first"]
            [:a.fn-a8476 "nth"]
            [:a.fn-a8476 "peek"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            [:a.fn-a8476 "cons"]
            [:a.fn-a8476 "conj"]
            [:a.fn-a8476 "rest"]
            [:a.fn-a8476 "pop"]]]]]])

(hiccups/defhtml vectors-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "[ ] Vectors" (tt-icon "vectors")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            [:span.literal-c3029 "[]"]
            [:a.fn-a8476 "vector"]
            [:a.fn-a8476 "vec"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Examine"]
          [:td.body-885f4
            [:div.row-5dec8
              "(my-vec idx) &rarr; (" [:a.inside-fn-c7607 "nth"] " my-vec idx)"
              (tt-icon "vector-as-fn")]
            [:a.fn-a8476 "get"]
            [:a.fn-a8476 "peek"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            [:a.fn-a8476 "assoc"]
            [:a.fn-a8476 "pop"]
            [:a.fn-a8476 "subvec"]
            [:a.fn-a8476 "replace"]
            [:a.fn-a8476 "conj"]
            [:a.fn-a8476 "rseq"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Loop"]
          [:td.body-885f4
            [:a.fn-a8476 "mapv"]
            [:a.fn-a8476 "filterv"]
            [:a.fn-a8476 "reduce-kv"]]]]]])

(hiccups/defhtml sets-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "#{ } Sets" (tt-icon "sets")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            [:span.literal-c3029 "#{}"]
            [:a.fn-a8476 "set"]
            [:a.fn-a8476 "hash-set"]
            [:a.fn-a8476 "sorted-set"]
            [:a.fn-a8476 "sorted-set-by"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Examine"]
          [:td.body-885f4
            [:div.row-5dec8
              "(my-set itm) &rarr; (" [:a.inside-fn-c7607 "get"] " my-set itm)"
              (tt-icon "set-as-fn")]
            [:a.fn-a8476 "contains?"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            [:a.fn-a8476 "conj"]
            [:a.fn-a8476 "disj"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Set Ops"]
          [:td.body-885f4
            [:span.literal-c3029 "(clojure.set/)"]
            [:a.fn-a8476 "union"]
            [:a.fn-a8476 "difference"]
            [:a.fn-a8476 "intersection"]
            [:a.fn-a8476 "select"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            [:span.literal-c3029 "(clojure.set/)"]
            [:a.fn-a8476 "subset?"]
            [:a.fn-a8476 "superset?"]]]]]])

(hiccups/defhtml maps-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "{ } Maps" (tt-icon "maps")]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            [:div.row-5dec8 "{:key1 \"a\" :key2 \"b\"}"]
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
            [:div.row-5dec8
              "(:key my-map) &rarr; (" [:a.inside-fn-c7607 "get"] " my-map :key)"
              (tt-icon "keywords-as-fn")]
            [:a.fn-a8476 "get-in"]
            [:a.fn-a8476 "contains?"]
            [:a.fn-a8476 "find"]
            [:a.fn-a8476 "keys"]
            [:a.fn-a8476 "vals"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            [:a.fn-a8476 "assoc"]
            [:a.fn-a8476 "assoc-in"]
            [:a.fn-a8476 "dissoc"]
            [:a.fn-a8476 "merge"]
            [:a.fn-a8476 "merge-with"]
            [:a.fn-a8476 "select-keys"]
            [:a.fn-a8476 "update-in"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Entry"]
          [:td.body-885f4
            [:a.fn-a8476 "key"]
            [:a.fn-a8476 "val"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Sorted Maps"]
          [:td.body-885f4
            [:a.fn-a8476 "rseq"]
            [:a.fn-a8476 "subseq"]
            [:a.fn-a8476 "rsubseq"]]]]]])

(hiccups/defhtml create-seq-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Create a Seq"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "From Collection"]
          [:td.body-885f4
            [:a.fn-a8476 "seq"]
            [:a.fn-a8476 "vals"]
            [:a.fn-a8476 "keys"]
            [:a.fn-a8476 "rseq"]
            [:a.fn-a8476 "subseq"]
            [:a.fn-a8476 "rsubseq"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Producer Functions"]
          [:td.body-885f4
            [:a.fn-a8476 "lazy-seq"]
            [:a.fn-a8476 "repeatedly"]
            [:a.fn-a8476 "iterate"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "From Constant"]
          [:td.body-885f4
            [:a.fn-a8476 "repeat"]
            [:a.fn-a8476 "range"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "From Other"]
          [:td.body-885f4
            [:a.fn-a8476 "re-seq"]
            [:a.fn-a8476 "tree-seq"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "From Sequence"]
          [:td.body-885f4
            [:a.fn-a8476 "keep"]
            [:a.fn-a8476 "keep-indexed"]]]]]])

(hiccups/defhtml seq-in-out-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Seq in, Seq out"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Get Shorter"]
          [:td.body-885f4
            [:a.fn-a8476 "distinct"]
            [:a.fn-a8476 "filter"]
            [:a.fn-a8476 "remove"]
            [:a.fn-a8476 "take-nth"]
            [:a.fn-a8476 "for"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Get Longer"]
          [:td.body-885f4
            [:a.fn-a8476 "cons"]
            [:a.fn-a8476 "conj"]
            [:a.fn-a8476 "concat"]
            [:a.fn-a8476 "lazy-cat"]
            [:a.fn-a8476 "mapcat"]
            [:a.fn-a8476 "cycle"]
            [:a.fn-a8476 "interleave"]
            [:a.fn-a8476 "interpose"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Get From Tail"]
          [:td.body-885f4
            [:a.fn-a8476 "rest"]
            [:a.fn-a8476 "nthrest"]
            [:a.fn-a8476 "next"]
            [:a.fn-a8476 "fnext"]
            [:a.fn-a8476 "nnext"]
            [:a.fn-a8476 "drop"]
            [:a.fn-a8476 "drop-while"]
            [:a.fn-a8476 "take-last"]
            [:a.fn-a8476 "for"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Get From Head"]
          [:td.body-885f4
            [:a.fn-a8476 "take"]
            [:a.fn-a8476 "take-while"]
            [:a.fn-a8476 "butlast"]
            [:a.fn-a8476 "drop-last"]
            [:a.fn-a8476 "for"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "'Change'"]
          [:td.body-885f4
            [:a.fn-a8476 "conj"]
            [:a.fn-a8476 "concat"]
            [:a.fn-a8476 "distinct"]
            [:a.fn-a8476 "flatten"]
            [:a.fn-a8476 "group-by"]
            [:a.fn-a8476 "partition"]
            [:a.fn-a8476 "partition-all"]
            [:a.fn-a8476 "partition-by"]
            [:a.fn-a8476 "split-at"]
            [:a.fn-a8476 "split-with"]
            [:a.fn-a8476 "filter"]
            [:a.fn-a8476 "remove"]
            [:a.fn-a8476 "replace"]
            [:a.fn-a8476 "shuffle"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Rearrange"]
          [:td.body-885f4
            [:a.fn-a8476 "reverse"]
            [:a.fn-a8476 "sort"]
            [:a.fn-a8476 "sort-by"]
            [:a.fn-a8476 "compare"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Process Items"]
          [:td.body-885f4
            [:a.fn-a8476 "map"]
            [:a.fn-a8476 "pmap"]
            [:a.fn-a8476 "map-indexed"]
            [:a.fn-a8476 "mapcat"]
            [:a.fn-a8476 "for"]
            [:a.fn-a8476 "replace"]
            [:a.fn-a8476 "seque"]]]]]])

(hiccups/defhtml use-seq-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Using a Seq"]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Extract Item"]
          [:td.body-885f4
            [:a.fn-a8476 "first"]
            [:a.fn-a8476 "second"]
            [:a.fn-a8476 "last"]
            [:a.fn-a8476 "rest"]
            [:a.fn-a8476 "next"]
            [:a.fn-a8476 "ffirst"]
            [:a.fn-a8476 "nfirst"]
            [:a.fn-a8476 "fnext"]
            [:a.fn-a8476 "nnext"]
            [:a.fn-a8476 "nth"]
            [:a.fn-a8476 "nthnext"]
            [:a.fn-a8476 "rand-nth"]
            [:a.fn-a8476 "when-first"]
            [:a.fn-a8476 "max-key"]
            [:a.fn-a8476 "min-key"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Construct Collection"]
          [:td.body-885f4
            [:a.fn-a8476 "zipmap"]
            [:a.fn-a8476 "into"]
            [:a.fn-a8476 "reduce"]
            [:a.fn-a8476 "reductions"]
            [:a.fn-a8476 "set"]
            [:a.fn-a8476 "vec"]
            [:a.fn-a8476 "into-array"]
            [:a.fn-a8476 "to-array-2d"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Pass to Function"]
          [:td.body-885f4
            [:a.fn-a8476 "apply"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Search"]
          [:td.body-885f4
            [:a.fn-a8476 "some"]
            [:a.fn-a8476 "filter"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Force Evaluation"]
          [:td.body-885f4
            [:a.fn-a8476 "doseq"]
            [:a.fn-a8476 "dorun"]
            [:a.fn-a8476 "doall"]]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Check For Forced"]
          [:td.body-885f4
            [:a.fn-a8476 "realized?"]]]]]])

(hiccups/defhtml bitwise-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Bitwise"]
    [:div.solo-section-d5309
      (fns-list ["bit-and" "bit-or" "bit-xor" "bit-not" "bit-flip"
        "bit-set" "bit-shift-right" "bit-shift-left" "bit-and-not"
        "bit-clear" "bit-test" "unsigned-bit-shift-right"])]])

;;------------------------------------------------------------------------------
;; Tooltips
;;------------------------------------------------------------------------------

;; NOTE: Tooltip for sequences: most sequence functions work on Strings as well.
;; You're welcome.

(hiccups/defhtml tooltips []

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
          [:td.code-72fa0.right-border-c1b54 "#(* % (apply + %&))"]
          [:td.code-72fa0 [:pre {:style "font-size:10px"}
            "(fn [x & the-rest]\n"
            "  (* x (apply + the-rest)))"]]]]]]

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

  [:div#tooltip-numbers.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "All ClojureScript Numbers are IEEE 754 Double Precision floating point. "
      "The same as JavaScript."]]

  [:div#tooltip-strings.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "ClojureScript Strings are JavaScript Strings and have all of the native "
      "methods and properties that a JavaScript String has."]
    [:p.info-2e4f9
      "ClojureScript Strings must be defined using double quotes."]
    [:p.info-2e4f9
      "The " [:code "clojure.string"] " namespace provides many useful "
      "functions for dealing with strings."]]

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
          [:td.tt-cell-e6fd2.right-border-c1b54 "list"]
          [:td.tt-cell-e6fd2 [:code "()"]]]
        [:tr.dark-even-6cd97
          [:td.tt-cell-e6fd2.right-border-c1b54 "vector"]
          [:td.tt-cell-e6fd2 [:code "[]"]]]
        [:tr.dark-odd-7aff7
          [:td.tt-cell-e6fd2.right-border-c1b54 "set"]
          [:td.tt-cell-e6fd2 [:code "#{}"]]]
        [:tr.dark-even-6cd97
          [:td.tt-cell-e6fd2.right-border-c1b54 "map"]
          [:td.tt-cell-e6fd2 [:code "{}"]]]]]]

  [:div#tooltip-lists.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Lists are a sequence of values, similar to a vector."]
    [:p.info-2e4f9
      "Most literal lists in a ClojureScript program represent a function call."]
    [:p.info-2e4f9
      "ie: " [:code "(a b c)"] " is a list of three things, and it also means "
      "\"call the function a with two arguments: b and c\""]]

  [:div#tooltip-sets.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9 "Sets are collections of unique values."]]

  [:div#tooltip-vectors.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Vectors are collections of values that are indexed by sequential integers."]
    [:p.info-2e4f9
      "Though similar, a ClojureScript Vector is not the same thing as a JavaScript Array. "
      "ie: " [:code "(.indexOf my-vec)"] " will not work on a ClojureScript Vector."]]

  [:div#tooltip-vector-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "A Vector can be used as a function to access its elements."]]

  [:div#tooltip-set-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "A Set can be used as a function to access its elements."]]

  [:div#tooltip-maps.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "A Map is a collection that maps keys to values. "
      "Accessing a value in a map using a key is very fast."]
    [:p.info-2e4f9
      "In JavaScript, Objects are commonly used as a de facto map using "
      "strings as keys. "
      "A key in a ClojureScript Map can be any value, although keywords are "
      "commonly used."]]

  [:div#tooltip-keywords-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Keywords can be used as functions to get a value from a map. "
      "They are commonly used as map keys for this reason."]])

;;------------------------------------------------------------------------------
;; Page
;;------------------------------------------------------------------------------

(hiccups/defhtml page []

  [:div.header-2a8a6
    [:img.logo-6ced3 {:src "/img/clojure-logo.png" :alt "Clojure Logo"}]
    [:h1.title-7a29c "ClojureScript Cheatsheet"]
    [:input#searchInput.search-70fb8 {:type "text" :placeholder "Search"}]
    [:label#toggleTooltips.tooltips-label-68aa0
      [:i.fa.fa-check-square-o] "Show tooltips?"]
    [:div.clr-43e49]]

  [:div.lrg-wrapper-cc101
    [:h2.group-title-68f3c "Basics"]
    [:div.lrg-col-left-d5f6d
      (basics-section)
      (functions-section)]
    [:div.lrg-col-cntr-bb1aa
      (numbers-section)
      (strings-section)]
    [:div.lrg-col-right-0f4a3
      (atoms-section)
      (js-interop-section)]

    [:h2.group-title-68f3c "Collections"]
    [:div.lrg-col-left-d5f6d
      (collections-section)
      (lists-section)]
    [:div.lrg-col-cntr-bb1aa
      (vectors-section)
      (sets-section)]
    [:div.lrg-col-right-0f4a3
      (maps-section)]

    [:h2.group-title-68f3c "Sequences"]
    [:div.lrg-col-left-d5f6d (create-seq-section)]
    [:div.lrg-col-cntr-bb1aa (seq-in-out-section)]
    [:div.lrg-col-right-0f4a3 (use-seq-section)]

    [:h2.group-title-68f3c "Misc"]
    [:div.lrg-col-left-d5f6d
      (bitwise-section)]
    [:div.clr-43e49]]

  [:div.med-wrapper-87a24
    [:h2.group-title-68f3c "Basics"]
    [:div.med-col-left-06d5d
      (basics-section)
      (numbers-section)
      (js-interop-section)]
    [:div.med-col-right-eb5c2
      (functions-section)
      (strings-section)
      (atoms-section)]

    [:h2.group-title-68f3c "Collections"]
    [:div.med-col-left-06d5d
      (collections-section)
      (lists-section)
      (maps-section)]
    [:div.med-col-right-eb5c2
      (vectors-section)
      (sets-section)]

    [:h2.group-title-68f3c "Sequences"]
    [:div.med-col-left-06d5d
      (create-seq-section)
      (use-seq-section)]
    [:div.med-col-right-eb5c2
      (seq-in-out-section)]

    [:h2.group-title-68f3c "Misc"]
    [:div.med-col-left-06d5d
      (bitwise-section)]
    [:div.clr-43e49]]

  [:div.sml-wrapper-7eff3
    [:h2.group-title-68f3c "Basics"]
    [:div.sml-col-d8a7e
      (basics-section)
      (functions-section)
      (numbers-section)
      (strings-section)
      (js-interop-section)
      (atoms-section)]

    [:h2.group-title-68f3c "Collections"]
    [:div.sml-col-d8a7e
      (collections-section)
      (lists-section)
      (vectors-section)
      (sets-section)
      (maps-section)]

    [:h2.group-title-68f3c "Sequences"]
    [:div.sml-col-d8a7e
      (create-seq-section)
      (use-seq-section)
      (seq-in-out-section)]

    [:h2.group-title-68f3c "Misc"]
    [:div.sml-col-d8a7e
      (bitwise-section)]
    [:div.clr-43e49]]

  (tooltips))