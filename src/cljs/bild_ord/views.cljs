(ns bild-ord.views
  (:require [bild-ord.domain.words :refer [words]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            cljsjs.jquery
            cljsjs.jquery-ui))

(defn render-illustration-svg
  [index]
  [:img.illustration.m2 {:src (str "/svg/" 0 "/" index ".svg")}])

(defn render-box-svg
  [index]
  [:img.box.m2 {:src (str "/svg/box.svg")}])

(defn render-word-draggable
  [word]
  (reagent/create-class
   {:reagent-render      (fn []
                           [:span word])
    :component-did-mount (fn [component]
                           (.draggable (js/$ (reagent/dom-node component))))}))

(defn render-word
  [index]
  [:div {:class (str "r" (rand-int 6))}
   [render-word-draggable (nth (words 0) index)]])

(defn app
  []
  [:div.clearfix.game-container
   [:div.col.col-4.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      [render-illustration-svg i])]
   [:div.col.col-3.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      [render-box-svg i])]
   [:div.col.col-5.p3.flex.flex-column.justify.around.fill-y.words
    (for [i (range 7)]
      [render-word i])]])
