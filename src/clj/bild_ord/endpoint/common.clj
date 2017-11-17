(ns bild-ord.endpoint.common
  (:require [clojure.java.io :as io]
            [clojure.string :as st]
            [hiccup.page :refer [html5 include-js include-css]]
            [gin.site :refer [sentinel]]))

(defn current-version []
  (try
    (-> "cache_version" io/resource slurp st/trim-newline)
    (catch java.lang.IllegalArgumentException e
      (println "Couldn't read from /resource/cache_version file")
      "any")))

(defn versioned-resource [path]
  (str path "?_version=" (current-version)))

(defn page
  "Base page layout"
  ([ga body] (page ga body {}))
  ([ga body options]
   (html5
    {:lang "sv"}
    [:head
     [:title (or (:title options) "Bild och ord")]
     [:link {:rel "apple-touch-icon-precomposed" :href "/favicon-152.png"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     (include-css "/css/base.css")
     (include-css
      (versioned-resource "/css/main.css"))
     (sentinel (:token ga))
     ]
    [:body
     (when-let [class (:class options)]
       {:class class})
     body
     (include-js
      (versioned-resource "/js/main.js"))
     (include-js "/lib/jquery.ui.touch-punch.min.js")
     (when (:cljs-main options)
       [:script (str (:cljs-main options) "();")])])))

(defn title-bar
  "Top title bar"
  []
  [:nav.clearfix.title-bar
   [:div.col.col-12
    [:a.h1 {:href "/"} "Bild och ord"]]])

(defn title-bar-with-actions
  "Title bar that includes actions, such as login/logout"
  [id]
  [:nav
   [:div.title-bar.flex.justify-between.items-center
    [:a.h1 {:href "/"} "Bild och ord"]
    [:ul.actions
     [:li [:a {:href "http://www.kjellstaffans.fi/material/bild-och-ord-pa-natet/"} "Instruktioner"]]
     [:li
      (if id
        [:div.menu-header {:id "menu-header"} id
         [:div.absolute.menu
          [:a {:href "/logout" } "Logga ut"]]]
        [:div
         [:a {:href "/login"} "Logga in"]])]]]])

(defn session-id
  [request]
  (-> request :session :identity))

(defn set-session-id
  [request id]
  (assoc-in request [:session :identity] id))

(defn clear-session-id
  [request]
  (assoc-in request [:session :identity] nil))
