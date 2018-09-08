(ns pl.tomaszgigiel.xml-to-csv.merging
  (:gen-class))

(defn merge? [l x]
  (cond
    (map? x) true
    :default false))

(defn to-merge [l] (filter #(merge? l %) l))
(defn to-core [l] (filter #((complement merge?) l %) l))

(defn merged [l]
  (let [c (to-core l)
        m (to-merge l)]
    (map #(flatten (conj m %)) c)))