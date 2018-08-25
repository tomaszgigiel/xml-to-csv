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

(def xml (this-common/string-from-resource "simple.xml"))

(deftest path-text-seq-test
  (is (= (->> "simple.edn" io/resource slurp edn/read-string) (this-xpath/path-text-seq xml)) "list path text pairs"))

(deftest xml-to-csv-test
  (is (= (string/split-lines (this-common/string-from-resource "simple.csv")) (this-xpath/xml-to-csv xml ";"))) "simple xml to csv")

(deftest big-xml-to-csv-test
  (is (= 10502 (count (this-xpath/xml-to-csv (this-common/string-from-resource "sample-trapeze.xml") ";")))) "big xml to csv")
