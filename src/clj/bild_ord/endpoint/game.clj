(ns bild-ord.endpoint.game
  (:require [compojure.core :refer :all]
            [bild-ord.endpoint.common :refer [page title-bar]]
            [ring.util.response :refer [redirect]]))

(defn game-content []
  "Placeholder element for the Reagent app"
  [:div#app])

(defn game []
  (page
   [:div
    (title-bar)
    (game-content)]
   {:cljs-main "bild_ord.app.main"}))

(defn game-endpoint [config]
  (routes
   (GET "/game" []
     (redirect "/game/group/0/part/0"))
   (GET "/game/group/:group/stage/:part" [_ _]
     (game))))
