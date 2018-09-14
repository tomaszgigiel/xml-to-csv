(ns pl.tomaszgigiel.xml-to-csv.merging-test
  (:use [clojure.test])
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [clojure.xml :as clojure-xml])
  (:require [pl.tomaszgigiel.xml-to-csv.merging :as merging])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config]))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(deftest merged-horizontal-test
  (is ( = '(({1 1} {2 2} {3 3})) (merging/merged-horizontal '({1 1} {2 2}) {3 3})))
  (is ( = '(({1 1} {2 2} {3 3})) (merging/merged-horizontal '(({1 1} {2 2})) {3 3})))
  (is ( = '(({1 1} {2 2} {3 3}) ({11 11}{22 22}{3 3})) (merging/merged-horizontal '(({1 1} {2 2}) ({11 11} {22 22})) {3 3}))))

(deftest merged-vertical-test
  (is ( = '(({3 3}) ({1 1}{2 2})) (merging/merged-vertical '({1 1} {2 2}) {3 3})))
  (is ( = '(({3 3}) ({1 1}{2 2})) (merging/merged-vertical '(({1 1} {2 2})) {3 3})))
  (is ( = '(({3 3}) ({1 1} {2 2}) ({11 11} {22 22})) (merging/merged-vertical '(({1 1} {2 2}) ({11 11} {22 22})) {3 3}))))

(let [a01 (->> "a01/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a02 (->> "a02/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a03 (->> "a03/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a04 (->> "a04/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a05 (->> "a05/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a06 (->> "a06/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a07 (->> "a07/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a08 (->> "a08/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a09 (->> "a09/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a10 (->> "a10/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a11 (->> "a11/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a12 (->> "a12/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a13 (->> "a13/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)
      a14 (->> "a14/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string)]

  (deftest merged-test
    (is (= (->> "a01/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a01)))
    (is (= (->> "a02/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a02)))
    (is (= (->> "a03/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a03)))
    (is (= (->> "a04/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a04)))
    (is (= (->> "a05/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a05)))
    (is (= (->> "a06/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a06)))
    (is (= (->> "a07/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a07)))
    (is (= (->> "a08/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a08)))
    (is (= (->> "a09/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a09)))
    (is (= (->> "a10/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a10)))
    (is (= (->> "a11/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a11)))
    (is (= (->> "a12/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a12)))
    (is (= (->> "a13/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a13)))
    (is (= (->> "a14/merged.edn" misc/string-from-resource edn/read-string) (merging/merged a14)))
   )
)