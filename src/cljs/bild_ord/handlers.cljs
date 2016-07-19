(ns bild-ord.handlers
  (:require [re-frame.core :refer [register-handler]]
            [bild-ord.db :refer [new-games default-state valid-stage?]]
            [bild-ord.domain.game :as game]
            [com.rpl.specter :as specter]))

(register-handler
  :initialise-db
  (fn [_ _]
    default-state))

(register-handler
 :game-stage
 (fn [db [_ group stage]]
   (let [stage (keyword stage)]
     {:pre [(valid-stage? stage)]}
     (cond-> db
       (empty? (:games db)) (assoc :games (new-games group))
       true                 (assoc :group group :stage stage)))))

(register-handler
 :guess-word
 (fn [db [_ index word]]
   (-> db
       (update-in [:games (:stage db)] #(game/guess-word % index word)))))

(register-handler
 :cancel-guess
 (fn [db [_ index]]
   (-> db
       (update-in [:games (:stage db)] #(game/cancel-guess % (js/parseInt index))))))

(register-handler
 :move-guess
 (fn [db [_ index-from index-to]]
   (-> db
       (update-in [:games (:stage db)] #(game/move-guess % (js/parseInt index-from) (js/parseInt index-to))))))

(register-handler
 :replace-guess
 (fn [db [_ index word]]
   (-> db
       (update-in [:games (:stage db)] #(game/replace-guess % index word)))))
