(ns bild-ord.views.type
  (:require [bild-ord.domain.words :as words]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defn- input
  "Renders one input where the user can type in a word"
  [index]
  [:div.flex.flex-wrap.items-end
   [:div.m2
    [:input {:size "10" :type "text" :autoComplete "off"}]]])

(defn inputs
  []
  [:div.col.col-8.flex.flex-column.fill-y.words-input
   (for [i (range 5)]
     ^{:key i} [input i])])
