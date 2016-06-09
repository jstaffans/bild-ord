(ns bild-ord.views.part-1
  (:require [bild-ord.domain.words :as words]
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
  (reagent/create-class
   {:reagent-render      (fn [] (render-drop-box-svg))
    :component-did-mount (fn [component]
                           (.droppable
                            (js/$ (reagent/dom-node component))
                            #js {:drop (fn [_ ui]
                                         (let [dropped (js/$ (.-draggable ui))
                                               response (.text dropped)]
                                           (dispatch [:drop-word index response])))}))}))

(defn render-word-svg
  "Renders a word SVG."
  [word correct?]
  (let [class (str "words "
                   (if correct? "correct" "incorrect"))]
    [:svg
     ;; TODO: size, position
     [:text {:class class :x 100 :y 30} word]]))


(defn render-response [question]
  [render-word-svg (words/response question) (words/correct? question)])

(defn render-question [index question]
  (if (words/responded? question)
    ^{:key index} [render-response question]
    ^{:key index} [render-drop-box index]))

(defn render-questions []
  "Renders the question: either an empty droppable box or a response."
  (let [questions (subscribe [:questions])]
    (fn []
      (into [:div.col.col-3.flex.flex-column.justify.around.fill-y]
            (map-indexed render-question @questions)))))


(defn render-word-draggable
  [word]
  (reagent/create-class
   {:reagent-render      (fn []
                           [:span word])
    :component-did-mount (fn [component]
                           (.draggable (js/$ (reagent/dom-node component)) #js {:revert true}))}))


(defn render-option [index option]
  ^{:key index}
  [:div {:class (str "r" index)}
      (if (nil? option)
        nbsp
        [render-word-draggable option])])

(defn render-options []
  (let [options (subscribe [:options])
        random-indicies (-> @options count range shuffle)]
    (fn []
      (into [:div.col-12.p3.flex.flex-column.justify-around.words.words-drag] ;; TODO - but view styles back into main views
            (map render-option random-indicies @options)))))
