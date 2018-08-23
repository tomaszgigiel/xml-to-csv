(ns pl.tomaszgigiel.xml-to-csv.common-test
  (:use [clojure.test])
  (:require [clojure.java.io :as io])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as this-common])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config]))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(deftest string-from-resource-test
  (is (.contains (this-common/string-from-resource "simple.xml") "<books>")) "string")

(deftest not-nil?-test
  (is (= false (this-common/not-nil? nil)) "nil")
  (is (= true (this-common/not-nil? "ok")) "not nil"))

(deftest indexes-of-test
  (is (= () (this-common/indexes-of "aa" "," 0)))
  (is (= () (this-common/indexes-of "aa" "," 3)))
  (is (= (list 2) (this-common/indexes-of "aa," "," 0)))
  (is (= () (this-common/indexes-of "aa," "," 3)))
  (is (= (list 2) (this-common/indexes-of "aa,bb" "," 0)))
  (is (= () (this-common/indexes-of "aa,bb" "," 3)))
  (is (= (list 2 5) (this-common/indexes-of "aa,bb," "," 0)))
  (is (= (list 5) (this-common/indexes-of "aa,bb," "," 3)))
  (is (= (list 2 5) (this-common/indexes-of "aa,bb,cc" "," 0)))
  (is (= (list 5) (this-common/indexes-of "aa,bb,cc" "," 3)))
  (is (= (list 3 7) (this-common/indexes-of "aaa,bbb,ccc" "," 0)))
  (is (= (list 3 7) (this-common/indexes-of "aaa,bbb,ccc" "," 3))))

(deftest in?-test
  (is (= true (this-common/in? [1 2 3] 1)))
  (is (= true (this-common/in? [1 2 3] 2)))
  (is (= nil (this-common/in? [1 2 3] 4))))
