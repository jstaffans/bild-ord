(ns bild-ord.endpoint.user
  (:require [bild-ord.endpoint.common :refer [page title-bar]]
            [compojure.core :refer :all]))

(defn login []
  (page
   [:form
    [:input.input {:type "text"}]
    [:button.btn.btn-primary "Logga in"]]))

(defn user-endpoint [config]
  (routes
   (GET "/" []
     (login))))
