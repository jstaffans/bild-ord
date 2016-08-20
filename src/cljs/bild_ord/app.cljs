(ns bild-ord.app
  (:require [bidi.bidi :as bidi]
            bild-ord.handlers
            [bild-ord.routes :as routes]
            bild-ord.subs
            [bild-ord.views :as views]
            [re-frame.core :refer [dispatch-sync]]
            [reagent.core :as reagent]))

;; hook up the nav bar actions, visible on every page
(when-let [menu-element (.getElementById js/document "menu-header")]
  (.addEventListener menu-element "click" #(-> menu-element .-classList (.toggle "open"))))

(defn mount-root
  []
  (reagent/render [views/app] (.getElementById js/document "app")))

(defn ^:export main []
  (dispatch-sync [:initialise-db])
  (routes/init)
  (mount-root))
