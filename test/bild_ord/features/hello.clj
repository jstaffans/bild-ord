(ns bild-ord.features.hello
  (:require [clojure.test :refer :all]
            [clj-webdriver.core :as s]
            [clj-webdriver.taxi :as t]
            [com.stuartsierra.component :as component]))

(def test-port 5744)
(def test-host "localhost")
(def test-base-url (str "http://" test-host ":" test-port "/"))

(defn with-server [form]
  (let [system (atom (bild-ord.system/new-system {:http {:port test-port}}))]
    (swap! system component/start)
    (form)
    (swap! system component/stop)))

(defn with-browser [form]
  (t/set-driver! {:browser :phantomjs})
  (form)
  (t/quit))

(use-fixtures :once with-server with-browser)

(deftest homepage-test
  (t/to test-base-url)
  (is (= (t/title) "Bild och ord")))
