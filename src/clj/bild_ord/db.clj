(ns bild-ord.db
  (:require [buddy.hashers :as hs]
            [clojure.java.jdbc :as jdbc]
            [com.stuartsierra.component :as component]
            [dire.core :as dire]
            [hugsql.core :as hugsql]
            [slingshot.slingshot :refer [throw+]]))

(defprotocol UserDb
  (add-user! [this user])
  (auth-user [this credentials]))

(hugsql/def-db-fns "bild_ord/sql/user.sql")

(defrecord DbComponent [config]
  component/Lifecycle
  (start [component] (assoc component :spec config))
  (stop [component] (assoc component :spec nil)))

(defn add-user!* [db user]
  (jdbc/with-db-connection [conn (:spec db)]
    (insert-user! conn (update-in user [:password] hs/encrypt))))

(defn auth-user* [db credentials]
  (jdbc/with-db-connection [conn (:spec db)]
    (let [unauthed {:error :invalid-username-or-password}]
      (if-let [user (get-user conn credentials)]
        (if (hs/check (:password credentials) (:password user))
          (dissoc user :password)
          (throw+ unauthed))
        (throw+ unauthed)))))

(dire/with-handler! #'add-user!*
  java.sql.SQLException
  (fn [e & args]
    (condp (partial re-seq) (.getMessage e)
      #"UNIQUE.*user\.username"
      (throw+ {:error :username-taken :username (select-keys (second args) [:username])}))))

(extend-type DbComponent
  UserDb
  (add-user! [this user] (add-user!* this user))
  (auth-user [this credentials] (auth-user* this credentials)))

(defn db-component [config]
  (map->DbComponent {:config config}))

(comment
  (require '[bild-ord.db :as db])
  (db/add-user! (:db system) {:username "johannes" :password "mypass"})
  (db/auth-user (:db system) {:username "johannes" :password "mypass"})
  )
