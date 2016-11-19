(ns bild-ord.handlers
  (:require [re-frame.core :refer [reg-event-db]]
            [bild-ord.db :refer [new-games default-state valid-stage?]]
            [bild-ord.domain.game :as game]))

(reg-event-db
  :initialise-db
  (fn [_ _]
    default-state))

(reg-event-db
 :game-stage
 (fn [db [_ group stage]]
   (let [stage (keyword stage)]
     {:pre [(valid-stage? stage)]}
     (cond-> db
       (empty? (:games db)) (assoc :games (new-games group))
       true                 (assoc :group group :stage stage)))))

(reg-event-db
 :guess-word
 (fn [db [_ index word]]
   (-> db
       (update-in [:games (:stage db)] #(game/guess-word % index word)))))

(reg-event-db
 :cancel-guess
 (fn [db [_ index]]
   (-> db
       (update-in [:games (:stage db)] #(game/cancel-guess % (js/parseInt index))))))

(reg-event-db
 :move-guess
 (fn [db [_ index-from index-to]]
   (-> db
       (update-in [:games (:stage db)] #(game/move-guess % (js/parseInt index-from) (js/parseInt index-to))))))

(reg-event-db
 :replace-guess
 (fn [db [_ index word]]
   (-> db
       (update-in [:games (:stage db)] #(game/replace-guess % index word)))))
