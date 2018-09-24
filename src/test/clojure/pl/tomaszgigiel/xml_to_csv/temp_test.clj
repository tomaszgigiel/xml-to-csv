(ns pl.tomaszgigiel.xml-to-csv.temp-test
  (:use [clojure.set])
  (:use [clojure.test])
  (:require [clojure.data.xml :as data-xml])
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.xml :as clojure-xml])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as common])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config])
  (:import java.io.StringReader))

(def a13p (->> "a13/input.xml" io/resource str clojure-xml/parse))
(def a13 (-> "a13/input.xml" io/resource str clojure-xml/parse (common/tree-to-rows-helper "")))

(defn get-cols [row] (set (map :col row)))

(defn row? [coll] (and (seq? coll) (every? map? coll)))
(defn rows? [coll] (and (seq? coll) (every? row? coll)))
(defn list-of-rows? [coll] (and (seq? coll) (every? rows? coll)))
(defn list-of-row-or-map? [coll] (and (seq? coll) (every? (fn [x] (or (row? x)(map? x))) coll)))
(defn list-of-rows-or-map? [coll] (and (seq? coll) (every? (fn [x] (or (rows? x)(map? x))) coll)))
(defn list-of-list? [coll] (and (seq? coll) (every? seq? coll)))

(defn to-vector-row [a]
  (cond
    (rows? a) a
    (row? a) (vec a)
    (map? a) (vec (list a))
    (list-of-rows? a) (first a))) ;;first

(defn to-list-row [a]
  (cond
    (rows? a) a
    (row? a) (list a)
    (map? a) (list (list a))
    (list-of-rows? a) (first a))) ;;first

(defn merge-horizontal? [cols row] (empty? (intersection cols (get-cols row))))
(defn merge-vertical? [cols row] ((complement merge-horizontal?) cols row))

(defn merged-horizontal [a b] (cond
                                (empty? a) b
                                (empty? b) a
                                :default (for [x (to-list-row a) y (to-list-row b)] (flatten (list x y)))))
;;(defn merged-vertical [a b] (reduce (fn [coll row] (conj coll row)) (to-list-row a) (to-list-row b)))
(defn merged-vertical [a b] (reduce (fn [coll row] (conj row coll)) (to-list-row a) (to-list-row b)))

(defn merged-list-of-rows [coll] (let [h (first coll)
                                       t (rest coll)
                                       cols (get-cols h)]
                                   (reduce (fn[a b] (if (merge-horizontal? cols b) (merged-horizontal a b) (merged-vertical a b))) h t)))

(defn merged-row-or-map [coll] (let [splitted (split-with map? coll)
                                     starting-map (splitted 0)
                                     h (first (splitted 1))
                                     t (rest (splitted 1))
                                     cols (get-cols h)]
                                 (reduce (fn[a b] (if (merge-horizontal? cols b) (merged-horizontal a b) (merged-vertical a (merged-horizontal starting-map b)))) (merged-horizontal starting-map h) t)))

(defn merged-rows-or-map [coll] (let [splitted (split-with map? coll)
                                      starting-map (splitted 0)
                                      starting-rows ((split-with rows? (splitted 1)) 0)]
                                  (merged-horizontal starting-map starting-rows)))

(defn merged [coll]
  (cond
    (empty? coll) ()
    (row? coll) (list coll)
    (rows? coll) coll
    (list-of-rows? coll) (merged-list-of-rows coll)
    (list-of-row-or-map? coll) (merged-row-or-map coll)
    (list-of-rows-or-map? coll) (merged (merged-rows-or-map coll))
    (list-of-list? coll) (merged (map merged coll))
    ;;(list-of-list? coll) (map merged coll)
    :default "unsupported"))

(merged a13)
