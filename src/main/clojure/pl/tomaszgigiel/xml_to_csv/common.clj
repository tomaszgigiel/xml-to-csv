(ns pl.tomaszgigiel.xml-to-csv.common
  (:require [clojure.data.xml :as xml])
  (:require [clojure.string :as string])
  (:require [clojure.tools.logging :as log])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:gen-class))

(defn path-text-seq
  ([element] (flatten (path-text-seq element [])))
  ([element path] (cond
                    (string? element) {:path path :text element}
                    (sequential? element) (map #(path-text-seq % path) element)
                    (map? element) (path-text-seq (:content element) (conj path (:tag element)))
                    :else nil)))

(defn xml-to-csv-table
  [element]
  (let [perform-columns (fn [cs c] (if (< (.indexOf cs c) 0) (conj cs c) cs))

        complete (fn [last start end path cols] (into [] (map-indexed (fn [idx x] (if (<(count (nth cols idx))(count path)) x nil)) (subvec last start end))))

        perform-row (fn [cols last path text] (let [idx-path (.indexOf cols path)
                                                    idx-cell (count last)]
                                                (cond
                                                  (= idx-path idx-cell) [(conj last text)]
                                                  (< idx-path idx-cell) [last (conj (complete last 0 idx-path path cols) text)]
                                                  (> idx-path idx-cell) (flatten (conj last (vec(replicate (- idx-cell idx-path) nil)) text)))))

        perform-item (fn perform-item ([table item] (let [cols (perform-columns (:cols table) (:path item))
                                                          butlast (pop (:rows table))
                                                          last (peek (:rows table))
                                                          path (:path item)
                                                          text (:text item)
                                                          last-new (perform-row cols last path text)]
                                                      {:cols cols :rows (into butlast last-new)})))
        table (reduce perform-item {:cols [] :rows [[]]} (path-text-seq element))]
    table))

(defn xml-to-csv
  [s separator]
  (let [row-to-line (fn [r] (string/join separator (map #(if (nil? %) "nil" %) r)))
        cols-to-row (fn [cs] (map (fn[v] (string/join "/" (map name v))) cs))
        table (->> s xml/parse xml-to-csv-table)
        cols (:cols table)
        rows (:rows table)]
    (cons (->> cols cols-to-row row-to-line) (map row-to-line rows))))
