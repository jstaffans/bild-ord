(ns bild-ord.endpoint.overview
  (:require [bild-ord.domain.words :as words]
            [bild-ord.endpoint.common :refer [page session-id title-bar]]
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

(defn column
  [group-from group-to]
  (for [group (range group-from group-to)]
    [:a {:href (str "/game/group/" group "/stage/drag")}
     [:div.clearfix.p2.mb3.group
      [:div.col-1.h2.mr2.index (inc group)]
      [:div.col-8.description
       (illustration group 0)
       (illustration group 1)
       (illustration group 2)
       [:span " …"]]]]) )

(defn overview [request]
  (page
   [:div
    (title-bar (session-id request))
    #_[:div.fit.mx2.mt2.p2.header ""]
    [:div.clearfix.px4.py3.groups
     [:div.col.lg-col-6.md-col-12.sm-col-12 (column 0 6)]
     [:div.col.lg-col-6.md-col-12.sm-col-12 (column 6 10)]]
    [:div.flex.fit.p2.footer
     [:div.flex-auto
      ""]
     [:div "© 2016 Kjell Staffans"]]]
   {:class "overview"}))

(defn overview-endpoint [config]
  (routes
   (GET  "/" [] overview)))
