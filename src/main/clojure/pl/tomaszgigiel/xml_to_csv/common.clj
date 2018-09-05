(ns pl.tomaszgigiel.xml-to-csv.common
  (:require [clojure.xml :as clojure-xml])
  (:require [clojure.string :as string])
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:gen-class))

(defn tree-to-rows [element path] 
  (let [new-path (str path "/" (name (:tag element)))
        new-content (:content element)]
    (cond
      (string? (first new-content)) {:col new-path :val (first new-content)}
      (sequential? new-content) (map #(tree-to-rows % new-path) new-content))))

(defn row-columns [row] (reduce (fn [columns r] (conj columns (:col r))) [] row))

(defn get-val
  [row column]
  (cond
    (empty? row) nil
    (= (:col (first row)) column) (:val (first row))
    :else (get-val (rest row) column)))

(defn table-columns [columns row-columns] (reduce (fn [cs c] (if (< (.indexOf cs c) 0) (conj cs c) cs)) columns row-columns))

(defn row-vals [row columns] (map #(get-val row %) columns))

(defn row-perform
  [table row]
  (let [row-columns (row-columns row)
        table-columns (table-columns (:cols table) row-columns)
        row-vals (row-vals row table-columns)]
    {:cols table-columns :rows (conj (:rows table) row-vals)}))

(defn tree-to-table [element] (reduce row-perform {:cols [] :rows []} (tree-to-rows element "")))

(defn xml-to-csv
  [s separator])
