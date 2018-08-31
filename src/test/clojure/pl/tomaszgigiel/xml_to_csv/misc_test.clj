(ns pl.tomaszgigiel.xml-to-csv.misc-test
  (:use [clojure.test])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config]))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(deftest string-from-resource-test
  (is (.contains (misc/string-from-resource "sample-simple.xml") "<books>")) "string")

(deftest not-nil?-test
  (is (= false (misc/not-nil? nil)) "nil")
  (is (= true (misc/not-nil? "ok")) "not nil"))

(deftest indexes-of-test
  (is (= () (misc/indexes-of "aa" "," 0)))
  (is (= () (misc/indexes-of "aa" "," 3)))
  (is (= (list 2) (misc/indexes-of "aa," "," 0)))
  (is (= () (misc/indexes-of "aa," "," 3)))
  (is (= (list 2) (misc/indexes-of "aa,bb" "," 0)))
  (is (= () (misc/indexes-of "aa,bb" "," 3)))
  (is (= (list 2 5) (misc/indexes-of "aa,bb," "," 0)))
  (is (= (list 5) (misc/indexes-of "aa,bb," "," 3)))
  (is (= (list 2 5) (misc/indexes-of "aa,bb,cc" "," 0)))
  (is (= (list 5) (misc/indexes-of "aa,bb,cc" "," 3)))
  (is (= (list 3 7) (misc/indexes-of "aaa,bbb,ccc" "," 0)))
  (is (= (list 3 7) (misc/indexes-of "aaa,bbb,ccc" "," 3))))

(deftest in?-test
  (is (= true (misc/in? [1 2 3] 1)))
  (is (= true (misc/in? [1 2 3] 2)))
  (is (= nil (misc/in? [1 2 3] 4))))
