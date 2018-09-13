(ns pl.tomaszgigiel.xml-to-csv.merging
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:use [clojure.set])
  (:gen-class))

(defn get-cols [row] (set (map :col row)))

(defn row? [l] (and (seq? l) (every? map? l)))
(defn list-of-rows? [l] (and (seq? l) (every? row? l)))
(defn list-of-list? [l] (and (seq? l) (every? list? l)))
(defn list-of-rows-and-maps? [coll] (and (seq? coll)(list-of-rows? (first coll))(every? map? (rest coll))))

(defn merge-horizontal? [cols row] (do (empty? (intersection cols (get-cols row)))))
(defn merge-vertical? [cols row] ((complement merge-horizontal?) cols row))

(defn merged-horizontal [a b] (for [x a y b] (flatten (list x y))))
(defn merged-vertical [a b] (reduce  (fn [coll row] (conj coll row)) a b))

(defn merged [coll]
  (cond
    (empty? coll) ()
    (row? coll) (coll)
    (list-of-rows? coll) coll
    (list-of-rows-and-maps? coll) (reduce merged-horizontal (first coll) (list (rest coll)))
    (and (seq? coll) (every? list-of-rows? coll)) (let [cc (get-cols (first (first coll)))]
                                                    (reduce (fn[a b] (cond
                                                                       (merge-horizontal? cc (first b)) (merged-horizontal a b)
                                                                       (merge-vertical? cc (first b)) (merged-vertical a b))) () coll))
    (list-of-list? coll) (merged (map merged coll))
    :defult coll))