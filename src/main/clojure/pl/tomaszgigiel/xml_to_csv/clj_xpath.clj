(ns pl.tomaszgigiel.xml-to-csv.clj-xpath
  (:require [clojure.string :as string])
  (:require [clojure.tools.logging :as log])
  (:require [clj-xpath.core :as xpath])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as this-common])
  (:gen-class))

(defn xpath-leaf?
  [n]
  (empty? (xpath/$x "./*" n)))

(defn tree-ancestry-seq
  [branch? children root]
  (let [walk
        (fn walk [node ancestors] 
          (lazy-seq 
            (conj (when (branch? node) (mapcat (fn [n] (walk n (conj ancestors node)))(children node)))
                  {:node node :ancestors ancestors})))]
    (walk root [])))

(defn xpath-ancestry-seq
  [xml]
  (tree-ancestry-seq (fn [n] (:node n))            ; branch?: nil or node
                     (fn [n] (xpath/$x "./*" n))   ; children: "./*" - children of current
                     (first (xpath/$x "/*" xml)))) ; root: "/*" - top

;; disadvantages: traversing twice, tree, list
(defn xpath-ancestry-transformed-seq
  [xml f]
  (map f (xpath-ancestry-seq xml)))

(defn path-text-pairs
  [xml]
  (let [node-tag (fn [n] (str "/" (name (:tag n))))
        node-tag-path (fn [ns] (string/join (map node-tag ns)))
        full-path (fn [m] (str (node-tag-path (:ancestors m)) (node-tag (:node m))))
        node-text (fn [m] (:text (:node m)))
        path-text-pair (fn [m] (when (xpath-leaf? m) {:path (full-path m) :text (node-text m)}))]
    (filter some? (xpath-ancestry-transformed-seq xml path-text-pair))))

(defn xml-to-csv
  ([xml separator]
    (let [paths (fn [ps p] (if(this-common/in? ps p)ps (conj ps p)))
          cell-count (fn [r] (->> r (re-seq (re-pattern separator)) count))
          complete-row (fn [before idx] (if(> idx 0)(subs before 0 (+ (nth (this-common/indexes-of before separator 0) (- idx 1))1))(str "")))
          perform-row (fn [last cols path val] (if (> (.indexOf cols path) (cell-count last))
                                                 [(str last separator val)]
                                                 [last (str (complete-row last (.indexOf cols path)) val)]))
          perform-item (fn perform-item ([table cell] (perform-item (pop (:rows table))
                                                                    (last (:rows table))
                                                                    (paths (:cols table) (:path cell)) 
                                                                    (:path cell)
                                                                    (:text cell)))
                         ([butlast last cols path val] {:cols cols :rows (into [] (concat butlast (perform-row last cols path val)))}))]
      (:rows (reduce
               perform-item
               {:cols [""] :rows [""]}
               (path-text-pairs xml)))))
  ([xml] (xml-to-csv xml ",")))
