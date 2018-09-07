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
