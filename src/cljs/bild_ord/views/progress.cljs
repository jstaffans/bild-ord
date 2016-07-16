(ns bild-ord.views.progress
  (:require cljsjs.jquery
            [re-frame.core :refer [subscribe]]
            [reagent.core :as reagent]))

(defn animate-progress
  [component]
  (let [container (-> (js/$ (reagent/dom-node component)))
        wrapper   (-> container (.children ".progress-wrap"))
        bar       (-> wrapper (.children ".progress-bar"))
        width     (.width wrapper)
        progress  (* (/ (-> component reagent/props :percent) 100) width)]
    (-> bar
        (.stop)
        (.animate #js {:width (str progress "px")}))))

(defn progress
  []
  (reagent/create-class
   {:reagent-render (fn []
                      [:div.progress-container
                       [:div.mx4.progress.progress-wrap
                        [:div.progress.progress-bar]]])

    :display-name "progress"

    :component-did-mount  animate-progress
    :component-did-update animate-progress}))
