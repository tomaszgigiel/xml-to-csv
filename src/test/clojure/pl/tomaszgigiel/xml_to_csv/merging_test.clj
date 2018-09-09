(ns pl.tomaszgigiel.xml-to-csv.merging-test
  (:use [clojure.test])
  (:require [pl.tomaszgigiel.xml-to-csv.merging :as merging])
  (:require [pl.tomaszgigiel.xml-to-csv.test-config :as test-config]))

(use-fixtures :once test-config/once-fixture)
(use-fixtures :each test-config/each-fixture)

(let [a '(({:col 1 :val "a1"} {:col 2 :val "a2"} {:col 3 :val "a3"}) {:col 4 :val "d1"})
      a-core '(({:col 1 :val "a1"} {:col 2 :val "a2"} {:col 3 :val "a3"}))
      a-merge '({:col 4 :val "d1"})
      a-merged '(({:col 1 :val "a1"} {:col 2 :val "a2"} {:col 3 :val "a3"} {:col 4 :val "d1"}))]

  (deftest a-test
    (is (= a-core (merging/to-core a)))
    (is (= a-merge (merging/to-merge a)))
    (is (= a-merged (merging/merged a))))
)

(let [b '(({:col 1 :val "a1"} {:col 2 :val "b"}))]

  (deftest b-test
    (is (= b (merging/to-core b)))
    (is (= () (merging/to-merge b)))
    (is (= b (merging/merged b))))
)

(let [c '(({:col 1 :val "a1"} {:col 2 :val "b1"} {:col 3 :val "c1"})
           ({:col 1 :val"a2"} {:col 2 :val "b2"} {:col 3 :val "c2"})
           {:col 4 :val "d1"}
           ({:col 5 :val "e1"} {:col 6 :val "f1"} {:col 7 :val "g1"})
           ({:col 5 :val "e2"} {:col 6 :val "f2"} {:col 7 :val "g2"}))
      c-core '(({:col 1 :val "a1"} {:col 2 :val "b1"} {:col 3 :val "c1"})
                ({:col 1 :val "a2"} {:col 2 :val "b2"} {:col 3 :val "c2"}))
      c-merge '({:col 4 :val "d1"}
                 ({:col 5 :val "e1"} {:col 6 :val "f1"} {:col 7 :val"g1"})
                 ({:col 5 :val "e2"} {:col 6 :val "f2"} {:col 7 :val"g2"}))
      c-merged '(({:col 1 :val "a1"} {:col 2 :val "b1"} {:col 3 :val "c1"} {:col 4 :val "d1"} {:col 5 :val "e1"} {:col 6 :val "f1"} {:col 7 :val "g1"} {:col 5 :val "e2"} {:col 6 :val "f2"} {:col 7 :val "g2"})
                  ({:col 1 :val "a2"} {:col 2 :val "b2"} {:col 3 :val "c2"} {:col 4 :val "d1"} {:col 5 :val "e1"} {:col 6 :val "f1"} {:col 7 :val "g1"} {:col 5 :val "e2"} {:col 6 :val "f2"} {:col 7 :val "g2"}))]

  (deftest c-test
    (is (= c-core (merging/to-core c)))
    (is (= c-merge (merging/to-merge c)))
    (is (= c-merged (merging/merged c))))
)
