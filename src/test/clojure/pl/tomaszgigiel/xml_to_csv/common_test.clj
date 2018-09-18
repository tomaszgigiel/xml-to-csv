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

(let [a01 (->> "a01/input.xml" io/resource str clojure-xml/parse)
      a02 (->> "a02/input.xml" io/resource str clojure-xml/parse)
      a03 (->> "a03/input.xml" io/resource str clojure-xml/parse)
      a04 (->> "a04/input.xml" io/resource str clojure-xml/parse)
      a05 (->> "a05/input.xml" io/resource str clojure-xml/parse)
      a06 (->> "a06/input.xml" io/resource str clojure-xml/parse)
      a07 (->> "a07/input.xml" io/resource str clojure-xml/parse)
      a08 (->> "a08/input.xml" io/resource str clojure-xml/parse)
      a09 (->> "a09/input.xml" io/resource str clojure-xml/parse)
      a10 (->> "a10/input.xml" io/resource str clojure-xml/parse)
      a11 (->> "a11/input.xml" io/resource str clojure-xml/parse)
      a12 (->> "a12/input.xml" io/resource str clojure-xml/parse)
      a13 (->> "a13/input.xml" io/resource str clojure-xml/parse)
      a14 (->> "a14/input.xml" io/resource str clojure-xml/parse)
      a15 (->> "a15/input.xml" io/resource str clojure-xml/parse)]

  (deftest tree-to-rows-test
   (is (= (->> "a01/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a01)))
   (is (= (->> "a02/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a02)))
   (is (= (->> "a03/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a03)))
   (is (= (->> "a04/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a04)))
   (is (= (->> "a05/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a05)))
   (is (= (->> "a06/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a06)))
   (is (= (->> "a07/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a07)))
   (is (= (->> "a08/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a08)))
   (is (= (->> "a09/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a09)))
   (is (= (->> "a10/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a10)))
   (is (= (->> "a11/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a11)))
   (is (= (->> "a12/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a12)))
   (is (= (->> "a13/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a13)))
   ;;(is (= (->> "a14/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a14)))
   (is (= (->> "a15/tree-to-rows.edn" misc/string-from-resource edn/read-string) (common/tree-to-rows a15)))
   )

  ;;(deftest tree-to-table-test
    ;;(is (= (->> "short/a-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table a)))
    ;;(is (= (->> "short/b-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table b)))
    ;;(is (= (->> "short/c-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table c)))
    ;;(is (= (->> "short/d-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table d)))
    ;;(is (= (->> "short/e-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table e)))
    ;;(is (= (->> "short/f-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table f)))
    ;;(is (= (->> "short/g-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table g)))
    ;;(is (= (->> "short/h-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table h)))
    ;;(is (= (->> "short/i-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table i)))
    ;;(is (= (->> "short/j-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table j)))
    ;;(is (= (->> "short/k-tree-to-table.edn" misc/string-from-resource edn/read-string) (common/tree-to-table k))))
)