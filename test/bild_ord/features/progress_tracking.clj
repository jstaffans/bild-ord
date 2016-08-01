(ns bild-ord.features.progress-tracking
  (:require [clojure.test :refer :all]
            [clj-webdriver.taxi :as t]
            [bild-ord.features.fixtures :refer :all]
            [bild-ord.features.helper :refer :all]))

(use-fixtures :once with-built-cljs with-server with-browser)

(deftest progress-tracking-test
  (testing "Completing a game turns the index marker green"
    (t/to (str test-base-url "login"))
    (t/quick-fill-submit {"#username" "bobbytables"
                          "#password" "password"
                          "button" t/click})
    (start-game 1)
    (drag-words "sol" "ros" "vas" "ram" "sil")
    (click-on "Gå vidare")
    (type-words "sol" "ros" "vas" "ram" "sil")
    (click-on "Gå vidare")
    (is (t/exists? {:tag :div, :text "1", :class "completed"}))))
