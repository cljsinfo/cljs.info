(ns cljsinfo-server.pages.cheatsheet
  (:require-macros [hiccups.core :as hiccups])
  (:require
    hiccups.runtime
    [cljsinfo-server.util :as util]))

;;------------------------------------------------------------------------------
;; Function Lists
;;------------------------------------------------------------------------------

(hiccups/defhtml fn-link [nme href]
  [:a.fn-a8476 #_{:href href} nme])

(defn- fns-list [fns]
  (map (fn [x] (fn-link x "/#")) fns))

;;------------------------------------------------------------------------------
;; Sections
;;------------------------------------------------------------------------------

(hiccups/defhtml numbers-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Numbers"
      [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "numbers"}]]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Literals"]
          [:td.body-885f4
            [:span.literal-c3029 "7"]
            [:span.literal-c3029 "3.14"]
            [:span.literal-c3029 "-1e3"]]]
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
            (fns-list ["byte" "short" "int" "long" "float" "double" "num"])]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Test"]
          [:td.body-885f4
            (fns-list ["zero?" "pos?" "neg?" "even?" "odd?" "number?"
              "rational?" "integer?" "decimal?" "float?"])]]
        [:tr.even-ff837
          [:td.label-9e0b7 "Random"]
          [:td.body-885f4
            [:a.fn-a8476 "rand"]
            [:a.fn-a8476 "rand-int"]]]]]])

(hiccups/defhtml strings-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "\" \" Strings"
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
            [:a.fn-a8476 "char?"]
            [:a.fn-a8476 "string?"]
            [:span.literal-c3029 "(clojure.string/)"]
            [:a.fn-a8476 "blank?"]]]]]])

(hiccups/defhtml js-interop-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "JavaScript Interop"
      [:i.fa.fa-info-circle.tooltip-link-0e91b]]
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
            [:span.literal-row-5dec8 "(.-innerHTML el)"]
            [:span.literal-row-5dec8 "(" [:a.inside-fn-c7607 "aget"] " el \"innerHTML\")"]]]
        [:tr.odd-372e6
          [:td.label-9e0b7 "Property Setting"]
          [:td.body-885f4
            [:span.literal-row-5dec8 "(set! (.-innerHTML el) \"Hi!\")"]
            [:span.literal-row-5dec8 "(" [:a.inside-fn-c7607 "aset"] " el \"innerHTML\" \"Hi!\")"]]]]]])

(hiccups/defhtml collections-section []
  [:div.section-31efe
    [:h3.section-title-8ccf5 "Collections"
      [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "collections"}]]
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
    [:h3.section-title-8ccf5 "( ) Lists"
      [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "lists"}]]
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
    [:h3.section-title-8ccf5 "[ ] Vectors"
      [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "vectors"}]]
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
            [:span.literal-row-5dec8
              "(my-vec idx) &rarr; (" [:a.inside-fn-c7607 "nth"] " my-vec idx)"
              [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "vector-as-fn"}]]
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
    [:h3.section-title-8ccf5 "#{ } Sets"
      [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "sets"}]]
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
            [:span.literal-row-5dec8
              "(my-set itm) &rarr; (" [:a.inside-fn-c7607 "get"] " my-set itm)"
              [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "set-as-fn"}]]
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
    [:h3.section-title-8ccf5 "{ } Maps"
      [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "maps"}]]
    [:table.tbl-902f0
      [:tbody
        [:tr.odd-372e6
          [:td.label-9e0b7 "Create"]
          [:td.body-885f4
            [:span.literal-row-5dec8 "{:key1 \"a\" :key2 \"b\"}"]
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
            [:span.literal-row-5dec8
              "(:key my-map) &rarr; (" [:a.inside-fn-c7607 "get"] " my-map :key)"
              [:i.fa.fa-info-circle.tooltip-link-0e91b {:data-tooltip-id "keywords-as-fn"}]]
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

;;------------------------------------------------------------------------------
;; Tooltips
;;------------------------------------------------------------------------------

;; NOTE: Tooltip for sequences: most sequence functions work on Strings as well.
;; You're welcome.

(hiccups/defhtml tooltips []
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

  [:div#tooltip-vectors.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "All vectors are collections and support the generic collection functions."]
    [:p.info-2e4f9
      "All vectors are also sequences and support the generic sequence functions."]
    [:p.info-2e4f9
      "A ClojureScript Vector is not the same thing as a JavaScript Array. "
      "ie: " [:code "(.indexOf my-vec)"] " will not work on a ClojureScript Vector."]]

  [:div#tooltip-vector-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "A Vector can be used as a function to access it's elements."]]

  [:div#tooltip-set-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "A Set can be used as a function to access it's elements."]]

  [:div#tooltip-maps.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9 "Maps - or hash maps - are a powerful data structure used often in ClojureScript programs."]
    [:p.info-2e4f9 "In JavaScript, Objects are commonly used as a de-facto hash map using strings as keys. "
      "A key in a ClojureScript Map can be any value, although commonly keywords are used."]
    [:p.info-2e4f9 "All maps are collections and support the generic collection functions."]
    [:p.info-2e4f9 "Maps are not a sequence, but most generic sequence functions can be used on a map. "
      "Sequence functions used on a map will return a sequence."]]

  [:div#tooltip-keywords-as-fn.tooltip-53dde {:style "display:none"}
    [:i.fa.fa-thumb-tack.pin-0ad63]
    [:p.info-2e4f9
      "Keywords can be used as functions to get a value from a map. "
      "They are commonly used as map keys for this reason."]])

;;------------------------------------------------------------------------------
;; Page
;;------------------------------------------------------------------------------

(hiccups/defhtml page []
  [:div.wrapper-cc101
    [:div.header-2a8a6
      [:img.logo-6ced3 {:src "/img/clojure-logo.png" :alt "Clojure Logo"}]
      [:h1.title-7a29c "ClojureScript Cheatsheet"]
      [:input.search-70fb8 {:type "text" :placeholder "Search"}]
      [:label#toggleTooltips.tooltips-label-68aa0 [:i.fa.fa-check-square-o] "Show tooltips?"]
      [:div.clr-43e49]]

    [:h2.group-title-68f3c "Basics"]
    (numbers-section)
    (strings-section)
    (js-interop-section)

    [:h2.group-title-68f3c "Collections"]
    (collections-section)
    (lists-section)
    (vectors-section)
    [:div.clr-43e49]
    (sets-section)
    (maps-section)

    [:h2.group-title-68f3c "Sequences"]
    (create-seq-section)
    (seq-in-out-section)
    (use-seq-section)

    [:h2.group-title-68f3c "Misc"]
    (js-interop-section)
    [:div.clr-43e49]]

  (tooltips))