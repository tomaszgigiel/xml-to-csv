(ns pl.tomaszgigiel.xml-to-csv.temp-test
  (:use [clojure.test])
  (:require [clojure.data.xml :as xml])
  (:require [clojure.xml :as clojure-xml])
  (:require [clojure.java.io :as io])
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

(def clojure-x (->> "short/f.xml" io/resource str clojure-xml/parse))

{:tag :a, :attrs nil, :content [
{:tag :b, :attrs nil, :content [
{:tag :d, :attrs nil, :content ["bb"]}
{:tag :e, :attrs nil, :content ["cc"]}]}
{:tag :b, :attrs nil, :content [
{:tag :c, :attrs nil, :content ["dd"]}
{:tag :d, :attrs nil, :content ["ee"]}
{:tag :e, :attrs nil, :content ["ff"]}]}]}

;;{a nil ab nil abd "bb" abe "cc"}
;;{a nil ab nil abc "dd" abd "bb" abe "cc"}

;; element is a map
;; element is a list
;; element is a string

;; {a ?}
;; {ab ?} {ab ?}
;; {a {b ?} {b ?}}

(def xx [":a" [":b" [":d" "bb" ":e" "cc"]] [":b" [":c" "dd" ":d" "ee" ":e" "ff"]]])
(def xx [":a" "aa"])

(def xx {":a" [{":b" [{":d" "bb"} {":e" "cc"}]} {":b" [{":c" "dd"} {":d" "ee"} {":e" "ff"}]}]})
;; (([":b:a:d" "bb"] [":b:a:e" "cc"]) ([":b:a:c" "dd"] [":b:a:d" "ee"] [":b:a:e" "ff"]))
(def xx {":a" "aa"})
(def xx {":a" [{":b" "bb"} {":c" "cc"}]})

(defn fun-xx [element path]   (cond     (and (map? element) (string? (first (vals element)))) [(str path (first (keys element))) (first (vals element))]
    (and (map? element) (sequential? (first (vals element)))) (map #(fun-xx % (str (first (keys element)) path)) (first (vals element)))))

(fun-xx xx "")
(vals xx)


(defn fun-xx [element path]   (cond     (sequential? element) (map #(fun-xx % (str (first element) path)) (rest element))
    (cond
      (string? (first element))
      )
        ))




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
