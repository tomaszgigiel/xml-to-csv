(ns pl.tomaszgigiel.xml-to-csv.common-test
  (:use [clojure.test])
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.xml :as clojure-xml])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as common])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config])
  (:import java.io.StringReader))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(def sample-simple-xml (misc/string-from-resource "sample-simple.xml"))
(def sample-trapeze-xml (misc/string-from-resource "sample-trapeze.xml"))

(deftest tree-to-rows-test
  (is (= (->> "short/a.edn" misc/string-from-resource edn/read-string) (-> "short/a.xml" io/resource str clojure-xml/parse (common/tree-to-rows "")))))

