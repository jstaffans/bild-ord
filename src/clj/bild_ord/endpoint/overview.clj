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

(defn overview [ga db request]
  (let [current-user (session-id request)]
    (page
     ga
     [:div
      (title-bar-with-actions current-user)
      [:div.clearfix
       (when (not current-user)
         [:div.alert
          [:a {:href "/login"} "Logga in"]
          " eller "
          [:a {:href "/bild_ord.pdf" :target "_blank"} "skriv ut en översiktsblankett"]
          " för att hålla koll på vilka övningar du redan gjort."])
       [:div.col.col-12.groups.flex.flex-wrap.justify-between
        (for [i (range 0 22)]
          (group db current-user i))]]
      [:div.clearfix
       [:div.col.col-12.py2.footer
        [:ul.left
         [:li
          [:a {:href "/bild_ord.pdf"} "Översiktsblankett"]]
         [:li
          [:a {:href "/om"} "Om bild och ord"]]]
        [:div.flex.fit [:div.flex-auto ""]
         [:div "© 2016-2017 Kjell Staffans"]]]]]

     {:class "overview"})))

(defn about [ga request]
  (let [current-user (session-id request)]
    (page
     ga
     [:div
      (title-bar-with-actions current-user)
      [:div.clearfix.about
       [:h1 "Om bild och ord"]
       [:p
        "Denna applikation är en del av det undervisningsmaterial som finns till förfogande på "
        [:a {:href "http://www.kjellstaffans.fi"} "Kjell Staffans hemsida."]
        " Allt material är © 2016 Kjell Staffans och får inte användas eller laddas ner i kommersiellt syfte."]
       [:p (str "Applikationen har utvecklats med stöd av Svenska Kulturfonden, Aktiastiftelsen i Vasa, "
                "Svensk-Österbottniska Samfundet r.f. och Samarbetsförbundet kring funktionshinder r.f.")]]])))

(defn overview-endpoint [ga config]
  (routes
   (GET  "/" [] (partial overview ga (:db config)))
   (GET "/om" [] (partial about ga))))
