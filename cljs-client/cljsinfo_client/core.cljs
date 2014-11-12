(ns cljsinfo-client.core
  (:require
    [cljsinfo-client.cheatsheet :as cheatsheet]
    [cljsinfo-client.threading-macro :as threading-macro]
    [cljsinfo-client.tooltips :as tooltips]
    [cljsinfo-client.util :refer [js-log log]]))

(def $ js/jQuery)

;;------------------------------------------------------------------------------
;; Global Client Init
;;------------------------------------------------------------------------------

(defn- init! [page]
  ;; initialize tooltips on all pages
  (tooltips/init!)

  ;; initialize a specific page
  (case page
    ;;"homepage" ()
    "cheatsheet" (cheatsheet/init!)
    "threading-macro" (threading-macro/init!)
    nil))

(js/goog.exportSymbol "CLJSINFO.init" init!)