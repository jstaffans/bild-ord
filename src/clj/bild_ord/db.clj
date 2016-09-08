(ns bild-ord.db
  (:require [buddy.hashers :as hs]
            [clojure.java.jdbc :as jdbc]
            [com.stuartsierra.component :as component]
            [dire.core :as dire]
            [hugsql.core :as hugsql]
            [slingshot.slingshot :refer [throw+]]))

(defprotocol UserDb
  (add-user! [this user])
  (auth-user [this username password])
  (complete-group! [this username group])
  (group-completed? [this username group])
  (truncate-database! [this]))

(hugsql/def-db-fns "bild_ord/sql/user.sql")

(defrecord DbComponent [config]
  component/Lifecycle
  (start [component] (assoc component :spec config))
  (stop [component] (assoc component :spec nil)))

(defn add-user!* [db user]
  (jdbc/with-db-connection [conn (:spec db)]
    (insert-user! conn (update-in user [:password] hs/encrypt))))

(defn auth-user* [db username password]
  (jdbc/with-db-connection [conn (:spec db)]
    (let [unauthed {:error ::invalid-username-or-password}]
      (if-let [user (get-user conn {:username username})]
        (if (hs/check password (:password user))
          (dissoc user :password)
          (throw+ unauthed))
        (throw+ unauthed)))))

(defn complete-group!* [db username group]
  (jdbc/with-db-connection [conn (:spec db)]
    (upsert-progress! conn {:username username, :game-id group})))

(defn group-completed?* [db username group]
  (jdbc/with-db-connection [conn (:spec db)]
    (= "done" (-> (get-progress conn {:username username, :game-id group}) first :state))))

(defn truncate-database!* [db]
  (jdbc/with-db-connection [conn (:spec db)]
    (delete-users! conn)
    (delete-progress! conn)))

(dire/with-handler! #'add-user!*
  java.sql.SQLException
  (fn [e & args]
    (condp (partial re-seq) (.getMessage e)
      #"UNIQUE.*user\.username"
      (throw+ {:error ::username-taken :username (select-keys (second args) [:username])}))))

(extend-type DbComponent
  UserDb
  (add-user! [this user] (add-user!* this user))
  (auth-user [this username password] (auth-user* this username password))
  (complete-group! [this username group] (complete-group!* this username group))
  (group-completed? [this username group] (group-completed?* this username group))
  (truncate-database! [this] (truncate-database!* this)))

(defn db-component [config]
  (map->DbComponent {:config config}))

(comment
  ;; Add a user to the database
  (require '[bild-ord.db :as db])
  (db/add-user! (:db system) {:username "johannes" :password "mypass"})
  )
