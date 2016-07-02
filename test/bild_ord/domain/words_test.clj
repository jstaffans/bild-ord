(ns bild-ord.domain.words-test
  (:require [clojure.test :refer :all]
            [bild-ord.domain.words :refer :all]))

(deftest play-a-game-test
  (testing "right first time"
    (is (success?
         (-> (new-game ["lura"])
             (respond 0 "lura")))))
  (testing "incorrect guess"
    (is (not (success?
              (-> (new-game ["lura"])
                  (respond 0 "sila"))))))
  (testing "wrong then corrected"
    (is (success?
         (-> (new-game ["lura"])
             (respond 0 "sila")
             (respond 0 nil)
             (respond 0 "lura"))))))

(deftest new-game-test
  (let [words ["lura" "sila" "vila"]
        game (new-game words)]
    (testing "has answers"
      (is (= words
             (answers game))))
    (testing "has empty guesses for each answer"
      (is (= [nil nil nil]
             (responses game))))
    (testing "is not a success"
      (is (not (success? game))))))

(deftest respond-test
  (let [game (-> (new-game ["lura" "sila"])
                 (respond 1 "lura"))]
    (testing "adds response at given index"
      (is (= "lura" (nth (responses game) 1))))
    (testing "leaves other responses unchanged"
      (is (= nil (nth (responses game) 0))))
    (testing "can be over-written"
      (let [game2 (respond game 1 "sila")]
        (is (= "sila" (nth (responses game2) 1)))))))
