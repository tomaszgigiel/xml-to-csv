(ns pl.tomaszgigiel.xml-to-csv.core
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.xml-to-csv.clj-xpath :as this-xpath])
  (:import java.io.File)
  (:gen-class))

(def cli-options
  [["-i" "--input-file XMLFILE" "input XML file"]
   ["-o" "--output-file CSVFILE" "output CSV file"]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["xml-to-csv: XML to CSV in Clojure"
        ""
        "Usage: java -jar /path/to/xml-to-csv-uberjar.jar -i /path/to/input.xml -o /another/path/to/output.csv"
        ""
        "Options:"
        options-summary
        ""
        "Please refer to the manual page for more information."]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with a error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:help options) ; help => exit OK with usage summary
      {:exit-message (usage summary) :ok? true}
      errors ; errors => exit with description of errors
      {:exit-message (error-msg errors)}
      ;; custom validation on arguments
      (= 0 (count arguments))
      {:options options}
      :else ; failed custom validation => exit with usage summary
      {:exit-message (usage summary)})))

(defn exit [status msg]
  (log/info msg)
  (System/exit status))

(defn xml-to-csv [input output]
  (with-open [in (io/input-stream input)
              out (io/writer output)]
    (doseq [line (this-xpath/xml-to-csv in ";")]
      (.write out line)
      (.newLine out))))

(defn -main [& args]
  "xml-to-csv: XML to CSV in Clojure"
  (let [{:keys [uri options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (xml-to-csv (new File (:input-file options)) (new File (:output-file options)))))
  (log/info "ok"))
