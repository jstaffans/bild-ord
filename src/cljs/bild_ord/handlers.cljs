(ns bild-ord.handlers
  (:require [re-frame.core :refer [register-handler]]
            [bild-ord.db :refer [default-state]]
            [bild-ord.domain.game :as game]
            [com.rpl.specter :as specter]))

(register-handler
  :initialise-db
  (fn [_ _]
    default-state))

(register-handler
 :game-stage
 (fn [db [_ group part]]
   (let [parts [:drag :type]]
     (assoc db :group group :part (nth parts (js/parseInt part))))))

(defn debug-game-state
  [db]
  (let [data (select-keys (:game db) [::game/slots ::game/pile])]
    (.log js/console (pr-str data))
    db))

(register-handler
 :guess-word
 (fn [db [_ index word]]
   (-> db
       (update-in [:game] #(game/guess-word % index word))
       (debug-game-state))))

#_(register-handler
 :move-guess
 (fn [db [_ index-from index-to]]
   (-> db
       (update-in [:game] #(game/move-guess % index-from index-to))
       (debug-game-state))))
