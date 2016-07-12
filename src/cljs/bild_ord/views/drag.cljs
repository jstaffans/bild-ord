(ns bild-ord.views.drag
  (:require [bild-ord.domain.game :as game]
            [bild-ord.views.common :refer [draggable droppable draggable-droppable nbsp]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            cljsjs.jquery
            cljsjs.jquery-ui))

(defn- drop-box-svg
  "Renders the generic drop box SVG."
  []
  [:img.slot.m2 {:src (str "/svg/box.svg")}])

(defn- drop-box
  "Renders the drop area and hooks it up as a jQuery droppable."
  [index]
  (droppable
   drop-box-svg
   (fn [word from-index]
     (dispatch
      (if from-index
        [:move-guess from-index index]
        [:guess-word index word])))))

(defn- word-in-slot
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

(defn- guess
  [index {:keys [::game/guess] :as slot}]
  [:div.slot.slot-guess.m2
   [word-in-slot index guess (game/correct? slot)]])

(defn- slot
  [index slot]
  (if (game/responded? slot)
    ^{:key index} [guess index slot]
    ^{:key index} [drop-box index]))

(defn- word-draggable
  [word]
  (draggable (fn [word] [:span word])))

(defn- option
  [index {:keys [::game/used? ::game/word] :as option}]
  ^{:key index}
  [:div.word {:class (str "r" index)}
      (if used?
        nbsp
        [word-draggable word])])

(defn slots
  []
  "Renders the slots column: either droppable boxes or responses."
  (let [slots (subscribe [:slots])]
    (fn []
      (into [:div.col.col-3.flex.flex-column.justify.around.fill-y]
            (map-indexed slot @slots)))))

(defn instructions-and-pile
  []
  "Renders the pile from which to drag words, and instructions for dragging."
  (let [pile (subscribe [:pile])
        random-indicies (-> @pile count range shuffle)]
    (droppable
     (fn []
       [:div.col.col-5.fill-y
        [:div.flex.flex-column.fill-y
         [:span.p3.instructions
          "Dra orden till deras rätta platser."]
         (into [:div.px3.pb3.flex.flex-column.justify-around.words.words-drag]
               (map option random-indicies @pile))]])
     (fn [_ from-index]
       (when from-index (dispatch [:cancel-guess from-index]))))))
