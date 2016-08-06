(ns bild-ord.endpoint.overview
  (:require [bild-ord.domain.words :as words]
            [bild-ord.endpoint.common :refer [page session-id title-bar-with-actions]]
            [bild-ord.db :as db]
            [clojure.string :as string]
            [compojure.core :refer :all]))

(defn illustration
  [group index]
  [:img.illustration.mx2 {:src (str "/svg/" group "/" index ".svg")}])

(defn group-description
  [group]
  (str
   (string/join
    ", "
    (take 2 (words/words-for-group group)))
   " …"))

(defn group-state [db group user]
  (if (db/group-completed? db user group) ".done" ".todo"))

(defn group-index-marker [db group user]
  (let [defaults "div.col-1.h2.mr2.index"
        state (group-state db group user)
        tag (-> defaults (str state) keyword)]
    [tag (inc group)]))

(defn column
  [group-from group-to db user]
  (for [group (range group-from group-to)]
    [:a {:href (str "/game/group/" group "/stage/drag")}
     [:div.clearfix.p2.mb3.group
      (group-index-marker db group user)
      [:div.col-8.description
       (illustration group 0)
       (illustration group 1)
       (illustration group 2)
       [:span " …"]]]]) )

(defn overview [db request]
  (let [current-user (session-id request)]
    (page
     [:div
      (title-bar-with-actions current-user)
      #_[:div.fit.mx2.mt2.p2.header ""]
      [:div.clearfix.px4.py3.groups
       [:div.col.lg-col-6.md-col-12.sm-col-12 (column 0 8 db current-user)]
       [:div.col.lg-col-6.md-col-12.sm-col-12 (column 8 16 db current-user)]]
      [:div.flex.fit.p2.footer
       [:div.flex-auto
        ""]
       [:div "© 2016 Kjell Staffans"]]]
     {:class "overview"})))

(defn overview-endpoint [config]
  (routes
   (GET  "/" [] (partial overview (:db config)))))
