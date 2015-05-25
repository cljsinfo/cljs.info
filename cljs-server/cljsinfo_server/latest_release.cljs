(ns cljsinfo-server.latest-release
  (:require
    [cljsinfo-server.util :refer [ts-log]]))

;;------------------------------------------------------------------------------
;; Fetch the latest CLJS release from GitHub
;;------------------------------------------------------------------------------

(def fs (js/require "fs-extra"))
(def js-moment  (js/require "moment"))
(def js-request (js/require "request"))

(def latest-release-api-url
  "https://api.github.com/repos/clojure/clojurescript/releases/latest")

;; User-Agent is required for the GitHub API
(def js-request-options (js-obj
  "headers" (js-obj "User-Agent" "cljs.info")
  "url" latest-release-api-url))

(defn- response-looks-good? [js-result]
  (and (goog/isObject js-result)
       (aget js-result "name")
       (aget js-result "created_at")))

; var a = moment([2007, 0, 28]);
; var b = moment([2007, 0, 29]);
; a.from(b) // "a day ago"
(defn- time-ago [time-str]
  (let [a (js-moment time-str)
        b (js-moment)]
    (.from a b)))

(defn- create-latest-from-api-result [js-result]
  {:release-date (aget js-result "created_at")
   :time-ago     (time-ago (aget js-result "created_at"))
   :version      (aget js-result "name")})

;; load the latest from disk if we have it
(def latest (atom
  (if-let [js-result (.readJsonSync fs "latest-release.json" (js-obj "throws" false))]
    (do (ts-log "Loaded latest CLJS release from latest-release.json")
        (create-latest-from-api-result js-result))
    nil)))

(defn- request-callback [err js-resp body-txt]
  (let [js-result (try (.parse js/JSON body-txt)
                    (catch js/Error err false))]
    (cond
      (or err (not= 200 (aget js-resp "statusCode")))
        (ts-log (str "Failed to fetch the latest release from the GitHub API. "
                     "Are you connected to the internet?"))

      (not js-result)
        (ts-log (str "Latest release from GitHub API was not valid JSON. "
                     "Chinese hackers again? Who knows?"))

      (response-looks-good? js-result)
        (do (ts-log (str "Updated latest release version: " (aget js-result "name")))
            (.writeJson fs "latest-release.json" js-result)
            (reset! latest (create-latest-from-api-result js-result)))

      :else
        (ts-log (str "Latest release from GitHub API didn't look right. "
                     "Should probably look into that.")))))

(defn- fetch-latest-release! []
  (ts-log "Fetching latest release version from GitHub...")
  (js-request js-request-options request-callback))

;; poll for update every hour
(def one-hour-in-ms (* 1000 60 60))
(js/setTimeout fetch-latest-release! one-hour-in-ms)

;; fetch now if we didn't load from disk
(when-not @latest
  (fetch-latest-release!))
