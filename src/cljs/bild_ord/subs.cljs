(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub]]
            [bild-ord.domain.game :as game]))

(reg-sub
 :slots
 (fn [db _]
   (-> db :games (get (:stage db)) ::game/slots)))

;; Keeps track of the words available to try.
;; Only for the dragging stage of a game.
(reg-sub
 :pile
 (fn [db _]
   (-> db :games :drag ::game/pile)))

(reg-sub
 :current-group
 (fn [db _]
   (:group db)))

(reg-sub
 :current-stage
 (fn [db _]
   (:stage db)))

(reg-sub
 :success?
 (fn [db _]
   (some-> db :games (get (:stage db)) game/success?)))

;; Progress from 0 to 100 of the entire game
(reg-sub
 :progress
 (fn [db _]
   (* (+ (game/progress (-> db :games :drag))
          (game/progress (-> db :games :type)))
       10)))
