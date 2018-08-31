(ns pl.tomaszgigiel.xml-to-csv.temp-test
  (:use [clojure.test])
  (:require [clojure.data.xml :as xml])
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as string])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as common])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config])
  (:import java.io.StringReader))

(def sample-simple-xml (misc/string-from-resource "sample-simple.xml"))
(def sample-trapeze-xml (misc/string-from-resource "sample-trapeze.xml"))

(def cc (->> "short/c.xml" misc/string-from-resource StringReader. xml/parse))

(def cc-p (list #clojure.data.xml.Element
  {:tag :a
   :content (#clojure.data.xml.Element
              {:tag :b
               :content (#clojure.data.xml.Element
                          {:tag :c :content ("aa")}
                          #clojure.data.xml.Element
                          {:tag :d :content ("bb")}
                          #clojure.data.xml.Element
                          {:tag :e :content ("cc")}
                          )
               } 
              #clojure.data.xml.Element
              {:tag :b
               :content (#clojure.data.xml.Element
                          {:tag :d :content ("ee")}
                          #clojure.data.xml.Element
                          {:tag :e :content ("ff")}
                          )
               }
              )
   }
))

(defn path-text-seq
  ([element] (flatten (path-text-seq element [])))
  ([element path] (cond
                    (string? element) {:path path :text element}
                    (sequential? element) (map #(path-text-seq % path) element)
                    (map? element) (path-text-seq (:content element) (conj path (:tag element)))
                    :else nil)))


(path-text-seq cc-p)
