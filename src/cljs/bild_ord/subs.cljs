(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]))

(register-sub
 :dropped-word-query
 (fn [db [_ index]]
   (reaction (get-in @db [:drop-areas index :word]))))
