(ns bild-ord.views
  (:require [bild-ord.views.common :as common]
            [bild-ord.views.part-1 :as part-1]
            [bild-ord.views.part-2 :as part-2]
            [re-frame.core :refer [subscribe]]))

(defn render-container
  []
  [:div.clearfix.game-container
   [:div.col.col-4.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      ^{:key i} [common/render-illustration-svg i])]])

(defn render-part-1
  "Renders the first part of a game (dragging words to their correct slots)"
  []
  (conj
   (render-container)

   [part-1/render-questions]

   [:div.col.col-5.fill-y.flex.flex-wrap.content-center
    [part-1/render-options]]))

(defn render-part-2
  "Renders the second part of a game (typing the words)"
  []
  (conj
   (render-container)
   [:div.col.col-8.flex.flex-column.fill-y.words-input
    (for [i (range 5)]
      ^{:key i} [part-2/render-input i])]))

(defn app
  []
  (let [part (subscribe [:current-part])]
    (fn []
      (condp = @part
        :drag (render-part-1)
        :type (render-part-2)
        (render-container)))))
