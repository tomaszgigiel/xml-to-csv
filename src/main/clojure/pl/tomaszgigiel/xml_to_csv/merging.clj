(ns pl.tomaszgigiel.xml-to-csv.merging
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:use [clojure.set])
  (:gen-class))

(defn cols [x] (set (map :col x)))

(defn row? [l] (and (seq? l) (every? map? l)))
(defn list-of-rows? [l] (and (seq? l) (every? row? l)))
(defn list-of-list? [l] (and (seq? l) (every? list? l)))
(defn list-of-rows-and-maps? [coll] (and (seq? coll)(list-of-rows? (first coll))(every? map? (rest coll))))

(defn merge-horizontal? [cols x] (empty? (intersection cols (cols x))))
(defn merge-vertical? [cols x] ((complement merge-horizontal?) cols x))

(defn merged-horizontal [a b] (for [x a y b] (flatten (list x y))))
(defn merged-vertical [coll x] (conj coll x))

(defn merged [coll]
  (cond
    (empty? coll) ()
    (row? coll) (coll)
    (list-of-rows? coll) coll
    (list-of-list? coll) (merged (map merged coll))
    (list-of-rows-and-maps? coll) (reduce merged-horizontal (first coll) (list (rest coll)))

    (and (seq? coll) (every? list-of-rows? coll)) (let [cols (cols (first coll))]
                                                    (reduce (fn[c x] (cond
                                                                        (merge-horizontal? cols x) (merged-horizontal c x)
                                                                        (merge-vertical? cols x) (merged-vertical c x))) () coll))
    :defult coll))