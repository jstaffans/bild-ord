(ns bild-ord.views.common
  (:require [reagent.core :as reagent]
            cljsjs.jquery
            cljsjs.jquery-ui))

(defn render-illustration-svg
  "Renders one of the left-hand illustrations."
  [index]
  [:img.illustration.m2 {:src (str "/svg/" 0 "/" index ".svg")}])

(defn draggable
  "Wraps the given render-fn with a jQuery draggable."
  [render-fn]
  (reagent/create-class
   {:reagent-render      render-fn
    :component-did-mount (fn [component]
                           (.draggable
                            (js/$ (reagent/dom-node component))
                            #js {:revert true}))}))

(defn droppable
  "Wraps the given render-fn with a jQuery droppable.
  drop-fn is a function that will be called with the dropped word
  (e.g. \"lura\" or \"sila\") and the value of the dragged element's
  data-drag-source attribute (which may be nil)."
  [render-fn drop-fn]
  (reagent/create-class
   {:reagent-render      render-fn
    :component-did-mount (fn [component]
                           (.droppable
                            (js/$ (reagent/dom-node component))
                            #js {:drop (fn [_ ui]
                                         (let [dropped     (js/$ (.-draggable ui))
                                               word        (.text dropped)
                                               drag-source (.attr dropped "data-drag-source")]
                                           (drop-fn word drag-source)))}))}))
