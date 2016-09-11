(ns bild-ord.endpoint.overview
  (:require [bild-ord.domain.words :as words]
            [bild-ord.endpoint.common :refer [page session-id title-bar-with-actions]]
            [bild-ord.db :as db]
            [clojure.string :as string]
            [compojure.core :refer :all]))

(defn illustration
  [group index]
  [:img.illustration {:src (str "/svg/" group "/" index ".svg")}])

(defn group-state [db group user]
  (if (db/group-completed? db user group) ".done" ".todo"))

(defn group-index-marker [db group user]
  (let [defaults "div.h2.index"
        state (group-state db group user)
        tag (-> defaults (str state) keyword)]
    [tag (inc group)]))

(defn group
  [db user index]
  [:a {:href (str "/game/group/" index "/stage/drag")}
   [:div.group
    (group-index-marker db index user)
    (illustration index 0)]])

(defn overview [db request]
  (let [current-user (session-id request)]
    (page
     [:div
      (title-bar-with-actions current-user)
      [:div.clearfix
       (when (not current-user)
         [:div.alert
          [:a {:href "/login"} "Logga in"]
          " om du vill att ditt avancemang ska sparas. "
          "Du kan också "
          [:a {:href "#"} "ladda ner"]" en översiktsblankett om du vill hålla koll manuellt."])
       [:div.col.col-12.groups.flex.flex-wrap.justify-between
        (for [i (range 0 22)]
          (group db current-user i))]]
      [:div.clearfix
       [:div.col.col-1.p2.footer]
       [:div.col.col-10.p2.footer
        [:div.flex.fit [:div.flex-auto ""]
         [:div "© 2016 Kjell Staffans"]]]
       [:div.col.col-1.p2.footer]]]

     {:class "overview"})))

(defn overview-endpoint [config]
  (routes
   (GET  "/" [] (partial overview (:db config)))))
