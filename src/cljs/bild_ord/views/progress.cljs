(ns bild-ord.views.progress
  (:require [re-frame.core :refer [subscribe]]
            [reagent.core :as reagent]))

(defn progress
  [{:keys [percent]}]
  [:div.progress-container
   [:div.progress.progress-wrap
    [:div.progress.progress-bar {:class (str "percent-" percent)}]]])
