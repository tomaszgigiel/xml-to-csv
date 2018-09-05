(ns pl.tomaszgigiel.xml-to-csv.temp-test
  (:use [clojure.test])
  (:require [clojure.xml :as clojure-xml])
  (:require [clojure.java.io :as io])
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as string])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as common])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config])
  (:import java.io.StringReader))

(def clojure-x (->> "short/k.xml" io/resource str clojure-xml/parse))

{:tag :a, :attrs nil, :content [
{:tag :b, :attrs nil, :content [
{:tag :d, :attrs nil, :content ["bb"]}
{:tag :e, :attrs nil, :content ["cc"]}]}
{:tag :b, :attrs nil, :content [
{:tag :c, :attrs nil, :content ["dd"]}
{:tag :d, :attrs nil, :content ["ee"]}
{:tag :e, :attrs nil, :content ["ff"]}]}]}

{:tag :a, :attrs nil, :content [
{:tag :b, :attrs nil, :content ["aa"]}
{:tag :c, :attrs nil, :content ["bb"]}]}

(defn tree-to-rows [element path] 
  (let [new-path (str path "/" (name (:tag element)))
        new-content (:content element)]
    (cond
      (string? (first new-content)) {:col new-path :val (first new-content)}
      (sequential? new-content) (map #(tree-to-rows % new-path) new-content))))

(tree-to-rows clojure-x "")
;; ({:col "/a/b", :val "aa"} {:col "/a/c", :val "bb"})

(defn row-columns [row] (reduce (fn [columns r] (conj columns (:col r))) [] row))
(row-columns (list {:col "aa" :val "aa"} {:col "bb" :val "bb"}))
;; ["aa" "bb"]
(row-columns (list {:col "/a/b", :val "aa"} {:col "/a/c", :val "bb"}))
;; ["/a/b" "/a/c"]

(defn get-val
  [row column]
  (cond
    (empty? row) nil
    (= (:col (first row)) column) (:val (first row))
    :else (get-val (rest row) column)))
(get-val (list {:col "aa" :val "aa"} {:col "bb" :val "bb"}) "aa")
;; "aa"
(get-val (list {:col "/a/b", :val "aa"} {:col "/a/c", :val "bb"}) "/a/b")
;; "aa"

(defn row-vals [row columns] (map #(get-val row %) columns))
(row-vals (list {:col "aa", :val "aa"} {:col "bb", :val "bb"}) ["cc" "bb" "aa"])
;; (nil "bb" "aa")
(row-vals (list {:col "/a/b", :val "aa"} {:col "/a/c", :val "bb"}) ["/a/b" "/a/c"])
;; ("aa" "bb")

(defn table-columns [columns row-columns] (reduce (fn [cs c] (if (< (.indexOf cs c) 0) (conj cs c) cs)) columns row-columns))
(table-columns ["cc" "bb"] (row-columns (list {:col "aa", :val "aa"} {:col "bb", :val "bb"})))
;; ["cc" "bb" "aa"]
(table-columns [] (row-columns (list {:col "/a/b", :val "aa"} {:col "/a/c", :val "bb"})))
;; ["/a/b" "/a/c"]

(defn row-perform
  [table row]
  (let [row-columns (row-columns row)
        table-columns (table-columns (:cols table) row-columns)
        row-vals (row-vals row table-columns)]
    {:cols table-columns :rows (conj (:rows table) row-vals)}))
(row-perform {:cols ["cc" "bb"] :rows []} (list {:col "aa", :val "aa"} {:col "bb", :val "bb"}))
;; {:cols ["cc" "bb" "aa"], :rows [(nil "bb" "aa")]}
(row-perform {:cols ["/a/b" "/a/c"] :rows []} (list {:col "/a/b", :val "aa"} {:col "/a/c", :val "bb"}))
;; {:cols ["/a/b" "/a/c"], :rows [("aa" "bb")]}

(defn tree-to-table [element] (reduce row-perform {:cols [] :rows []} (tree-to-rows element "")))
(tree-to-table clojure-x)
;; {:cols ["/a/b/d" "/a/b/e" "/a/b/c"], :rows [("bb" "cc") ("ee" "ff" "dd")]}
