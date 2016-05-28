(ns bild-ord.handlers
  (:require [re-frame.core :refer [register-handler]]
            [bild-ord.db :refer [default-state]]
            [com.rpl.specter :as specter]))

(register-handler
  :initialise-db
  (fn [_ _]
    default-state))

(register-handler
 :game-stage
 (fn [db [_ group part]]
   (assoc db :group group :part part)))

(register-handler
 :drop-word
 (fn [db [_ index word]]
   (specter/setval [:answers (specter/keypath index) :word] word db)))
