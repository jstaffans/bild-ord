(ns bild-ord.views
  (:require [re-frame.core :refer [subscribe]]
            [bild-ord.views.common :as common]
            [bild-ord.views.part-1 :as part-1]))

(defn app
  []
  [:div.clearfix.game-container
   [:div.col.col-4.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      ^{:key i} [common/render-illustration-svg i])]
   [:div.col.col-3.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      ^{:key i} [part-1/render-word-drop-area i])]
   [:div.col.col-5.p3.fill-y.flex.flex-wrap.content-center.right-pane
    [:div.col-12.flex.flex-column.justify.around.words
     (for [i (range 7)]
       ^{:key i} [part-1/render-word i])]]])
