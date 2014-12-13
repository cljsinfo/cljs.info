(defproject cljs.info "0.1.0"

  :description "A cool ClojureScript website."
  :url "https://github.com/oakmac/cljs.info"
  :license {
    :name "MIT License"
    :url "https://github.com/oakmac/cljs.info/blob/master/LICENSE.md"
    :distribution :repo }

  :dependencies [
    [org.clojure/clojure "1.6.0"]
    [org.clojure/clojurescript "0.0-2411"]
    [hiccups "0.3.0"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :source-paths ["src"]

  :cljsbuild {
    :builds {
      :cheatsheet {
        :source-paths ["cljs-cheatsheet"]
        :compiler {
          :output-to "public/js/cheatsheet.js"
          :optimizations :whitespace }}

      :cheatsheet-adv {
        :source-paths ["cljs-cheatsheet"]
        :compiler {
          :externs ["externs/jquery-1.9.js"]
          :output-to "public/js/cheatsheet.min.js"
          :optimizations :advanced
          :pretty-print false }}

      :client {
        :source-paths ["cljs-client"]
        :compiler {
          :output-to "public/js/client.js"
          :optimizations :whitespace }}

      :client-adv {
        :source-paths ["cljs-client"]
        :compiler {
          :externs ["externs/jquery-1.9.js"]
          :output-to "public/js/client.min.js"
          :optimizations :advanced
          :pretty-print false }}

     :server {
      :source-paths ["cljs-server"]
      :compiler {
        :language-in :ecmascript5
        :language-out :ecmascript5
        :target :nodejs
        :output-to "app.js"
        :optimizations :simple }}
}})