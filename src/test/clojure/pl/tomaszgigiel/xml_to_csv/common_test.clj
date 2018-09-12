(ns pl.tomaszgigiel.xml-to-csv.common-test
  (:use [clojure.test])
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:require [clojure.xml :as clojure-xml])
  (:require [pl.tomaszgigiel.xml-to-csv.common :as common])
  (:require [pl.tomaszgigiel.xml-to-csv.misc :as misc])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config]))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(comment

(let [a-element (->> "short/a.xml" io/resource str clojure-xml/parse)
      b-element (->> "short/b.xml" io/resource str clojure-xml/parse)
      c-element (->> "short/c.xml" io/resource str clojure-xml/parse)
      d-element (->> "short/d.xml" io/resource str clojure-xml/parse)
      e-element (->> "short/e.xml" io/resource str clojure-xml/parse)
      f-element (->> "short/f.xml" io/resource str clojure-xml/parse)
      g-element (->> "short/g.xml" io/resource str clojure-xml/parse)
      h-element (->> "short/h.xml" io/resource str clojure-xml/parse)
      i-element (->> "short/i.xml" io/resource str clojure-xml/parse)
      j-element (->> "short/j.xml" io/resource str clojure-xml/parse)
      k-element (->> "short/k.xml" io/resource str clojure-xml/parse)
      l-element (->> "short/l.xml" io/resource str clojure-xml/parse)]

  (println (common/tree-to-rows-helper l-element ""))
  
  (deftest tree-to-rows-test
   (is (= (->> "short/a-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a-element)))
   (is (= (->> "short/b-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows b-element)))
   (is (= (->> "short/c-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows c-element)))
   (is (= (->> "short/d-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows d-element)))
   (is (= (->> "short/e-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows e-element)))
   (is (= (->> "short/f-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows f-element)))
   (is (= (->> "short/g-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows g-element)))
   (is (= (->> "short/h-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows h-element)))
   (is (= (->> "short/i-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows i-element)))
   (is (= (->> "short/j-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows j-element)))
   (is (= (->> "short/k-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows k-element)))
   (is (= (->> "short/l-tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows l-element)))
   )

  ;;(deftest tree-to-table-test
    ;;(is (= (->> "short/a-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table a-element)))
    ;;(is (= (->> "short/b-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table b-element)))
    ;;(is (= (->> "short/c-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table c-element)))
    ;;(is (= (->> "short/d-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table d-element)))
    ;;(is (= (->> "short/e-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table e-element)))
    ;;(is (= (->> "short/f-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table f-element)))
    ;;(is (= (->> "short/g-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table g-element)))
    ;;(is (= (->> "short/h-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table h-element)))
    ;;(is (= (->> "short/i-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table i-element)))
    ;;(is (= (->> "short/j-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table j-element)))
    ;;(is (= (->> "short/k-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table k-element))))
)
)