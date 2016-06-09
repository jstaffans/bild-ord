(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

;; Keeps track of the users responses and the "true" answers
(register-sub
 :questions
 (fn [db _]
   (reaction (:questions @db))))

;; Keeps track of the words available to try
(register-sub
 :options
 (fn [db _]
   (reaction (:options @db))))

(register-sub
 :current-group
 (fn [db _]
   (reaction (:group @db))))

(register-sub
 :current-part
 (fn [db _]
   (reaction (:part @db))))
