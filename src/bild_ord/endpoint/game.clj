(ns bild-ord.endpoint.game
  (:require [compojure.core :refer :all]
            [bild-ord.endpoint.common :refer [page title-bar]]))

(defn game-endpoint [config]
  (routes
   (GET "/" []
     (page
      [:div
       (title-bar)
       [:div ""]]))))
