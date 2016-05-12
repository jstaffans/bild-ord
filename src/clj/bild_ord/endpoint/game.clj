(ns bild-ord.endpoint.game
  (:require [compojure.core :refer :all]
            [bild-ord.endpoint.common :refer [page title-bar include-illustration-svg include-box-svg]]))

(defn include-word
  [group index]
  (let [words ["lura" "måne" "sila" "gula" "sola" "påse" "vila"]]
    [:div {:class (str "r" (rand-int 6))}
     (nth words index)]))

(defn game-content
  []
  [:div#app]
  #_[:div.clearfix.game-container
   [:div.col.col-4.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      (include-illustration-svg 0 i))]
   [:div.col.col-3.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      (include-box-svg i))]
   [:div.col.col-5.p3.flex.flex-column.justify.around.fill-y.words
    (for [i (range 7)]
      (include-word 0 i))]])

(defn game
  []
  (page
   [:div
    (title-bar)
    (game-content)]))

(defn game-endpoint [config]
  (routes
   (GET "/" []
     (game))))
