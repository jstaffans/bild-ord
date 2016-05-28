(ns bild-ord.views.part-1
  (:require [bild-ord.domain.words :refer [words]]
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
                                               word    (.text dropped)]
                                           (dispatch [:drop-word index word])))}))}))

(defn render-word-svg
  "Renders a word SVG."
  [word]
  [:svg
   ;; TODO: size, position
   [:text {:x 100 :y 30} word]])

(defn render-word-drop-area
  "Renders the word drop area. This may be either an empty drop box, if nothing has been
  dropped there yet, or the already-dropped word."
  [index]
  (let [dropped-word (subscribe [:dropped-word-query index])]
    (fn []
      (if-let [word @dropped-word]
        [render-word-svg word]
        [render-drop-box index]))))

(defn render-word-draggable
  [word]
  (reagent/create-class
   {:reagent-render      (fn []
                           [:span word])
    :component-did-mount (fn [component]
                           (.draggable (js/$ (reagent/dom-node component)) #js {:revert true}))}))


(defn render-word
  [index]
  (let [answers      (subscribe [:answers])
        random-class (str "r" (rand-int 6))]
    (fn []
      [:div {:class random-class}
       (let [word (nth (words 0) index)]
         (if (@answers word)
           nbsp ;; make sure the container stays when draggable word is hidden
           [render-word-draggable word]))])))
