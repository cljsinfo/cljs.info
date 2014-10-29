(ns cljsinfo-client.tooltips
  (:require
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Tooltips State
;;------------------------------------------------------------------------------

(def active-tooltips (atom {}))

(defn- on-change-tooltips [_ _ old-t new-t]
  ;; TODO: write me
  )

(add-watch active-tooltips :change on-change-tooltips)

;;------------------------------------------------------------------------------
;; Init and Events
;;------------------------------------------------------------------------------

(defn- on-mouseenter [js-evt]
  (let [current-target (aget js-evt "currentTarget")
        tooltip-num (.attr ($ current-target) "data-tooltip-id")
        tooltip-id (str "tooltip-" tooltip-num)
        tooltip-el (by-id tooltip-id)]
    (when tooltip-el
      (show-el! tooltip-id))))

(defn- on-mouseleave [js-evt]
  (let [current-target (aget js-evt "currentTarget")
        tooltip-num (.attr ($ current-target) "data-tooltip-id")
        tooltip-id (str "tooltip-" tooltip-num)
        tooltip-el (by-id tooltip-id)]
    (when tooltip-el
      (hide-el! tooltip-id))))

(defn init!
  "Initialize tooltips."
  []
  (.on ($ "body") "mouseenter" ".tooltip-0e91b" on-mouseenter)
  (.on ($ "body") "mouseleave" ".tooltip-0e91b" on-mouseleave))