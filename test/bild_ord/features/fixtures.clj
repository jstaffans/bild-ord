(ns bild-ord.features.fixtures
  (:require [clj-webdriver.taxi :as t]
            [com.stuartsierra.component :as component]
            [bild-ord.system :as sys]
            [bild-ord.features.db :as db]
            [cljs.build.api :as cljsb]))

(def test-port 5744)
(def test-host "localhost")
(def test-base-url (str "http://" test-host ":" test-port "/"))
(def test-config
   {:http {:port test-port}
    :app {:defaults {:static {:files "target/compiled"}}}})

(defn build-cljs []
  (cljsb/build "src" {:output-to "target/compiled/js/main.js"
                      :output-dir "target/compiled/js/out"
                      :optimizations :whitespace
                      :pretty-print true}))

(defn with-built-cljs [tests]
  (build-cljs)
  (tests))

(defn test-system []
  (into (sys/new-system test-config)
        {:db (db/test-db {:connection-uri "jdbc:sqlite:test.db"})}))

(defn with-server [tests]
  (let [system (atom (test-system))]
    (swap! system component/start)
    (tests)
    (swap! system component/stop)))

(defn with-browser [tests]
  (t/set-driver! {:browser :phantomjs})
  (tests)
  (t/quit))

(comment
  ;; Use this for debugging tests
  (def system (atom (test-system)))
  (build-cljs)
  (swap! system component/start)
  (t/set-driver! {:browser :phantomjs})
  (t/quit)
  (swap! system component/stop)
  )
