(ns bild-ord.routes
  (:require [bidi.bidi :as bidi]
            [pushy.core :as pushy]))

(def routes
  ["/game" {["/group/" :group-id "/part/" :part] :group}])

(defn dispatch-route
  [match]
  (case (:handler match)
    :group (.log js/console match)))

(def history
    (pushy/pushy dispatch-route (partial bidi/match-route routes)))

(defn init
  []
  (pushy/start! history))
