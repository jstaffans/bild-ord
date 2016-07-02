(ns bild-ord.views.common
  (:require [reagent.core :as reagent]
            cljsjs.jquery
            cljsjs.jquery-ui))

(defn render-illustration-svg
  "Renders one of the left-hand illustrations."
  [index]
  [:img.illustration.m2 {:src (str "/svg/" 0 "/" index ".svg")}])

(defn draggable
  "Wraps the given render-fn with a jQuery draggable"
  [render-fn]
  (reagent/create-class
   {:reagent-render      render-fn
    :component-did-mount (fn [component]
                           (.draggable
                            (js/$ (reagent/dom-node component))
                            #js {:revert true}))}))
