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
     (include-css "/css/base.css")
     (include-css "/css/main.css?version=__git_ref")]
    [:body
     (when-let [class (:class options)]
       {:class class})
     body
     (include-js "/js/main.js?version=__git_ref")
     (when (:cljs-main options)
       [:script (str (:cljs-main options) "();")])])))

(defn title-bar
  "Top title bar shown on some pages"
  ([] (title-bar nil))
  ([id]
   [:nav.clearfix.title-bar
    [:div.col.pt1
     [:a.h1 {:href "/"} "Bild och ord"]]
    [:div.col.topmenu id]]))

(defn session-id
  [request]
  (-> request :session :identity))

(defn set-session-id
  [request id]
  (assoc-in request [:session :identity] id))
