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


(def a13 (-> "a13/input.xml" io/resource str clojure-xml/parse (common/tree-to-rows-helper "")))

(defn get-cols [row] (set (map :col row)))

(defn row? [l] (and (seq? l) (every? map? l)))
(defn list-of-rows? [l] (and (seq? l) (every? row? l)))
(defn list-of-list? [l] (and (seq? l) (every? seq? l)))
(defn list-of-rows-and-maps? [coll] (and (seq? coll)(list-of-rows? (first coll))(every? map? (rest coll))))
(defn list-of-row-or-map? [coll] (and (seq? coll) (every? (fn [x] (or (row? x)(map? x))) coll)))

(defn to-list-row [a]
  (cond
    (list-of-rows? a) a
    (row? a) (list a)
    (map? a) (list (list a))))

(defn merge-horizontal? [cols row] (empty? (intersection cols (get-cols row))))
(defn merge-vertical? [cols row] ((complement merge-horizontal?) cols row))

(defn merged-horizontal [a b] (for [x (to-list-row a) y (to-list-row b)] (flatten (list x y))))
(defn merged-vertical [a b] (reduce  (fn [coll row] (conj coll row)) (to-list-row a) (to-list-row b)))

(defn merged [coll]
  (cond
    (empty? coll) ()
    (row? coll) (list coll)
    (list-of-rows? coll) coll
    (list-of-rows-and-maps? coll) (reduce merged-horizontal (first coll) (list (rest coll)))
    (list-of-row-or-map? coll) (let [cc (get-cols (first coll))]
                                 (reduce (fn[a b] (if (merge-horizontal? cc b) (merged-horizontal a b) (merged-vertical a b)))
                                         () coll))
    (and (seq? coll) (every? list-of-rows? coll)) (let [cc (get-cols (first (first coll)))]
                                                    (reduce (fn[a b] (cond
                                                                       (merge-horizontal? cc (first b)) (merged-horizontal a b)
                                                                       (merge-vertical? cc (first b)) (merged-vertical a b))) () coll))
    (list-of-list? coll) (merged (map merged coll))
    :defult "unknown"))

(merged a13)

