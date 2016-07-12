(ns bild-ord.views.common
  (:require [reagent.core :as reagent]
            cljsjs.jquery
            cljsjs.jquery-ui))

(def nbsp \u00A0)

(defn illustration-svg
  "Renders one of the left-hand illustrations."
  [index]
  [:img.illustration.m2 {:src (str "/svg/" 0 "/" index ".svg")}])

(defn- add-drag-handler [component]
  (-> (js/$ (reagent/dom-node component))
      (.draggable #js {:revert true})))

(defn- add-drop-handler [component drop-fn]
  (.droppable
   (js/$ (reagent/dom-node component))
   #js {:drop (fn [_ ui]
                (let [dropped     (js/$ (.-draggable ui))
                      word        (.text dropped)
                      drag-source (.attr dropped "data-drag-source")]
                  (drop-fn word drag-source)))}))

(defn draggable
  "Wraps the given render-fn with a jQuery draggable."
  [render-fn]
  (reagent/create-class
   {:reagent-render      render-fn
    :component-did-mount (fn [component]
                           (add-drag-handler component))}))

(defn droppable
  "Wraps the given render-fn with a jQuery droppable.
  drop-fn is a function that will be called with the dropped word
  (e.g. \"lura\" or \"sila\") and the value of the dragged element's
  data-drag-source attribute (which may be nil)."
  [render-fn drop-fn]
  (reagent/create-class
   {:reagent-render      render-fn
    :component-did-mount (fn [component]
                           (add-drop-handler component drop-fn))}))

(defn draggable-droppable
  "Combination of draggable and droppable."
  [render-fn drop-fn]
  (reagent/create-class
   {:reagent-render      render-fn
    :component-did-mount (fn [component]
                           (add-drag-handler component)
                           (add-drop-handler component drop-fn))}))

(defn modal
  "Renders a modal, covering the whole screen."
  [children callback-fn]
  [:div.modal.flex
   [:div.backdrop {:on-click callback-fn}]
   [:div.content.p2
    children]])
