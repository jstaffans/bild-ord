(ns bild-ord.app
  (:require [bidi.bidi :as bidi]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch]]
            [bild-ord.views :as views]
            bild-ord.handlers
            bild-ord.subs))

(defn mount-root
  []
  (.log js/console "Mounting root")
  (reagent/render [views/app] (.getElementById js/document "app")))

(defn ^:export main []
  (dispatch [:initialise-db])
  (mount-root))
