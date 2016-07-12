(ns bild-ord.views
  (:require [bild-ord.views.common :as common]
            [bild-ord.views.drag :refer [slots pile]]
            [bild-ord.views.type :refer [inputs]]
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
  (let [next-fn #(routes/manual-dispatch (routes/next-stage-path 0 stage))]
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
  (let [stage (subscribe [:current-stage])
        success? (subscribe [:success?])]
    (fn []
      (conj
       (condp = @stage
         :drag (conj
                (container)
                [slots]
                [pile])
         :type (conj
                (container)
                [inputs])
         (container))
       (goto-next @stage)
       #_(when @success? (goto-next @stage))))))
