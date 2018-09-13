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

(def l (->> "l/tree-to-rows-helper.edn" misc/string-from-resource edn/read-string))

(defn cols [x] (set (map :col x)))

(defn row? [l] (and (seq? l) (every? map? l)))
(defn list-of-rows? [l] (and (seq? l) (every? row? l)))
(defn list-of-list? [l] (and (seq? l) (every? list? l)))
(defn list-of-rows-and-maps? [coll] (and (seq? coll)(list-of-rows? (first coll))(every? map? (rest coll))))

(defn merge-horizontal? [cols x] (empty? (intersection cols (cols x))))
(defn merge-vertical? [cols x] ((complement merge-horizontal?) cols x))

(defn merged-horizontal [a b] (for [x a y b] (flatten (list x y))))
(defn merged-vertical [a b] (reduce  (fn [coll row] (conj coll row)) a b))

(defn merged [coll]
  (cond
    (empty? coll) ()
    (row? coll) (coll)
    (list-of-rows? coll) coll
    (list-of-rows-and-maps? coll) (reduce merged-horizontal (first coll) (list (rest coll)))
    (and (seq? coll) (every? list-of-rows? coll)) (let [cols (cols (first coll))]
                                                    (reduce (fn[c x] (merged-vertical c x)) () coll))
    (list-of-list? coll) (merged (map merged coll))
    :defult coll))

(merged l)




(defn merged [coll]
  (cond
    (empty? coll) ()
    (row? coll) (coll)
    (list-of-rows? coll) coll
    (list-of-rows-and-maps? coll) (reduce merged-horizontal (first coll) (list (rest coll)))
    (and (seq? coll) (every? list-of-rows? coll)) (let [cols (cols (first coll))]
                                                  (reduce (fn[c x] (cond
                                                                      (merge-horizontal? cols x) (merged-horizontal c x)
                                                                      (merge-vertical? cols x) (merged-vertical c x))) () coll))
    (list-of-list? coll) (merged (map merged coll))
    :defult coll))

