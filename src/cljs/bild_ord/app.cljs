(ns bild-ord.app
  (:require [bidi.bidi :as bidi]
            [reagent.core :as reagent]
            [bild-ord.views :as views]))

(defn mount-root
  []
  (.log js/console "Mounting root")
  (reagent/render [views/app] (.getElementById js/document "app")))

(defn ^:export main []
  ;; (dispatch [:initialise-app])
  (mount-root))
