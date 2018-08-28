(ns pl.tomaszgigiel.xml-to-csv.misc
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:gen-class))

(defn string-from-resource [r] (slurp (io/resource r)))

(def not-nil? (complement nil?))

(defn indexes-of
  [s value from-index]
  (let [i (string/index-of s value from-index)]
    (if(not-nil? i)(conj (indexes-of s value (+ i 1)) i)())))

(defn in?
  "true if coll contains elem and nil otherwise"
  [coll elm]
  (some #(= elm %) coll))

(defn empties [n] (if (> n 0) (conj (empties (- n 1)) []) []))
