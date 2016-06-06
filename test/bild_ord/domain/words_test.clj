(ns bild-ord.domain.words-test
  (:require [clojure.test :refer :all]
            [bild-ord.domain.words :refer :all]))

(deftest play-a-game-test
  (testing "right first time"
    (is (success?
         (-> (new-game ["lura"])
             (respond "lura" 0)))))
  (testing "incorrect guess"
    (is (not (success?
              (-> (new-game ["lura"])
                  (respond "sila" 0))))))
  (testing "wrong then corrected"
    (is (success?
         (-> (new-game ["lura"])
             (respond "sila" 0)
             (respond nil 0)
             (respond "lura" 0))))))

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

(deftest correctness-test
  (testing "A guess may be correct"
    (is (true? (correct? {"lura" "lura"}))))
  (testing "A guess may be incorrect"
    (is (false? (correct? {"lura" "sila"}))))
  (testing "A game is a collection of correctness"
    (is [true false false]
        (map correct? [{"lura" "lura"} {"sila" "vila"} {"vila" "sila"}]))))

(deftest success-test
  (testing "A successful game"
    (testing "has all answers correct"
      (is (true? (success? [{"lura" "lura"} {"sila" "sila"} {"vila" "vila"}])))))
  (testing "An unsuccessful game"
    (testing "has some answers missing"
      (is (false? (success? [{"lura" "lura"} {"sila" nil} {"vila" nil}]))))
    (testing "has some answers incorrect"
      (is (false? (success? [{"lura" "sila"} {"sila" "lura"} {"vila" "vila"}]))))))

(deftest respond-test
  (let [game (-> (new-game ["lura" "sila"])
                 (respond "lura" 1))]
    (testing "adds response at given index"
      (is (= "lura" (nth (responses game) 1))))
    (testing "leaves other responses unchanged"
      (is (= nil (nth (responses game) 0))))
    (testing "can be over-written"
      (let [game2 (respond game "sila" 1)]
        (is (= "sila" (nth (responses game2) 1)))))))
