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

(defn merged-horizontal [coll x] (map #(conj coll %) x))
(defn merged-vertical [coll x] (conj coll x))

(defn merged [coll]
  (cond
    (empty? coll) ()
    (row? coll) (coll)
    (list-of-rows? coll) coll
    (list-of-list? coll) (merged (map merged coll))
    (list-of-rows-and-maps? coll) (reduce merged-horizontal (first coll) (rest coll))

    (and (seq? coll) (every? list-of-rows? coll)) (let [cols (cols (first coll))]
                                                    (reduce (fn[c x] (cond
                                                                        (merge-horizontal? cols x) (merged-horizontal c x)
                                                                        (merge-vertical? cols x) (merged-vertical c x))) () coll))
    :defult coll))

(def l '(
      (
        ({:col "a" :val "a11"} {:col "b" :val "b11"} {:col "c" :val "c11"})
        ({:col "a" :val "a12"} {:col "b" :val "b12"} {:col "c" :val "c12"})
        ({:col "a" :val "a13"} {:col "b" :val "b13"} {:col "c" :val "c13"})
      )
      {:col "d" :val "d1"}
    )
  )
(merged l)

;;;
(defn merged-ab [a b]
  (cond
    (empty? a) b
    (empty? b) a
    (and (list-of-rows? a) (list-of-rows? b)) (let [cols (cols (first a))] (reduce (fn[a x] (cond
                                                                                              (merge-horizontal? cols x) (merged-horizontal a x)
                                                                                              (merge-vertical? cols x) (merged-vertical a x))) a b))
    :default "?"))

(defn core? [l x]
  (let [a (cols (first l))
        b (cols x)]  
    (cond
      (empty? l) true
      (empty? (intersection a b)) false
      :default true)))

(defn to-core [l] (filter #(core? l %) l))
(defn to-merge [l] (filter #((complement core?) l %) l))

(defn merged-2 [l]
  (let [c (to-core l)
        m (to-merge l)]
    (map #(flatten (conj m %)) c)))


(row? '({:col 1 :val 1} {:col 2 :val 2}))
(list-of-rows? '(({:col 1 :val 1} {:col 2 :val 2})({:col 1 :val 3} {:col 2 :val 4})))
