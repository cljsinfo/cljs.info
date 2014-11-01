(ns cljsinfo-client.cheatsheet
  (:require
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Events
;;------------------------------------------------------------------------------

(defn- click-toggle-tooltips []
  ;; TODO: write me
  )

(defn- add-events []
  (.on ($ "#toggleTooltips") "click" click-toggle-tooltips)
  )

;;------------------------------------------------------------------------------
;; Init
;;------------------------------------------------------------------------------

(defn init!
  "Initialize the cheatsheet page."
  []
  (add-events))