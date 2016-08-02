(ns bild-ord.features.progress-tracking
  (:require [clojure.test :refer :all]
            [clj-webdriver.taxi :as t]
            [bild-ord.features.fixtures :refer :all]
            [bild-ord.features.helper :refer :all]))

(use-fixtures :once with-built-cljs with-server with-browser)

(defn play-game []
  (start-game 1)
  (drag-words "sol" "ros" "vas" "ram" "sil")
  (click-on "Gå vidare")
  (type-words "sol" "ros" "vas" "ram" "sil")
  (click-on "Gå vidare")) 

(defn index-marked-done? []
  (t/exists? {:css ".done", :text "1"}))

(defn index-marked-todo? []
  (t/exists? {:css ".todo", :text "1"}))

(deftest progress-tracking-test
  (testing "Completing a game"
    (testing "when not logged-in, index marker remains grey"
      (t/to test-base-url)
      (play-game)
      (is (index-marked-todo?)))
    (testing "when logged-in, index marker turns green"
      (log-in)
      (play-game)
      (is (index-marked-done?)))))
