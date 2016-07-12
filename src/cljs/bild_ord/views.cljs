(ns bild-ord.views
  (:require [bild-ord.views.common :as common]
            [bild-ord.views.part-1 :refer [slots pile]]
            [bild-ord.views.part-2 :refer [inputs]]
            [bild-ord.routes :as routes]
            [re-frame.core :refer [subscribe]]))

(defn container
  []
  [:div.clearfix.game-container
   [:div.col.col-4.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      ^{:key i} [common/illustration-svg i])]])

(defn goto-next
  []
  (let [next-fn #(routes/manual-dispatch "/game/group/0/part/1")]
    (common/modal
     [:div.goto-next
      [:h1.m2 "Bra jobbat!"]
      [:div.instructions.m2
       "Du klarade första delen av spelet. Gå nu vidare till nästa del."]
      [:div.m2
       [:button.btn.btn-primary {:on-click next-fn} "Gå vidare"]]]
     next-fn)))

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
                [pile])
         :type (conj
                (container)
                [inputs])
         (container))
       (when @success? (goto-next))))))
