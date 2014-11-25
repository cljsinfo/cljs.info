(ns cljs-cheatsheet.state
  (:require
    [cljs-cheatsheet.util :refer [js-log log uuid]]))

;; these atoms are used in multiple modules
(def active-tooltip (atom nil))
(def mouse-position (atom nil))
(def mousetrap-boxes (atom nil))

(defn- logger [_ _ o n]
  (log o)
  (log n)
  (js-log "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"))

;; (add-watch active-tooltip :log logger)
