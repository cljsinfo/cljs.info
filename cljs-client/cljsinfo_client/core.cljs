(ns cljsinfo-client.core
  (:require
    [cljsinfo-client.threading-macro :as threading-macro]
    [cljsinfo-client.util :refer [js-log log]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Global Client Init
;;------------------------------------------------------------------------------

(defn- init! [page]
  ;; initialize a specific page
  (case page
    ;;"homepage" ()
    "threading-macro" (threading-macro/init!)
    nil))

(js/goog.exportSymbol "CLJSINFO.init" init!)