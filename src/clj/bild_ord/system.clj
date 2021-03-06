(ns bild-ord.system
  (:require [bild-ord.db :refer [db-component]]
            [bild-ord.endpoint
             [game :refer [game-endpoint]]
             [overview :refer [overview-endpoint]]
             [user :refer [user-endpoint]]]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [duct.component
             [endpoint :refer [endpoint-component]]
             [handler :refer [handler-component]]
             [hikaricp :refer [hikaricp]]
             [ragtime :refer [ragtime]]]
            [duct.middleware
             [not-found :refer [wrap-not-found]]
             [route-aliases :refer [wrap-route-aliases]]]
            [meta-merge.core :refer [meta-merge]]
            [ragtime.jdbc :as jdbc]
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
         :game-endpoint (endpoint-component (partial game-endpoint (:ga config)))
         :user-endpoint (endpoint-component (partial user-endpoint (:ga config)))
         :overview-endpoint (endpoint-component (partial overview-endpoint (:ga config)))
         :db (db-component (:db config))
         :ragtime (ragtime (:ragtime config)))
        (component/system-using
         {:http              [:app]
          :game-endpoint     [:db]
          :user-endpoint     [:db]
          :overview-endpoint [:db]
          :app               [:game-endpoint :user-endpoint :overview-endpoint]
          :ragtime           [:db]}))))
