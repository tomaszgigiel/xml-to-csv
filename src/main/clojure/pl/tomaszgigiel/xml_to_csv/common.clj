(ns pl.tomaszgigiel.xml-to-csv.common
  (:require [clojure.data.xml :as xml])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.tools.logging :as log])
  (:import java.io.StringReader)
  (:gen-class))

(defn string-from-resource
  [r]
  (slurp (io/resource r)))

(def not-nil? (complement nil?))

(defn indexes-of
  [s value from-index]
  (let [i (string/index-of s value from-index)]
    (if(not-nil? i)(conj (indexes-of s value (+ i 1)) i)())))

(defn in? 
  "true if coll contains elem and nil otherwise"
  [coll elm]
  (some #(= elm %) coll))

(defn empties [n] (if (> n 0) (conj (empties (- n 1)) []) []))

(defn path-text-seq
  "i.e. (->> \"<a><b>foo</b></a>\" StringReader. clojure.data.xml/parse path-text-seq)
  ({:path [:a :b], :element \"foo\"})"
  ([element] (flatten (path-text-seq element [])))
  ([element path] (cond
                    (string? element) {:path path :text element}
                    (sequential? element) (map #(path-text-seq % path) element)
                    (map? element) (path-text-seq (:content element) (conj path (:tag element)))
                    :else nil)))

(defn xml-to-csv
  [element]
  (let [paths (fn [ps p] (if(this-common/in? ps p)ps (conj ps p)))
        complete-row (fn [before idx] (if(> idx 0)(subs before 0 (+ (nth (this-common/indexes-of before separator 0) (- idx 1))1))(str "")))

        perform-row (fn [cols last path text] (let [col-idx (.indexOf cols path)
                                                    last-size (count last)]
                                                (cond
                                                  (= col-idx)
                                                  (= idx 0) [(conj last text)]
                                                  (< idx 0) [last (into (subvec last 0 (- idx)) text)]
                                                  (> idx 0) (into last (empties idx) text)))
                                                
                                                
                                                
                                                if (= (.indexOf cols path) (count last))
                                           [(conj last text)]
                                           [last ((subvec last 0 (- (.indexOf cols path) (count last))))        (str (complete-row last (.indexOf cols path)) val)]))
        
        
        
        perform-item (fn perform-item ([table item] (let [cols (paths (:cols table) (:path item))
                                                          butlast (pop (:rows table))
                                                          last (peek (:rows table))
                                                          path (:path item)
                                                          text (:text item)
                                                          last-new (perform-row cols last path text)]
                                                      {:cols cols :rows (into butlast last-new)})))
        table (reduce perform-item {:cols [] :rows []} (path-text-seq element))]
    table))

(def sample-simple-xml (string-from-resource "sample-simple.xml"))
(def sample-trapeze-xml (string-from-resource "sample-trapeze.xml"))

(->> "<a><b>foo</b></a>" StringReader. clojure.data.xml/parse path-text-seq)
(path-text-seq (xml/parse (StringReader. sample-simple-xml)))
(xml-to-csv (xml/parse (StringReader. sample-simple-xml)))

(into [:a :b] [:c :d])