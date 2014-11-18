(ns cljs-cheatsheet.core
  (:require
    [clojure.string :refer [blank?]]
    [cljs-cheatsheet.dom :refer [by-id]]
    [cljs-cheatsheet.html :as html]
    [cljs-cheatsheet.tooltips :as tooltips]
    [cljs-cheatsheet.util :refer [js-log log]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Window Size
;;------------------------------------------------------------------------------

;; TODO:
;; - move this to CSS media queries?
;; - create a 4 column layout

(def sml-layout-class "sml-5dcf3")
(def med-layout-class "med-0000a")
(def lrg-layout-class "lrg-92b4d")
(def layout-classes (str sml-layout-class " "
                         med-layout-class " "
                         lrg-layout-class))

(defn- width->size [w]
  (cond
    (>= w 1060) :large
    (>= w 660)  :medium
    :else       :small))

(def current-size (atom nil))

(defn- on-change-size [_ _ _ new-size]
  (-> ($ "body")
    (.removeClass layout-classes)
    (.addClass
      (case new-size
        :small  sml-layout-class
        :medium med-layout-class
        :large  lrg-layout-class
        :else nil))))

(add-watch current-size :change on-change-size)

;;------------------------------------------------------------------------------
;; Search
;;------------------------------------------------------------------------------

(def current-search-txt (atom ""))

(def matched-search-class "matched-e5c67")
(def matched-search-sel (str "." matched-search-class))
(def no-results-class "no-results-5d3ea")
(def fn-link-sel ".fn-a8476, .inside-fn-c7607")
(def group-sel ".group-2be36")
(def section-sel ".section-31efe")
(def search-input-id "searchInput")
(def search-input-sel (str "#" search-input-id))

(defn- show-all-groups-and-sections! []
  (.show ($ group-sel))
  (.show ($ section-sel)))

(defn- toggle-el!
  "Show / hide an element based on whether it contains a search match or not."
  [_idx el]
  (let [$el ($ el)
        $matched (.find $el matched-search-sel)
        matches-in-el? (pos? (aget $matched "length"))]
    (if matches-in-el?
      (.show $el)
      (.hide $el))))

(defn- toggle-groups-and-sections!
  "Show / hide groups and sections based on whether they contain a search match
   or not."
  []
  (.each ($ section-sel) toggle-el!)
  (.each ($ group-sel) toggle-el!))

(defn- any-matches-total? []
  (-> ($ matched-search-sel)
      (aget "length")
      pos?))

(defn- toggle-fn-link [el search-txt]
  (let [$link ($ el)
        link-txt (-> $link .text .toLowerCase)
        match? (not= -1 (.indexOf link-txt search-txt))]
    (if match?
      (.addClass $link matched-search-class)
      (.removeClass $link matched-search-class))))

(defn- toggle-fn-matches! [search-txt]
  (let [$links ($ fn-link-sel)]
    (.each $links #(toggle-fn-link %2 search-txt))))

(defn- clear-search! []
  (.removeClass ($ search-input-sel) no-results-class)
  (.removeClass ($ fn-link-sel) matched-search-class)
  (show-all-groups-and-sections!))

(defn- show-no-matches! []
  (.addClass ($ search-input-sel) no-results-class)
  (.removeClass ($ fn-link-sel) matched-search-class)
  (show-all-groups-and-sections!))

(defn- search! [txt]
  (toggle-fn-matches! txt)
  (if (any-matches-total?)
    (do
      (.removeClass ($ search-input-sel) no-results-class)
      (toggle-groups-and-sections!))
    (show-no-matches!)))

(defn- change-search-txt [_kwd _the-atom _old-txt new-txt]
  (if (blank? new-txt)
    (clear-search!)
    (search! new-txt)))

(add-watch current-search-txt :main change-search-txt)

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- click-toggle-tooltips []
  ;; TODO: write me
  )

(defn- on-resize []
  (let [browser-width (.width ($ js/window))
        new-size (width->size browser-width)]
    (when-not (= @current-size new-size)
      (reset! current-size new-size))))

(defn- keydown-search-input2 []
  (let [txt (-> ($ search-input-sel) .val .toLowerCase)]
    (when (not= txt @current-search-txt)
      (reset! current-search-txt txt))))

;; reset the stack so we can grab the value out of the text field
(defn- keydown-search-input []
  (js/setTimeout keydown-search-input2 1))

(defn- add-events! []
  ;; (.on ($ "#toggleTooltips") "click" click-toggle-tooltips)
  (.on ($ search-input-sel) "keydown" keydown-search-input)
  (aset js/window "onresize" on-resize))

;;------------------------------------------------------------------------------
;; Global Cheatsheet Init
;;------------------------------------------------------------------------------

(defn- init! []
  ;; load HTML on the page
  (.prepend ($ "body") (html/page))

  ;; initialize tooltip events
  (tooltips/init!)

  ;; add search and other events
  (add-events!)

  ;; trigger resize and search
  (on-resize)
  (keydown-search-input2)

  ;; put the focus on the search field
  (when-let [search-input-el (by-id search-input-id)]
    (.focus search-input-el)))

($ init!)