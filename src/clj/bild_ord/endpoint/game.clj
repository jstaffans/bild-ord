(ns bild-ord.endpoint.game
  (:require [compojure.core :refer :all]
            [bild-ord.endpoint.common :refer [session-id page title-bar]]
            [ring.util.response :refer [redirect]]))

(defn game-content []
  "Placeholder element for the Reagent app"
  [:div#app])

(defn game [request]
  (page
   [:div
    (title-bar (session-id request))
    (game-content)]
   {:cljs-main "bild_ord.app.main"}))

(defn game-endpoint [config]
  (routes
   (GET "/game/group/:group/stage/:part" [_ _]
     game)))
