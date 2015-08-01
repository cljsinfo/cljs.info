(ns cljsinfo-server.docs
  (:require
    [cljs.reader :refer [read-string]]
    [cljsinfo-server.config :refer [config]]
    [cljsinfo-server.util :refer [ts-log]]))

;;------------------------------------------------------------------------------
;; Node libraries
;;------------------------------------------------------------------------------

(def fs         (js/require "fs-extra"))
(def js-request (js/require "request"))

;;------------------------------------------------------------------------------
;; Docs
;;------------------------------------------------------------------------------

(def docs-filename "docs.edn")
(def js-file-encoding (js-obj "encoding" "utf-8"))
(def docs-url "https://raw.githubusercontent.com/cljsinfo/cljs-api-docs/catalog/cljs-api.edn")

(def docs
  "This atom holds the docs data. Occasionally updated by polling GitHub."
  (atom {}))

(defn fetch-and-update-docs!
  "Fetches the latest docs from GitHub and updates the docs atom with them."
  []
  (ts-log "Fetching latest docs from GitHub...")
  (js-request docs-url (fn [err js-resp body-txt]
    (if (or err (not= 200 (aget js-resp "statusCode")))
      (ts-log "Failed to fetch latest API docs from GitHub. Are you connected to the internet?")
      (do (.writeFile fs docs-filename body-txt js-file-encoding)
          ;; TODO: need to wrap read-string in a try/catch or otherwise make sure
          ;;       it's valid EDN
          (reset! docs (:symbols (read-string body-txt)))
          (ts-log "Latest docs fetched, written to docs.edn, and docs atom updated."))))))

;; try to load the docs from docs.edn, else fetch the most recent ones from GitHub
(if-let [docs-string (try (.readFileSync fs docs-filename js-file-encoding)
                          (catch js/Error err false))]
  (do (reset! docs (:symbols (read-string docs-string)))
      (ts-log "Loaded docs from docs.edn"))
  (fetch-and-update-docs!))

;; fetch fresh docs from GitHub on load (config option)
(when (true? (:fetch-docs-on-load? config))
  (fetch-and-update-docs!))

;; begin polling for doc updates (config option)
(when (number? (:fetch-docs-every-n-seconds config))
  (js/setInterval fetch-and-update-docs! (* 1000 (:fetch-docs-every-n-seconds config))))
