(ns bild-ord.endpoint.game-test
  (:require [clojure.test :refer :all]
            [bild-ord.endpoint.game :as game]))

(def handler
  (game/game-endpoint {}))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
