(ns bild-ord.features.fixtures
  (:require [clj-webdriver.taxi :as t]
            [com.stuartsierra.component :as component]
            [duct.component.ragtime :as ragtime]
            [bild-ord.system :as sys]
            [bild-ord.db :as db]
            [bild-ord.endpoint.user :refer [user-endpoint]]
            [bild-ord.endpoint.game :refer [game-endpoint]]
            [bild-ord.endpoint.overview :refer [overview-endpoint]]
            [cljs.build.api :as cljsb]
            [meta-merge.core :refer [meta-merge]]))

(def test-port 5744)
(def test-host "localhost")
(def test-base-url (str "http://" test-host ":" test-port "/"))
(def test-config
  {:db {:connection-uri "jdbc:sqlite:test.db"}
   :http {:port test-port}
   :app {:defaults {:static {:files "target/compiled"}}}})

(defn build-cljs []
  (cljsb/build "src" {:output-to "target/compiled/js/main.js"
                      :output-dir "target/compiled/js/out"
                      :optimizations :whitespace
                      :pretty-print true}))

(defn with-built-cljs [tests]
  (build-cljs)
  (tests))

(defn load-seed-data [db]
  (db/add-user! db {:username "bobbytables" :password "password"}))

(defn clean-database [db]
  (db/truncate-database! db))

(defrecord TestFixtures []
  component/Lifecycle
  (start [this]
    (-> this :ragtime ragtime/reload ragtime/migrate)
    (load-seed-data (:db this))
    (assoc this :seeded? true))
  (stop [this]
    (clean-database (:db this))
    (assoc this :seeded? nil)))

(defn test-fixtures []
  (->TestFixtures))

(defn test-system [config]
  (let [config (meta-merge sys/base-config config)]
    (-> (component/system-map
         :app (duct.component.handler/handler-component (:app config))
         :http (ring.component.jetty/jetty-server (:http config))
         :game-endpoint (duct.component.endpoint/endpoint-component game-endpoint)
         :user-endpoint (duct.component.endpoint/endpoint-component user-endpoint)
         :overview-endpoint (duct.component.endpoint/endpoint-component overview-endpoint)
         :db (bild-ord.db/db-component (:db config))
         :ragtime (duct.component.ragtime/ragtime (:ragtime config))
         :fixtures (test-fixtures))
        (component/system-using
         {:http              [:app]
          :game-endpoint     []
          :user-endpoint     [:db]
          :overview-endpoint [:db]
          :app               [:game-endpoint :user-endpoint :overview-endpoint]
          :ragtime           [:db]
          :fixtures          [:db :ragtime]}))))

(defn test-database [config]
  (let [config (meta-merge sys/base-config config)]
    (-> (component/system-map
         :db (bild-ord.db/db-component (:db config))
         :ragtime (duct.component.ragtime/ragtime (:ragtime config))
         :fixtures (test-fixtures))
        (component/system-using
         {:ragtime           [:db]
          :fixtures          [:db :ragtime]}))))

(defmacro with-system [[system-sym system-config] & body]
  `(let [~system-sym (atom ~system-config)]
     (try
       (swap! ~system-sym component/start)
       ~@body
       (finally (swap! ~system-sym component/stop)))))

(defn with-server [tests]
  (let [system (atom (test-system test-config))]
    (try
      (swap! system component/start)
      (tests)
      (finally
        (swap! system component/stop)))))

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
