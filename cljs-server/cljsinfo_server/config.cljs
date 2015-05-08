(ns cljsinfo-server.config
  (:require
    [clojure.walk :refer [keywordize-keys]]))

;;------------------------------------------------------------------------------
;; Config
;;------------------------------------------------------------------------------

;; TODO: print a useful error if config.json does not exist

(def config
  (-> (js/require "./config.json")
      js->clj
      keywordize-keys))
