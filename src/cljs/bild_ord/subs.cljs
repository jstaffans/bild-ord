(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub-raw]]
            [bild-ord.domain.game :as game]))

(reg-sub-raw
 :slots
 (fn [db _]
   (reaction (-> @db :games (get (:stage @db)) ::game/slots))))

;; Keeps track of the words available to try.
;; Only for the dragging stage of a game.
(reg-sub-raw
 :pile
 (fn [db _]
   (reaction (-> @db :games :drag ::game/pile))))

(reg-sub-raw
 :current-group
 (fn [db _]
   (reaction (:group @db))))

(reg-sub-raw
 :current-stage
 (fn [db _]
   (reaction (:stage @db))))

(reg-sub-raw
 :success?
 (fn [db _]
   (reaction (some-> @db :games (get (:stage @db)) game/success?))))

;; Progress from 0 to 100 of the entire game
(reg-sub-raw
 :progress
 (fn [db _]
   (reaction
    (* (+ (game/progress (-> @db :games :drag))
          (game/progress (-> @db :games :type)))
       10))))
