(ns pl.tomaszgigiel.xml-to-csv.clj-xpath
  (:require [clojure.string :as string])
  (:require [clojure.tools.logging :as log])
  (:require [clj-xpath.core :as xpath])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as this-common])
  (:gen-class))

;; https://github.com/clojure/clojure/blob/clojure-1.9.0/src/clj/clojure/core.clj#L4871
(defn path-text-seq
  [xml]
  (let [branch? (fn [n] (:node n))
        children (fn [n] (xpath/$x "./*" n))
        leaf? (fn [n] (empty? (children n)))
        root (first (xpath/$x "/*" xml))
        walk (fn walk [node path]
               (let [node-path (str path "/" (name (:tag node)))]
                 (lazy-seq (filter some?
                                   (conj (when (branch? node) (mapcat (fn [n] (walk n node-path)) (children node)))
                                         (when (leaf? node) {:path node-path :text (:text node)}))))))]
    (walk root nil)))

(defn xml-to-csv
  ([xml separator]
    (let [paths (fn [ps p] (if(this-common/in? ps p)ps (conj ps p)))
          cell-count (fn [r] (->> r (re-seq (re-pattern separator)) count))
          complete-row (fn [before idx] (if(> idx 0)(subs before 0 (+ (nth (this-common/indexes-of before separator 0) (- idx 1))1))(str "")))
          perform-row (fn [last cols path val] (if (> (.indexOf cols path) (cell-count last))
                                                 [(str last separator val)]
                                                 [last (str (complete-row last (.indexOf cols path)) val)]))
          perform-item (fn perform-item ([table cell] (perform-item (pop (:rows table))
                                                                    (peek (:rows table))
                                                                    (paths (:cols table) (:path cell)) 
                                                                    (:path cell)
                                                                    (:text cell)))
                         ([butlast last cols path val] {:cols cols :rows (into [] (concat butlast (perform-row last cols path val)))}))
          table (reduce perform-item 
                        {:cols [] :rows [""]}
                        (path-text-seq xml))]
      (into [(string/join separator (:cols table))] (subvec (:rows table) 1))))
  ([xml] (xml-to-csv xml ",")))
