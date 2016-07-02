(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [bild-ord.domain.game :as game]))

(register-sub
 :slots
 (fn [db _]
   (reaction (-> @db :game ::game/slots))))

;; Keeps track of the words available to try
(register-sub
 :pile
 (fn [db _]
   (reaction (-> @db :game ::game/pile))))

(register-sub
 :current-group
 (fn [db _]
   (reaction (:group @db))))

(register-sub
 :current-part
 (fn [db _]
   (reaction (:part @db))))
