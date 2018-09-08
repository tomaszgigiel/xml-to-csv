(ns pl.tomaszgigiel.xml-to-csv.merging-test
  (:use [clojure.test])
  (:require [pl.tomaszgigiel.xml-to-csv.merging :as merging])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config]))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(let [a '(({1 "a1"}) ({2 "a2"}) ({3 "a3"}) {4 "d1"})
      a-core '(({1 "a1"}) ({2 "a2"}) ({3 "a3"}))
      a-merge '({4 "d1"})
      a-merged '(({1 "a1"} {4 "d1"}) ({2 "a2"} {4 "d1"}) ({3 "a3"} {4 "d1"}))]

(deftest merge?-test
  (is (= false (merging/merge? () ())))
  (is (= true (merging/merge? () {1 "a1"}))))

(deftest to-merge-test
 (is (= a-merge (merging/to-merge a))))

(deftest to-core-test
  (is (= a-core (merging/to-core a))))

(deftest merged-test
 (is (= a-merged (merging/merged a))))
)