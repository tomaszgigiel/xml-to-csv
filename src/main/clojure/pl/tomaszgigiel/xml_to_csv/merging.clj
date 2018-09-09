(ns pl.tomaszgigiel.xml-to-csv.merging
  (:use [clojure.set])
  (:gen-class))

(defn cols [x] (set (map :col x)))

(defn core? [l x]
  (let [a (cols (first l))
        b (cols x)]  
    (cond
      (empty? l) true
      (empty? (intersection a b)) false
      :default true)))

(defn to-core [l] (filter #(core? l %) l))
(defn to-merge [l] (filter #((complement core?) l %) l))

(defn merged [l]
  (let [c (to-core l)
        m (to-merge l)]
    (map #(flatten (conj m %)) c)))