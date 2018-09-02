(ns pl.tomaszgigiel.xml-to-csv.temp-test
  (:use [clojure.test])
  (:require [clojure.data.xml :as xml])
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as string])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as common])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config])
  (:import java.io.StringReader))

(defn path-text-seq
  ([element] (flatten (path-text-seq element [] [])))
  ([element path pre] (cond
                     (string? element) {:path path :text element :pre pre}
                     (sequential? element) (map #(path-text-seq % path pre) element)
                     (map? element) (path-text-seq (:content element) (conj path (:tag element)) (conj pre element))
                     :else nil)))


(->> "short/f.xml" misc/string-from-resource StringReader. xml/parse path-text-seq)
