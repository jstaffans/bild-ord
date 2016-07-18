(ns bild-ord.views.hint
  (:require [bild-ord.domain.words :as words]
            [bild-ord.routes :as routes]
            [re-frame.core :refer [subscribe]]))

;; From the type stage, the user can go back to the drag stage, and the
;; correct words will be shown in their respective positions. This serves
;; as a hint for what to enter as inputs in the type stage.

(defn- truth
  [index truth]
  ^{:key index}
  [:div.m2.slot.slot-truth
   [:span.words.correct truth]])

(defn truths
  []
  "Renders the correct words in their respective positions."
  (into [:div.col.col-3.flex.flex-column.justify.around.fill-y]
        (map-indexed truth (words/words-for-group 0))))

(defn instructions
  []
  [:div.col.col-5.fill-y.p3.instructions
   [:a
    {:href (routes/next-stage-path 0 :hint)}
    "Gå vidare och fortsätt med att skriva in orden."]])
