(ns bild-ord.views
  (:require [bild-ord.views.common :as common]
            [bild-ord.views.drag :as drag]
            [bild-ord.views.type :as type]
            [bild-ord.views.hint :as hint]
            [bild-ord.views.progress :as progress]
            [bild-ord.routes :as routes]
            [re-frame.core :refer [subscribe]]))

(defn container
  [group]
  [:div.clearfix.game-container
    [:div.col.col-1 \u00A0]
    [:div.col.col-2.flex.flex-column.justify.around.fill-y
     (for [i (range 5)]
       ^{:key i} [common/illustration-svg group i])]
    [:div.col.col-1 \u00A0]])

(defn goto-type
  [group stage]
  (let [next-path (routes/next-stage-path group stage)]
    (common/modal
     [:div.goto-next
      [:h1.m2 "Bra jobbat!"]
      [:div.m2 "Du klarade första delen av spelet. Gå nu vidare till nästa del."]
      [:div.m2
       [:a.btn.btn-primary {:href next-path} "Gå vidare"] ]])))

(defn goto-done
  [group]
  (common/modal
   [:div.goto-next
    [:h1.m2 "Bra jobbat!"]
    [:div.m2 "Du klarade av andra delen av spelet. Du kan nu gå vidare och välja en annan grupp ord."]
    [:div.m2
     [:a.btn.btn.btn-primary {:href (routes/complete-group-path group)} "Gå vidare"] ]]))

(defn app
  []
  (let [group            (subscribe [:current-group])
        stage            (subscribe [:current-stage])
        success?         (subscribe [:success?])
        current-progress (subscribe [:progress])]
    (fn []
      [:div
       (when @success?
         (condp = @stage
           :drag (goto-type @group @stage)
           :type (goto-done @group)))
       [progress/progress {:percent @current-progress}]
       (condp = @stage
         :drag (conj
                (container @group)
                [drag/slots]
                [drag/instructions-and-pile])
         :hint (conj
                (container @group)
                [hint/truths @group]
                [hint/instructions @group])
         :type (conj
                (container @group)
                [type/inputs]
                [type/instructions @group]))])))
