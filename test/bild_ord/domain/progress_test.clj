(ns bild-ord.domain.progress-test
  (:require [clojure.test :refer :all]
            [bild-ord.features.fixtures :refer [with-system test-database test-config]]
            [bild-ord.db :as db]))

(deftest progress-test
  (with-system [system (test-database test-config)]
    (let [db (:db @system)
          todo-group 0
          done-group 1
          username "bobbytables"]

      (db/complete-group! db username done-group)

      (testing "todo group should not be completed"
        (is (false? (db/group-completed? db username todo-group))))
      
      (testing "done group should be completed"
        (is (true? (db/group-completed? db username done-group)))))))
