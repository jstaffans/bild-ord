(ns bild-ord.endpoint.user
  (:require [bild-ord.endpoint.common :refer [page title-bar]]
            [compojure.core :refer :all]))

(defn login []
  (page
   [:div "Login"]))

(defn user-endpoint [config]
  (routes
   (GET "/" []
     (login))))
