(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

;; subscription for drop areas, to know whether or not
;; a word has been dropped in the area
#_(register-sub
 :dropped-word-query
 (fn [db [_ index]]
   (reaction (-> @db :game answers (nth index) [:answers index :word]))))

;; returns the set of words already used in part 1 (already dragged)
#_(register-sub
 :answers
 (fn [db _]
   (reaction (set (map :word (:answers @db))))))

(register-sub
 :questions
 (fn [db _]
   (reaction (:questions @db))))

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
