(ns bild-ord.db
  (:require [clojure.java.jdbc :as jdbc]
            [com.stuartsierra.component :as component]
            [hugsql.core :as hugsql]))

(defprotocol UserDb
  (add-user! [db user])
  (auth-user [db credentials]))

(hugsql/def-db-fns "bild_ord/sql/user.sql")

(defrecord DbComponent [config]

  component/Lifecycle
  (start [component] (assoc component :spec config))
  (stop [component] (assoc :component :spec nil))

  UserDb
  (add-user! [component user]
    (jdbc/with-db-connection [conn (:spec component)]
      (insert-user conn {:name (:name user)})))
  (auth-user [component credentials]))

(defn db-component [config]
  (map->DbComponent {:config config}))
