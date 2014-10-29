(ns cljsinfo-client.tooltips
  (:require
    [clojure.set :refer [difference]]
    [cljsinfo-client.dom :refer [by-id set-html! show-el! hide-el!]]
    [cljsinfo-client.util :refer [js-log log uuid]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Helper
;;------------------------------------------------------------------------------

(defn- evt->tt-num
  "Returns a tooltip-id from a JS event or nil if the tooltip was not found."
  [js-evt]
  (let [current-target (aget js-evt "currentTarget")
        tooltip-num (.attr ($ current-target) "data-tooltip-id")
        tooltip-id (str "tooltip-" tooltip-num)
        tooltip-el (by-id tooltip-id)]
    (if tooltip-el
      tooltip-id)))

;;------------------------------------------------------------------------------
;; Tooltips State
;;------------------------------------------------------------------------------

(def hovered (atom #{}))
(def pinned (atom #{}))

(defn- on-change-hovered [_ _ old-ids new-ids]
  (let [removed-ids (difference old-ids new-ids)
        added-ids (difference new-ids old-ids)]
    (doall (map hide-el! removed-ids))
    (doall (map show-el! added-ids))))

(defn- on-change-pinned [_ _ old-ids new-ids]
  ;; TODO: write me
  )

(add-watch hovered :change on-change-hovered)
(add-watch pinned :change on-change-pinned)

;;------------------------------------------------------------------------------
;; Init and Events
;;------------------------------------------------------------------------------

(defn- on-mouseenter [js-evt]
  (let [tooltip-id (evt->tt-num js-evt)]
    (when (and tooltip-id
               (not (contains? @pinned tooltip-id)))
      (swap! hovered conj tooltip-id))))

(defn- on-mouseleave [js-evt]
  (let [tooltip-id (evt->tt-num js-evt)]
    (when (and tooltip-id
               (not (contains? @pinned tooltip-id)))
      (swap! hovered disj tooltip-id))))

(defn- on-click [js-evt]
  (let [tooltip-id (evt->tt-num js-evt)]
    (if (contains? @pinned tooltip-id)
      (swap! pinned disj tooltip-id)
      (swap! pinned conj tooltip-id))))

;;------------------------------------------------------------------------------
;; Init
;;------------------------------------------------------------------------------

(defn init!
  "Initialize tooltip events."
  []
  (doto ($ "body")
    (.on "mouseenter" ".tooltip-0e91b" on-mouseenter)
    (.on "mouseleave" ".tooltip-0e91b" on-mouseleave)
    (.on "click" ".tooltip-0e91b" on-click)))