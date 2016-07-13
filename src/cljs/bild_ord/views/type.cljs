(ns bild-ord.views.type
  (:require [bild-ord.domain.game :as game]
            [bild-ord.routes :as routes]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defn- input
  "Renders one input where the user can type in a word"
  [index {:keys [::game/guess] :as slot}]
  [:div.flex.flex-wrap.items-end
   [:div.m2
    [:input {:class        (cond
                             (empty? guess)       ""
                             (game/correct? slot) "correct"
                             :else                "incorrect")
             :size         "8"
             :type         "text"
             :autoComplete "off"
             :value        guess
             :on-change    #(dispatch [:guess-word index (-> % .-target .-value)])}]]])

(defn inputs
  []
  (let [slots (subscribe [:slots])]
    (fn []
      (into
       [:div.col.col-3.flex.flex-column.fill-y.words-input]
       (map-indexed input @slots)))))

(defn instructions
  []
  [:div.col.col-5.fill-y.p3.instructions
   "Skriv nu in orden. Om du vill kan du "
   [:a {:href (routes/stage-path 0 :hint)} "gå tillbaka."]])
