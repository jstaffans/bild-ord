(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
 :dropped-word-query
 (fn [db [_ index]]
   (reaction (get-in @db [:answers index :word]))))

(register-sub
 :answers
 (fn [db _]
   (reaction (set (map :word (:answers @db))))))
