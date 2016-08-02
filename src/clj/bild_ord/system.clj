(ns bild-ord.system
  (:require [bild-ord.db :refer [db-component]]
            [bild-ord.endpoint.user :refer [user-endpoint]]
            [bild-ord.endpoint.game :refer [game-endpoint]]
            [bild-ord.endpoint.overview :refer [overview-endpoint]]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [duct.component
             [endpoint :refer [endpoint-component]]
             [handler :refer [handler-component]]
             [hikaricp :refer [hikaricp]]
             [ragtime :refer [ragtime]]]
            [ragtime.jdbc :as jdbc]
            [duct.middleware
             [not-found :refer [wrap-not-found]]
             [route-aliases :refer [wrap-route-aliases]]]
            [meta-merge.core :refer [meta-merge]]
            [ring.component.jetty :refer [jetty-server]]
            [ring.middleware
             [defaults :refer [site-defaults wrap-defaults]]
             [webjars :refer [wrap-webjars]]]))

(def base-config
  {:app     {:middleware [[wrap-not-found :not-found]
                          [wrap-webjars]
                          [wrap-defaults :defaults]
                          [wrap-route-aliases :aliases]]
             :not-found  (io/resource "bild_ord/errors/404.html")
             :defaults   (meta-merge site-defaults {:static {:resources "bild_ord/public"}})
             :aliases    {}}
   :ragtime {:resource-path "bild_ord/migrations"}})

(defn new-system [config]
  (let [config (meta-merge base-config config)]
    (-> (component/system-map
         :app (handler-component (:app config))
         :http (jetty-server (:http config))
         :game-endpoint (endpoint-component game-endpoint)
         :user-endpoint (endpoint-component user-endpoint)
         :overview-endpoint (endpoint-component overview-endpoint)
         :db (db-component (:db config))
         :ragtime (ragtime (:ragtime config)))
        (component/system-using
         {:http              [:app]
          :game-endpoint     [:db]
          :user-endpoint     [:db]
          :overview-endpoint [:db]
          :app               [:game-endpoint :user-endpoint :overview-endpoint]
          :ragtime           [:db]}))))
