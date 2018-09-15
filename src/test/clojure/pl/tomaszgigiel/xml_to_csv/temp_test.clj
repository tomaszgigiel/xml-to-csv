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

(def a09 (-> "a09/input.xml" io/resource str clojure-xml/parse (common/tree-to-rows-helper "")))

(defn get-cols [row] (set (map :col row)))

(defn row? [coll] (and (seq? coll) (every? map? coll)))
(defn rows? [coll] (and (seq? coll) (every? row? coll)))
(defn list-of-rows? [coll] (and (seq? coll) (every? rows? coll)))
(defn list-of-row-or-map? [coll] (and (seq? coll) (every? (fn [x] (or (row? x)(map? x))) coll)))
(defn list-of-rows-or-map? [coll] (and (seq? coll) (every? (fn [x] (or (rows? x)(map? x))) coll)))
(defn list-of-list? [coll] (and (seq? coll) (every? seq? coll)))

(defn to-list-row [a]
  (cond
    (rows? a) a
    (row? a) (list a)
    (map? a) (list (list a))))

(defn merge-horizontal? [cols row] (empty? (intersection cols (get-cols row))))
(defn merge-vertical? [cols row] ((complement merge-horizontal?) cols row))

(defn merged-horizontal [a b] (for [x (to-list-row a) y (to-list-row b)] (flatten (list x y))))
(defn merged-vertical [a b] (reduce (fn [coll row] (conj coll row)) (to-list-row a) (to-list-row b)))

(defn merged-list-of-rows [coll] (let [h (first coll)
                                       t (rest coll)
                                       cols (get-cols h)]
                                   (reduce (fn[a b] (if (merge-horizontal? cols b) (merged-horizontal a b) (merged-vertical a b))) h t)))

(defn merged-row-or-map [coll] (let [rs (filter row? coll)
                                     ms (filter map? coll)]
                                 (merged-horizontal rs ms)))

(defn merged-rows-or-map [coll] (let [rs (first (filter rows? coll))
                                      ms (filter map? coll)]
                                  (merged-horizontal rs ms)))

(defn merged [coll]
  (cond
    (empty? coll) ()
    (row? coll) (list coll)
    (rows? coll) coll
    (list-of-rows? coll) (merged-list-of-rows coll)
    (list-of-row-or-map? coll) (merged-row-or-map coll)
    (list-of-rows-or-map? coll) (merged (merged-rows-or-map coll))
    (list-of-list? coll) (merged (map merged coll))
    :defult "unsupported"))


(merged a09)


;;;;


(every? map? '({:col "/aa/bb/a", :val "a1"} {:col "/aa/bb/b", :val "b1"} {:col "/aa/bb/c", :val "c1"}))

(defn row? [coll] (and (println coll) (seq? coll) (every? map? coll)))
(defn rows? [coll] (and (seq? coll) (every? row? coll)))
(rows? a09)


(rows?
  '(
     ({:col "/aa/bb/a", :val "a1"} {:col "/aa/bb/b", :val "b1"} {:col "/aa/bb/c", :val "c1"})
     ({:col "/aa/bb/a", :val "a2"} {:col "/aa/bb/b", :val "b2"} {:col "/aa/bb/c", :val "c2"})
     {:col "/aa/d", :val "d1"}
   )
)




