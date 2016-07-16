(ns bild-ord.domain.game-test
  (:require [clojure.test :refer :all]
            [bild-ord.domain.game :refer :all :as game]))

(def example-game
  (new-game 0
            ["lura" "sila" "vila" "gula" "måne"]
            ["lura" "måne" "sila" "gula" "sola" "påse" "vila"]))

(deftest play-dragging-game-test
  (testing "right first time"
    (is (success?
         (-> example-game
             (guess-word 0 "lura")
             (guess-word 1 "sila")
             (guess-word 2 "vila")
             (guess-word 3 "gula")
             (guess-word 4 "måne"))))))

(deftest correctness-test
  (testing "A slot may contain a correct guess"
    (is (true? (correct? (slot "lura" "lura")))))
  (testing "A slot may contain an incorrect guess"
    (is (false? (correct? (slot "lura" "sila")))))
  (testing "A slot with no guess is incorrect"
    (is (false? (correct? (slot "lura")))))
  (testing "A game is a collection of correctness"
    (is (= [true false false]
           (map correct? [(slot "lura" "lura")
                          (slot "sila" "vila")
                          (slot "vila" "sila")])))))

(deftest success-test
  (testing "A successful game"
    (testing "has all answers correct"
      (is (true? (success? {::game/slots [(slot "lura" "lura")
                                          (slot "sila" "sila")
                                          (slot "vila" "vila")
                                          (slot "gula" "gula")
                                          (slot "måne" "måne")]})))))
  (testing "An unsuccessful game"
    (testing "has some answers missing"
      (is (false? (success? {::game/slots [(slot "lura" "lura")
                                           (slot "sila" "sila")
                                           (slot "vila")
                                           (slot "gula")
                                           (slot "måne")]}))))
    (testing "has some answers incorrect"
      (is (false? (success? {::game/slots [(slot "lura" "lura")
                                           (slot "sila" "sila")
                                           (slot "vila" "gula")
                                           (slot "gula" "vila")
                                           (slot "måne" "måne")]}))))))


(deftest guess-word-test
  (let [after-guess (guess-word example-game 0 "lura")]
    (testing "puts the guess in the slot"
      (is (= "lura"
             (get-guess after-guess 0))))
    (testing "removes the option from the pile"
      (is (true? (::game/used? (get-option after-guess "lura")))))))

(deftest move-guess-test
  (let [after-move (-> example-game
                       (guess-word 0 "lura")
                       (move-guess 0 1))]
    (testing "original guess has gone"
      (is (nil? (get-guess after-move 0))))
    (testing "guess is in new slot"
      (is (= "lura"
             (get-guess after-move 1))))))

(deftest move-replace-guess-test
  (let [after-move (-> example-game
                       (guess-word 0 "lura")
                       (guess-word 1 "sila")
                       (move-guess 0 1))]
    (testing "replaced guess is back in pile"
      (is (false? (::game/used? (get-option after-move "sila")))))))

(deftest cancel-guess-test
  (let [after-cancel (-> example-game
                         (guess-word 0 "lura")
                         (cancel-guess 0))]
    (testing "original guess has gone"
      (is (nil? (get-guess after-cancel 0))))
    (testing "guess is back in pile"
      (is (false? (::game/used? (get-option after-cancel "lura")))))))

(deftest replace-guess-test
  (let [after-replace (-> example-game
                          (guess-word 0 "lura")
                          (replace-guess 0 "sila"))]
    (testing "original guess has been replaced with new guess"
      (is (= "sila" (get-guess after-replace 0))))
    (testing "original guess is back in the pile"
      (is (false? (::game/used? (get-option after-replace "lura")))))
    (testing "new guess is removed from pile"
      (is (true? (::game/used? (get-option after-replace "sila")))))))

(deftest progress-test
  (let [initial-state             example-game
        after-one-correct-guess   (-> example-game (guess-word 0 "lura"))
        after-all-correct-guesses (-> example-game
                                      (guess-word 0 "lura")
                                      (guess-word 1 "sila")
                                      (guess-word 2 "vila")
                                      (guess-word 3 "gula")
                                      (guess-word 4 "måne"))]
    (testing "progress from zero to five correct guesses"
      (is (= 0 (progress initial-state)))
      (is (= 1 (progress after-one-correct-guess)))
      (is (= 5 (progress after-all-correct-guesses))))))
