(ns bild-ord.domain.words-test
  (:require [clojure.test :refer :all]
            [bild-ord.domain.words :refer :all]))

(deftest words-test
  (testing "Word groups"
    (is (= (list "sol" "ros" "vas" "ram" "sil") (words-for-group 0)))
    (is (= (list "resa" "niga" "lysa" "leka" "leta") (words-for-group 7)))
    (is (= (list "ram" "ris" "ros" "sal" "sil" "sol" "vas") (sort (options-for-group 0))))))
