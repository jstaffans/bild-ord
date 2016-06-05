(ns bild-ord.views.part-2
  (:require [bild-ord.domain.words :refer [words]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defn render-input
  "Renders one input where the user can type in a word"
  [index]
  [:div.flex.flex-wrap.items-end
   [:div.m2
    [:input {:type "text" :autocomplete "off"}]]])
