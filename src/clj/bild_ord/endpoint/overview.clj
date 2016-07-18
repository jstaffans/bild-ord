(ns bild-ord.endpoint.overview
  (:require [bild-ord.endpoint.common :refer [session-id page title-bar]]
            [compojure.core :refer :all]))

(defn overview [request]
  (page
   [:div
    (title-bar (session-id request))]))

(defn overview-endpoint [config]
  (routes
   (GET  "/" [] overview)))
