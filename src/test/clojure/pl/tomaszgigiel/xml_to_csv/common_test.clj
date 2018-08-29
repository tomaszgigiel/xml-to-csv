(ns pl.tomaszgigiel.xml-to-csv.common-test
  (:use [clojure.test])
  (:require [clojure.data.xml :as xml])
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as string])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as common])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config])
  (:import java.io.StringReader))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(def sample-simple-xml (misc/string-from-resource "sample-simple.xml"))
(def sample-trapeze-xml (misc/string-from-resource "sample-trapeze.xml"))

(deftest path-text-seq-test
  (is (= (list {:path [:a :b], :text "foo"} {:path [:a :c], :text "boo"}) (->> "<a><b>foo</b><c>boo</c></a>" StringReader. xml/parse common/path-text-seq)))
  (is (= (->> "sample-simple.edn" misc/string-from-resource edn/read-string) (->> sample-simple-xml StringReader. xml/parse common/path-text-seq))))

(deftest xml-to-csv-table
  (is (= (->> "short/a.edn" misc/string-from-resource edn/read-string) (->> "short/a.xml" misc/string-from-resource StringReader. xml/parse common/xml-to-csv-table)))
  (is (= (->> "short/b.edn" misc/string-from-resource edn/read-string) (->> "short/b.xml" misc/string-from-resource StringReader. xml/parse common/xml-to-csv-table)))
  ;(is (= (->> "short/c.edn" misc/string-from-resource edn/read-string) (->> "short/c.xml" misc/string-from-resource StringReader. xml/parse common/xml-to-csv-table)))
  ;(is (= (->> "short/d.edn" misc/string-from-resource edn/read-string) (->> "short/d.xml" misc/string-from-resource StringReader. xml/parse common/xml-to-csv-table)))
  (is (= (->> "short/e.edn" misc/string-from-resource edn/read-string) (->> "short/e.xml" misc/string-from-resource StringReader. xml/parse common/xml-to-csv-table)))
  ;(is (= (->> "short/f.edn" misc/string-from-resource edn/read-string) (->> "short/f.xml" misc/string-from-resource StringReader. xml/parse common/xml-to-csv-table)))
  ;;(is (= (->> "short/g.edn" misc/string-from-resource edn/read-string) (->> "short/g.xml" misc/string-from-resource StringReader. xml/parse common/xml-to-csv-table)))
  (is (= (->> "short/h.edn" misc/string-from-resource edn/read-string) (->> "short/h.xml" misc/string-from-resource StringReader. xml/parse common/xml-to-csv-table)))
  (is (= 4 (time (count (:rows (->> sample-simple-xml StringReader. xml/parse common/xml-to-csv-table))))))
  (is (= (->> "sample-simple.table.edn" misc/string-from-resource edn/read-string) (->> sample-simple-xml StringReader. xml/parse common/xml-to-csv-table)))
  (is (= 500 (time (count (:rows (->> sample-trapeze-xml StringReader. xml/parse common/xml-to-csv-table)))))))

(deftest xml-to-csv-test
  (is (= "a/b;a/c\nfoo;boo") (time (-> "<a><b>foo</b><c>boo</c></a>" StringReader. (common/xml-to-csv ";")))) "xml to csv")

(deftest simple-xml-to-csv-test
  (is (= (string/split-lines (misc/string-from-resource "sample-simple.csv")) (time (-> sample-simple-xml StringReader. (common/xml-to-csv ";"))))) "sample-simple.xml to csv")

(deftest trapeze-xml-to-csv-test
  (is (= (string/split-lines (misc/string-from-resource "sample-trapeze.csv")) (time (-> sample-trapeze-xml StringReader. (common/xml-to-csv ";"))))) "sample-trapeze.xml to csv")
