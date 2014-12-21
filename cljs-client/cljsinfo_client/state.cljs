(ns cljsinfo-client.state
  (:require
    [cljsinfo-client.util :refer [js-log log]]))

;;------------------------------------------------------------------------------
;; Shared State
;;------------------------------------------------------------------------------

(def current-size (atom nil))