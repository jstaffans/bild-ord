(ns bild-ord.views.progress
  (:require [reagent.core :as reagent]))

(defn progress
  []
  (reagent/create-class
   {:reagent-render (fn [component]
                      [:div.progress-container
                       [:div.progress-wrap
                        {:data-progress-percent 15}
                        "progress"]])}))
