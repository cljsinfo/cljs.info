(ns cljsinfo-server.config
  (:require
    [clojure.walk :refer [keywordize-keys]]
    [cljsinfo-server.util :refer [ts-log]]))

(def fs (js/require "fs-extra"))

;;------------------------------------------------------------------------------
;; Config
;;------------------------------------------------------------------------------

(def default-config-options {
  :fetch-docs-every-n-seconds false
  :fetch-docs-on-load? false
  :minified-client? false
  :port 9292
  :refresh-docs-key nil})

(def config
  (if-let [js-config (.readJsonSync fs "config.json" (js-obj "throws" false))]
    (do (ts-log "Loaded config settings from config.json")
        (->> js-config
             js->clj
             keywordize-keys
             (merge default-config-options)))
    (do (ts-log "Loaded default config settings (config.json not found or invalid)")
        default-config-options)))
