(ns bild-ord.views.part-1
  (:require [bild-ord.domain.game :as game]
            [bild-ord.views.common :refer [draggable droppable draggable-droppable nbsp]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            cljsjs.jquery
            cljsjs.jquery-ui))

(defn render-illustration-svg
  "Renders one of the left-hand illustrations."
  [index]
  [:img.illustration.m2 {:src (str "/svg/" 0 "/" index ".svg")}])

(defn render-drop-box-svg
  "Renders the generic drop box SVG."
  []
  [:img.slot.m2 {:src (str "/svg/box.svg")}])

(defn render-drop-box
  "Renders the drop area and hooks it up as a jQuery droppable."
  [index]
  (droppable
   render-drop-box-svg
   (fn [word from-index]
     (dispatch
      (if from-index
        [:move-guess from-index index]
        [:guess-word index word])))))

#_(dispatch [:move-guess] index-from index-to)

(defn render-word-in-slot
  [index word correct?]
  (draggable-droppable
    (fn [index word correct?]
      [:span {:data-drag-source index
              :class            (str "words " (if correct? "correct" "incorrect"))} word])
    (fn [word from-index]
      (dispatch
       (if from-index
         [:move-guess from-index index]
         [:replace-guess index word])))))

(defn render-guess
  [index {:keys [::game/guess] :as slot}]
  [:div.slot.slot-guess.m2
   [render-word-in-slot index guess (game/correct? slot)]])

(defn render-slot [index slot]
  (if (game/responded? slot)
    ^{:key index} [render-guess index slot]
    ^{:key index} [render-drop-box index]))

(defn render-slots []
  "Renders the slot: either an empty droppable box or a response."
  (let [slots (subscribe [:slots])]
    (fn []
      (into [:div.col.col-3.flex.flex-column.justify.around.fill-y]
            (map-indexed render-slot @slots)))))

(defn render-word-draggable
  [word]
  (draggable (fn [word] [:span word])))

(defn render-option [index {:keys [::game/used? ::game/word] :as option}]
  ^{:key index}
  [:div {:class (str "r" index)}
      (if used?
        nbsp
        [render-word-draggable word])])

(defn render-pile []
  (let [pile (subscribe [:pile])
        random-indicies (-> @pile count range shuffle)]
    (droppable
     (fn []
       (into [:div.col-12.p3.flex.flex-column.justify-around.words.words-drag]
             (map render-option random-indicies @pile)))
     (fn [_ from-index] (dispatch [:cancel-guess from-index])))))
