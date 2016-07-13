(ns bild-ord.views
  (:require [bild-ord.views.common :as common]
            [bild-ord.views.drag :refer [slots instructions-and-pile]]
            [bild-ord.views.type :refer [inputs instructions]]
            [bild-ord.routes :as routes]
            [re-frame.core :refer [subscribe]]))

(defn container
  []
  [:div.clearfix.game-container
   [:div.col.col-4.flex.flex-column.justify.around.fill-y
    (for [i (range 5)]
      ^{:key i} [common/illustration-svg i])]])

(defn goto-next
  [stage]
  (let [texts     {:drag "Du klarade första delen av spelet. Gå nu vidare till nästa del."
                   :type "Du klarade av andra delen av spelet. Du kan nu gå vidare och välja en annan grupp ord."}
        next-path (routes/next-stage-path 0 stage)]
    (common/modal
     [:div.goto-next
      [:h1.m2 "Bra jobbat!"]
      [:div.m2 (stage texts)]
      [:div.m2
       [:a.btn.btn-primary {:href next-path} "Gå vidare"] ]])))

(defn app
  []
  (let [stage (subscribe [:current-stage])
        success? (subscribe [:success?])]
    (fn []
      (conj
       (condp = @stage
         :drag (conj
                (container)
                [slots]
                [instructions-and-pile])
         :type (conj
                (container)
                [inputs]
                [instructions])
         (container))
       (when @success? (goto-next @stage))))))
