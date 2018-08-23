(ns pl.tomaszgigiel.xml-to-csv.clj-xpath-test
  (:use [clojure.test])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clj-xpath.core :as xpath])
  (:require [pl.tomaszgigiel.xml-to-csv.clj-xpath :as this-xpath])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as this-common])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config]))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(def xml (this-common/string-from-resource "simple.xml"))

(deftest xpath-simple-test
  (is (= :books (xpath/$x:tag "/*" xml)) "get root node tag")
  (is (= (list :book :book) (xpath/$x:tag* "/books/book" xml)) "get list of tag nodes by path")
  (is (= () (xpath/$x:tag* "/non-existent/non-existent" xml)) "get empty list of tag nodes by completely non existent path")
  (is (= () (xpath/$x:tag* "/books/non-existent" xml)) "get empty list of tag nodes by partly non existent path")
  (is (= (list :author :author :author :author) (xpath/$x:tag* "/books/book/authors/author" xml)) "get list of tag nodes by path")
  (is (= (list :first-name :first-name :first-name :first-name) (xpath/$x:tag* "/books/book/authors/author/first-name" xml)) "get list of tag nodes by path")
  (is (= () (xpath/$x:tag* "/books/book/authors/author/first-name/*" xml)) "get empty list of tag nodes by too deep path")
  (is (= () (xpath/$x "/books/book/authors/author/first-name/*" xml))"get empty list of nodes by too deep path")
  (is (= 2 (count (xpath/$x "./*" (first (xpath/$x "/books/book/authors/author" xml))))) "first-name last-name")
  (is (= 0 (count (xpath/$x "./*" (first (xpath/$x "/books/book/authors/author/first-name" xml))))) "nothing, too deep path")
  (is (= () (xpath/$x "./*" (first (xpath/$x "/books/book/authors/author/first-name" xml)))) "nothing, too deep path")
  (is (= clojure.lang.LazySeq (class (xpath/$x "/*" xml))) "xpath result is LazySeq"))

(deftest xpath-advanced-test
  (is (= (list :title :first-name :last-name) (distinct (xpath/$x:tag* "//*[not(*)]" xml))) "all leaves tags")
  (is (= (list :title :first-name :last-name) (distinct (xpath/$x:tag* "//*[not(child::*)]" xml))) "all leaves tags")
  (is (= 37 (count (xpath/$x:text* "//node()[not(node())]" xml))) "all nodes text (leaves or not)")
  (is (some #(= "\n" %) (xpath/$x:text* "//node()[not(node())]" xml)) "all nodes text (leaves or not)")
  (is (= (list :first-name :last-name) (distinct (xpath/$x:tag* "//*[ancestor::author]" xml))) "all leaves tags for author branch")
  (is (= "Amit" (xpath/$x:text "/books/book[1]/authors[1]/author[1]/first-name[1]" xml)) "first name")
  (is (= (list :books :book :authors :author) (distinct (xpath/$x:tag* "//first-name/ancestor::*" xml))) "first name"))

(deftest xpath-leaf?-test
  (is (= true (this-xpath/xpath-leaf? (first (xpath/$x "/books/book/authors/author/first-name" xml)))) "no children")
  (is (= false (this-xpath/xpath-leaf? (first (xpath/$x "/books/book/authors/author" xml)))) "has children")
  (is (= false (this-xpath/xpath-leaf? (first (xpath/$x "/non-existent/non-existent" xml)))) "non existent path"))

(deftest tree-ancestry-seq-test
  (let [ms (this-xpath/tree-ancestry-seq (fn [n] (:node n))
                                         (fn [n] (xpath/$x "./*" n))
                                         (first (xpath/$x "/*" xml)))]
    (is (= 19 (->> xml (re-seq #"</") count) (count ms)) "nodes/tag count")))

(deftest xpath-ancestry-seq-test
  (is (= 19 (->> xml (re-seq #"</") count) (->> xml this-xpath/xpath-ancestry-seq count)) "nodes/tag count"))

(deftest xpath-ancestry-transformed-seq-test
  (let [node-tag (fn [n] (str "/" (name (:tag n))))
        node-tag-path (fn [ns] (string/join (map node-tag ns)))
        full-path (fn [m] (str (node-tag-path (:ancestors m)) (node-tag (:node m))))
        all-paths (distinct (this-xpath/xpath-ancestry-transformed-seq xml full-path))
        all-paths-from-resource (string/split-lines(this-common/string-from-resource "simple.all-paths.txt"))]
    (is (= all-paths-from-resource all-paths) "list of all distinct paths")))

(deftest path-text-pairs-test
  (is (= 10 (count (this-xpath/path-text-pairs xml))) "list path text pairs")
  (is (some #(= {:path "/books/book/title", :text "Clojure in Action"} %) (this-xpath/path-text-pairs xml)) "list path text pairs"))

(deftest xml-to-csv-test
  (is (= (string/split-lines (this-common/string-from-resource "simple.csv")) (this-xpath/xml-to-csv xml ";")) "simple xml to csv"))
