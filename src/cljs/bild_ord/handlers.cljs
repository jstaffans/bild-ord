(ns bild-ord.handlers
  (:require [re-frame.core :refer [register-handler]]
            [bild-ord.db :refer [default-state]]
            [com.rpl.specter :as specter]))

(register-handler
  :initialise-db
  (fn [_ _]
    default-state))

(register-handler
 :drop-word
 (fn [db [index word]]
   (specter/setval [:drop-areas (specter/keypath index) :word] word db)))
