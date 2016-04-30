(ns bild-ord.endpoint.common
  (:require [hiccup.page :refer [html5 include-js include-css]]))

(defn page
  "Base page layout"
  ([body] (page body {}))
  ([body options]
   (html5
    {:lang "sv"}
    [:head
     [:title "Bild och ord"]
     (include-css "/assets/basscss/basscss.css")
     (include-css "/css/main.css")
     (include-js "/js/main.js")]
    [:body
     (when-let [class (:class options)]
       {:class class})
     body])))

(defn title-bar
  "Top title bar shown on some pages"
  []
  [:nav.clearfix.title-bar
   [:div.sm-col
    [:h1.m0 "Bild och ord"]]])

(defn include-svg
  [group index]
  [:img.illustration.p2 {:src (str "/svg/" group "/" index ".svg")}])
