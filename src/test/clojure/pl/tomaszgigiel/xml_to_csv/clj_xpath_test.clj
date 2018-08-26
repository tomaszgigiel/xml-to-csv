(ns pl.tomaszgigiel.xml-to-csv.clj-xpath-test
  (:use [clojure.test])
  (:require [clj-xpath.core :as xpath])
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [pl.tomaszgigiel.xml-to-csv.clj-xpath :as this-xpath])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as this-common])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config]))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(def sample-simple-xml (this-common/string-from-resource "sample-simple.xml"))
(def sample-trapeze-xml (this-common/string-from-resource "sample-trapeze.xml"))

(deftest path-text-seq-test
  (is (= (->> "sample-simple.edn" io/resource slurp edn/read-string) (this-xpath/path-text-seq sample-simple-xml)) "list path text pairs"))

(deftest simple-xml-to-csv-test
  (is (= (string/split-lines (this-common/string-from-resource "sample-simple.csv")) (this-xpath/xml-to-csv sample-simple-xml ";"))) "sample-simple.xml to csv")

(deftest trapeze-xml-to-csv-test
  (is (= (string/split-lines (this-common/string-from-resource "sample-trapeze.csv")) (this-xpath/xml-to-csv sample-trapeze-xml ";"))) "sample-trapeze.xml to csv")
