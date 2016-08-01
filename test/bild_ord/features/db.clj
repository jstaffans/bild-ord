(ns bild-ord.features.db
  (:require [bild-ord.db :as db]
            [com.stuartsierra.component :as component]))

(defn load-seed-data [db]
  (db/add-user! db {:username "bobbytables" :password "password"})
  db)

(defn drop-contents [db]
  ; delete * from users?
  )

(defrecord TestDb [config]
  component/Lifecycle
  (start [component]
    (-> (assoc component :spec config)
        (load-seed-data)))
  (stop [component]
    ;(drop-contents)
    (assoc component :spec nil))

  db/UserDb
  (add-user! [this user] (db/add-user!* this user))
  (auth-user [this username password] (db/auth-user* this username password)))

(defn test-db [config]
  (map->TestDb {:config config}))
