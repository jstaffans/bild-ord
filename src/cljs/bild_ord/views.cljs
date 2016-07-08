(ns bild-ord.views
  (:require [bild-ord.views.common :as common]
            [bild-ord.views.part-1 :refer [slots pile]]
            [bild-ord.views.part-2 :refer [input]]
            [re-frame.core :refer [subscribe]]))

(defn container
  []
  [:div.clearfix.game-container
   [:div.col.col-4.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      ^{:key i} [common/illustration-svg i])]])

(defn goto-next
  []

  )

(defn app
  []
  (let [part (subscribe [:current-part])
        success? (subscribe [:success?])]
    (fn []
      (conj
       (condp = @part
         :drag (conj
                (container)
                [slots]
                [:div.col.col-5.fill-y.flex.flex-wrap.content-center
                 [pile]])
         :type (conj
                (container)
                [:div.col.col-8.flex.flex-column.fill-y.words-input
                 (for [i (range 5)]
                   ^{:key i} [input i])])
         (container))
       (common/modal "Grattis" #(.log js/console "next"))
       #_(when @success? (modal "Grattis" #(.log js/console "next")))))))
