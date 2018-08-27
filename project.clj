(defproject xml-to-csv "1.0.0.0-SNAPSHOT"
  :description "xml-to-csv: CML to CSV in Clojure"
  :url "http://tomaszgigiel.pl"
  :license {:name "Apache License"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.cli "0.3.7"]
                 [org.clojure/tools.logging "0.4.1"]
                 ;; otherwise log4j.properties has no effect
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]
                 [com.github.kyleburton/clj-xpath "1.4.11"]]

  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  :resource-paths ["src/main/resources"]
  :target-path "target/%s"

  :profiles {:uberjar {:aot :all :jar-name "xml-to-csv.jar" :uberjar-name "xml-to-csv-uberjar.jar"}
             :main-xml-to-csv {:main ^:skip-aot pl.tomaszgigiel.xml-to-csv.core}
             :dev {:resource-paths ["src/test/resources"] :jmx-opts ["-Xmx512m"]}}
  :aliases {"run-main-xml-to-csv" ["with-profile" "main-xml-to-csv,dev" "run"]})
