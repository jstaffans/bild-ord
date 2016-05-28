(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

;; subscription for drop areas, to know whether or not
;; a word has been dropped in the area
(register-sub
 :dropped-word-query
 (fn [db [_ index]]
   (reaction (get-in @db [:answers index :word]))))

;; returns the set of words already used in part 1 (already dragged)
(register-sub
 :answers
 (fn [db _]
   (reaction (set (map :word (:answers @db))))))
