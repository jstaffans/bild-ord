(ns bild-ord.features.user-authentication
  (:require [clojure.test :refer :all]
            [clj-webdriver.taxi :as t]
            [bild-ord.features.fixtures :refer :all]
            [bild-ord.features.helper :refer :all]))

(use-fixtures :once with-built-cljs with-server with-browser)

(deftest logging-in-test
  (testing "Logging-in sets the current user"
    (log-in)
    (is (not (nil? (t/find-element [{:tag "nav"}
                                    {:tag :div, :text "bobbytables"}]))))))
