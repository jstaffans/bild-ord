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
    :game-stage (dispatch-sync [:game-stage (-> match :route-params :group) (-> match :route-params :stage)])))

(def history
    (pushy/pushy dispatch-route (partial bidi/match-route routes)))

(defn manual-dispatch
  [path]
  (pushy/set-token! history path))

(defn next-stage-path
  [group stage]
  (bidi/path-for routes :game-stage :group group :stage (name (db/next-stage stage))))

(defn init
  []
  (pushy/start! history))
