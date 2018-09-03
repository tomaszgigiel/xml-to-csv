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
(->> "short/f.xml" misc/string-from-resource StringReader. xml/parse)

(def x (->> "short/f.xml" misc/string-from-resource StringReader. xml/parse))


#clojure.data.xml.Element{:tag :a, :attrs {}, :content (
#clojure.data.xml.Element{:tag :b, :attrs {}, :content (
#clojure.data.xml.Element{:tag :d, :attrs {}, :content ("bb")}
#clojure.data.xml.Element{:tag :e, :attrs {}, :content ("cc")})}
#clojure.data.xml.Element{:tag :b, :attrs {}, :content (
#clojure.data.xml.Element{:tag :c, :attrs {}, :content ("dd")}
#clojure.data.xml.Element{:tag :d, :attrs {}, :content ("ee")}
#clojure.data.xml.Element{:tag :e, :attrs {}, :content ("ff")})})}

;;{a nil ab nil abd "bb" abe "cc"}
;;{a nil ab nil abc "dd" abd "bb" abe "cc"}

(defn enrich [element column]
  (cond
    (string? element) {column element}
    (sequential? element) (const {(:tag element) nil} (map #(enrich % column) element))
    (map? element) (const {(str (:tag parent) (:tag element)) :content element} (enrich element (str (:tag parent) (:tag element))))
    :else nil))

(enrich x "")

;;a/b/d;a/b/e;a/b/c
;;bb;cc;nil
;;ee;ff;dd
