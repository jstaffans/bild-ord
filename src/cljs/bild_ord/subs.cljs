(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [bild-ord.domain.game :as game]))

(defn- current-stage
  [db]
  (:stage @db))

(register-sub
 :slots
 (fn [db _]
   (let [stage (current-stage db)]
     (reaction (-> @db :games stage ::game/slots)))))

;; Keeps track of the words available to try.
;; Only for the dragging stage of a game.
(register-sub
 :pile
 (fn [db _]
   (reaction (-> @db :games :drag ::game/pile))))

(register-sub
 :current-group
 (fn [db _]
   (reaction (:group @db))))

(register-sub
 :current-stage
 (fn [db _]
   (reaction (:stage @db))))

(register-sub
 :success?
 (fn [db _]
   (let [stage (current-stage db)]
     (reaction (-> @db :games stage game/success?)))))
