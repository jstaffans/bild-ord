(ns bild-ord.endpoint.game
  (:require [compojure.core :refer :all]
            [bild-ord.endpoint.common :refer [page title-bar include-svg]]))


(defn game-content
  []
  [:div.clearfix.game-container
   [:div.col.col-4.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      (include-svg 0 i))]
   [:div.col.col-3.flex.fill-y [:div "Boxes"]]
   [:div.col.col-5.flex.fill-y.words [:div "Words"]]])

(defn game
  []
  (page
   [:div
    (title-bar)
    (game-content)]))

(defn game-endpoint [config]
  (routes
   (GET "/" []
     (game))))
