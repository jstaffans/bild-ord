(ns bild-ord.endpoint.game-test
  (:require [clojure.test :refer :all]
            [bild-ord.endpoint.game :as game]))

(def handler
  (game/game-endpoint {}))

(deftest a-test
  (testing "TODO: Write me or delete me!"
    (is (= 1 1))))
