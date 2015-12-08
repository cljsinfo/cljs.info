(defproject cljs.info "0.1.0"

  :description "A cool ClojureScript website."
  :url "https://github.com/cljsinfo/cljs.info"

  :license {:name "MIT License"
            :url "https://github.com/cljsinfo/cljs.info/blob/master/LICENSE.md"
            :distribution :repo}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [com.cognitect/transit-cljs "0.8.207"]
                 [hiccups "0.3.0"]]

  :plugins [[lein-cljsbuild "1.1.1"]]

  :source-paths ["src"]

  :clean-targets ["app.js"
                  "public/js/cheatsheet.js"
                  "public/js/cheatsheet.min.js"
                  "public/js/client.js"
                  "public/js/client.min.js"
                  "target"]

  :cljsbuild
    {:builds
      [{:id "cheatsheet"
        :source-paths ["cljs-cheatsheet"]
        :compiler {:output-to "public/js/cheatsheet.js"
                   :optimizations :whitespace}}

       {:id "cheatsheet-adv"
        :source-paths ["cljs-cheatsheet"]
        :compiler {:externs ["externs/jquery-1.9.js"]
                   :output-to "public/js/cheatsheet.min.js"
                   :optimizations :advanced
                   :pretty-print false}}

       {:id "client"
        :source-paths ["cljs-client"]
        :compiler {:output-to "public/js/client.js"
                   :optimizations :whitespace}}

       {:id "client-adv"
        :source-paths ["cljs-client"]
        :compiler {:externs ["externs/jquery-1.9.js"]
                   :output-to "public/js/client.min.js"
                   :optimizations :advanced
                   :pretty-print false}}

       {:id "server"
        :source-paths ["cljs-server"]
        :compiler {:language-in :ecmascript5
                   :language-out :ecmascript5
                   :output-to "app.js"
                   :optimizations :simple
                   :target :nodejs}}]})
