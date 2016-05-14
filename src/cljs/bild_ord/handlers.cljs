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
   (update db :drop-areas (fn [drop-areas]
                            (update drop-areas index #(assoc % :word word))))))
