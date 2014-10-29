(ns cljsinfo-client.core
  (:require
    [cljsinfo-client.tooltips :as tooltips]
    [cljsinfo-client.util :refer [js-log log]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Global Client Init
;;------------------------------------------------------------------------------

(defn- init! []
  (tooltips/init!))

($ init!)