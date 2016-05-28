(ns bild-ord.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :refer [dispatch]]))

(def routes
  ["/game" {["/group/" :group "/part/" :part] :game-stage}])

(defn dispatch-route
  [match]
  (case (:handler match)
    :game-stage (dispatch [:game-stage (-> match :route-params :group) (-> match :route-params :part)])))

(def history
    (pushy/pushy dispatch-route (partial bidi/match-route routes)))

(defn init
  []
  (pushy/start! history))
