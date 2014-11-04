(ns cljsinfo-client.cheatsheet
  (:require
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Window Size
;;------------------------------------------------------------------------------

;; TODO:
;; - move this to CSS media queries?
;; - create a 4 column layout

(defn- width->size [w]
  (cond
    (>= w 1060) :large
    (>= w 660)  :medium
    :else       :small))

(def current-size (atom nil))

(defn- on-change-size [_ _ _ new-size]
  (-> ($ "body")
    (.removeClass "sml-5dcf3 med-0000a lrg-92b4d")
    (.addClass
      (case new-size
        :small  "sml-5dcf3"
        :medium "med-0000a"
        :large  "lrg-92b4d"
        :else nil))))

(add-watch current-size :change on-change-size)

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

(defn- add-events []
  (.on ($ "#toggleTooltips") "click" click-toggle-tooltips)
  (aset js/window "onresize" on-resize)
  )

;;------------------------------------------------------------------------------
;; Init
;;------------------------------------------------------------------------------

(defn init!
  "Initialize the cheatsheet page."
  []
  (add-events)
  (on-resize)
  ;; put focus on the search field
  (if-let [search-input-el (by-id "searchInput")]
    (.focus search-input-el)))