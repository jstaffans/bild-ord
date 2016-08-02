(ns bild-ord.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]
            [re-frame.core :refer [dispatch-sync]]
            [bild-ord.db :as db]))

(def routes
  ["/game" {["/group/" :group "/stage/" :stage] :game-stage}])

(defn dispatch-route
  [match]
  (case (:handler match)
    :game-stage
    (dispatch-sync [:game-stage
                    (-> match :route-params :group js/parseInt)
                    (-> match :route-params :stage)])))

(def history
  (pushy/pushy dispatch-route (partial bidi/match-route routes)))

(defn stage-path
  "Short-circuit to a given stage."
  [group stage]
  (bidi/path-for routes :game-stage :group group :stage stage))

(defn next-stage-path
  "Returns a path for the next stage of the game, given a current stage."
  [group stage]
  (if-let [next-stage (db/next-stage stage)]
    (bidi/path-for routes :game-stage :group group :stage next-stage)
    "/"))

(defn complete-group-path [group]
  (str "/game/group/" group "/complete"))

(defn init
  []
  (pushy/start! history))
