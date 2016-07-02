(ns bild-ord.views.part-1
  (:require [bild-ord.domain.game :as game]
            [bild-ord.views.common :refer [draggable droppable]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            cljsjs.jquery
            cljsjs.jquery-ui))

(def nbsp \u00A0)

(defn render-illustration-svg
  "Renders one of the left-hand illustrations."
  [index]
  [:img.illustration.m2 {:src (str "/svg/" 0 "/" index ".svg")}])

(defn render-drop-box-svg
  "Renders the generic drop box SVG."
  []
  [:img.box.m2 {:src (str "/svg/box.svg")}])

(defn render-drop-box
  "Renders the drop area and hooks it up as a jQuery droppable."
  [index]
  (droppable
   render-drop-box-svg
   (fn [word] (dispatch [:guess-word index word]))))

#_(dispatch [:move-guess] index-from index-to)

(defn render-word-svg
  "Renders a word SVG."
  [word correct?]
  (let [class (str "words " (if correct? "correct" "incorrect"))]
    [:svg
     ;; TODO: size, position
     [:text {:class class :x 100 :y 30} word]]))

(defn render-guess [{:keys [::game/truth ::game/guess] :as slot}]
  (draggable (fn [] [render-word-svg guess (game/correct? slot)])))

(defn render-slot [index slot]
  (if (game/responded? slot)
    ^{:key index} [render-guess slot]
    ^{:key index} [render-drop-box index]))

(defn render-slots []
  "Renders the slot: either an empty droppable box or a response."
  (let [slots (subscribe [:slots])]
    (fn []
      (into [:div.col.col-3.flex.flex-column.justify.around.fill-y]
            (map-indexed render-slot @slots)))))

(defn render-word-draggable
  [word]
  (draggable (fn [] [:span word])))

(defn render-option [index {:keys [::game/used? ::game/word] :as option}]
  ^{:key index}
  [:div {:class (str "r" index)}
      (if used?
        nbsp
        [render-word-draggable word])])

(defn render-pile []
  (let [pile (subscribe [:pile])
        random-indicies (-> @pile count range shuffle)]
    (fn []
      (into [:div.col-12.p3.flex.flex-column.justify-around.words.words-drag] ;; TODO - but view styles back into main views
            (map render-option random-indicies @pile)))))
