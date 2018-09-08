(ns pl.tomaszgigiel.xml-to-csv.temp-test
  (:use [clojure.test])
  (:require [clojure.data.xml :as data-xml])
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.xml :as clojure-xml])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as common])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config])
  (:import java.io.StringReader))


(comment
(
  ({:col "/aa/bb/a" :val "a1"} {:col "/aa/bb/b" :val "b1"} {:col "/aa/bb/c" :val "c1"})
  ({:col "/aa/bb/a" :val "a2"} {:col "/aa/bb/b" :val "b2"} {:col "/aa/bb/c" :val "c2"})
  {:col "/aa/d" :val "d1"}
  {:col "/aa/d" :val "d2"}
  ({:col "/aa/bb/a" :val "a3"} {:col "/aa/bb/b" :val "b3"} {:col "/aa/bb/c" :val "c3"})
  {:col "/aa/d" :val "d3"}
  ({:col "/aa/bb/e" :val "e1"} {:col "/aa/bb/f" :val "f1"} {:col "/aa/bb/g" :val "g1"})
  ({:col "/aa/bb/e" :val "e2"} {:col "/aa/bb/f" :val "f2"} {:col "/aa/bb/g" :val "g2"})
  ({:col "/aa/bb/e" :val "e3"} {:col "/aa/bb/f" :val "f3"} {:col "/aa/bb/g" :val "g3"})
)

(defn merge-rows []
  ())

(def clojure-i (->> "short/i.xml" io/resource str clojure-xml/parse))

{
 :tag :aa, :attrs nil, :content [
                                 {:tag :bb, :attrs nil, :content [
                                                                  {:tag :a, :attrs nil, :content ["a1"]}
                                                                  {:tag :b, :attrs nil, :content ["b1"]}
                                                                  {:tag :c, :attrs nil, :content ["c1"]}
                                                                  ]
                                  }
                                 {:tag :bb, :attrs nil, :content [
                                                                  {:tag :a, :attrs nil, :content ["a2"]}
                                                                  {:tag :b, :attrs nil, :content ["b2"]}
                                                                  {:tag :c, :attrs nil, :content ["c2"]}
                                                                  ]
                                  }
                                 {:tag :d, :attrs nil, :content ["d1"]}
                                 ]
 }


(->> "short/i.xml" misc/string-from-resource data-xml/parse-str)

#clojure.data.xml.Element{:tag :aa, :attrs {}, :content (
                                                          #clojure.data.xml.Element{:tag :bb, :attrs {}, :content (
                                                                                                                    #clojure.data.xml.Element{:tag :a, :attrs {}, :content ("a1")}
                                                                                                                    #clojure.data.xml.Element{:tag :b, :attrs {}, :content ("b1")}
                                                                                                                    #clojure.data.xml.Element{:tag :c, :attrs {}, :content ("c1")}
                                                                                                                    )
                                                                                    }
                                                          #clojure.data.xml.Element{:tag :bb, :attrs {}, :content (
                                                                                                                    #clojure.data.xml.Element{:tag :a, :attrs {}, :content ("a2")}
                                                                                                                    #clojure.data.xml.Element{:tag :b, :attrs {}, :content ("b2")}
                                                                                                                    #clojure.data.xml.Element{:tag :c, :attrs {}, :content ("c2")}
                                                                                                                    )
                                                                                    }
                                                          #clojure.data.xml.Element{:tag :d, :attrs {}, :content ("d1")}
                                                          )
                          }

(def clojure-l (->> "short/l.xml" io/resource str clojure-xml/parse))

{:tag :aa, :attrs nil, :content [
                                 {:tag :bb, :attrs nil, :content [
                                                                  {:tag :cc, :attrs nil, :content [
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a1"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b1"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c1"]}]}
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a2"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b2"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c2"]}]}
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a3"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b3"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c3"]}]}]}
                                                                  {:tag :d, :attrs nil, :content ["d1"]}
                                                                  ]
                                  }
                                 {:tag :bb, :attrs nil, :content [
                                                                  {:tag :cc, :attrs nil, :content [
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a1"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b1"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c1"]}]}
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a2"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b2"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c2"]}]}
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a3"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b3"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c3"]}]}]}
                                                                  {:tag :d, :attrs nil, :content ["d1"]}]}
                                 {:tag :bb, :attrs nil, :content [
                                                                  {:tag :cc, :attrs nil, :content [
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a1"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b1"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c1"]}]}
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a2"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b2"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c2"]}]}
                                                                                                   {:tag :dd, :attrs nil, :content [
                                                                                                                                    {:tag :a, :attrs nil, :content ["a3"]}
                                                                                                                                    {:tag :b, :attrs nil, :content ["b3"]}
                                                                                                                                    {:tag :c, :attrs nil, :content ["c3"]}]}]}
                                                                  {:tag :d, :attrs nil, :content ["d1"]}
                                                                  ]
                                  }
                                 ]
 }

)