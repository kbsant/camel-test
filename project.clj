(defproject camel-test "0.1.0-SNAPSHOT"
  :description "Camel test project"
  :url "https://github.com/kbsant/camel-test"
  :license {:name "Public domain"
            :url "https://unlicense.org/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.slf4j/slf4j-simple "1.7.31"]
                 [environ "1.2.0"]
                 [takeoff/clj-camel "1.6.6"]
                 [org.apache.camel/camel-http "3.8.0"]
                 ]
  :main ^:skip-aot camel-test.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
