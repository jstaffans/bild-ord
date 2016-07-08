(ns bild-ord.endpoint.user
  (:require [bild-ord.db :refer [auth-user]]
            [bild-ord.endpoint.common :refer [page title-bar]]
            [compojure.core :refer :all]
            [ring.util
             [anti-forgery :refer [anti-forgery-field]]
             [response :refer [redirect]]]
            [slingshot.slingshot :refer [try+]]))

(defn index [request]
  (if-let [id (-> request :session :identity)]
    (page
      [:div
       (title-bar id)])
     (redirect "/login")))

(defn login
  ([] (login nil))
  ([error]
   (page
    [:div
     (title-bar)
     [:div.form-login
      (when error
        [:div "Error"])
      [:form {:action "/login" :method "POST"}
       [:div.control-group
        [:label.label {:for "username"} "Användarnamn"]
        [:input.input {:id "username" :name "username" :type "text"}]]
       [:div.control-group
        [:label.label {:for "password"} "Lösenord"]
        [:input.input {:id "password" :name "password" :type "password"}]]
       [:button.btn.btn-primary "Logga in"]
       (anti-forgery-field)]]])))

(defn authenticate [db request]
  (let [username (get-in request [:form-params "username"])
        password (get-in request [:form-params "password"])
        session (:session request)]
    (try+
     (auth-user db username password)
     (-> (redirect "/")
         (assoc-in [:session :identity] username))
     (catch [:error :bild-ord.db/invalid-username-or-password] e
       (login :login-failed)))))

(defn user-endpoint [config]
  (routes
   (GET "/" [] index)
   (GET "/login" [] (login))
   (POST "/login" []
     (partial authenticate (:db config)))))
