(defproject clinp "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://github.com/dtackett/clinp"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2127"]
                 [specljs "2.8.0"]]

  :plugins [[lein-cljsbuild "1.0.1"]
            ;; specljs
            [specljs "2.8.0"]]

  :source-paths ["src"]

  :cljsbuild {
              :builds {
                       :final {:id "clinp"
                               :source-paths ["src"]
                               :compiler {
                                          :output-to "target/clinp.js"
                                          :output-dir "target/clinp"
                                          :optimizations :whitespace
                                          :source-map "target/clinp.js.map"}}

                       :dev {:id "clinp"
                             :source-paths ["src" "spec"]
                             :compiler {
                                        :output-to "target/clinp.js"
                                        :output-dir "target"
                                        :optimizations :whitespace

                                        :pretty-print true}
                             :notify-command ["phantomjs"
                                              "bin/phantom/spec-runner.js"
                                              "bin/phantom/spec-runner.html"]}}})
