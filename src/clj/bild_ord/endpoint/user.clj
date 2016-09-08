(ns bild-ord.endpoint.user
  (:require [bild-ord.db :refer [auth-user add-user!]]
            [bild-ord.domain.words :refer [random-words]]
            [bild-ord.endpoint.common
             :refer
             [clear-session-id page set-session-id title-bar]]
            [clojure.string :as string]
            [compojure.core :refer :all]
            [ring.util
             [anti-forgery :refer [anti-forgery-field]]
             [response :refer [redirect]]]
            [slingshot.slingshot :refer [try+]]))

(defn login
  [request]
  (page
   [:div
    (title-bar)
    [:div.user-form
     (when (get-in request [:query-params "registered"])
       [:div.alert
        "Ditt konto har skapats! Du kan nu logga in."])
     (when (:error request)
       [:div.error
        "Inloggningen misslyckades. Antingen så finns användarnamnet inte, eller så var lösenordet fel. Försök igen."])
     [:form {:action "/login" :method "POST"}
      [:div.control-group
       [:label.label {:for "username"} "Användarnamn"]
       [:input.input {:id "username" :name "username" :type "text" :required "required"}]]
      [:div.control-group
       [:label.label {:for "password"} "Lösenord"]
       [:input.input {:id "password" :name "password" :type "password"}]]
      [:button.my2.btn.btn-primary "Logga in"]
      (anti-forgery-field)]]
    (when (not (get-in request [:query-params "registered"]))
      [:div.center
       "Har du inget konto ännu? Registrera dig "
       [:a {:href "/register"} "här!"]])]))

(defn register
  [request]
  (let [password (string/join "-" (random-words 3))]
    (page
     [:div
      (title-bar)
      [:div.user-form
       (when (:error request)
         [:div.error
          "Registreringen misslyckades - antagligen finns användarnamnet redan. Försök med ett annat användarnamn."])
       [:form {:action "/register" :method "POST"}
        [:div.control-group
         [:label.label {:for "username"} "Användarnamn"]
         [:input.input {:id "username" :name "username" :type "text" :required "required"}]]
        [:div.control-group
         [:label.label {:for "password"} "Ditt lösenord"]
         [:label.label.bold password]
         [:input.input {:id "password" :name "password" :type "hidden" :value password}]]
        [:div.control-group.italic
         "Skriv upp eller kom ihåg ditt lösenord!"]
        [:button.my2.btn.btn-primary "Skapa konto"]
        (anti-forgery-field)]]
      [:div.center
       "Har du redan ett konto? Logga in "
       [:a {:href "/login"} "här!"]]])))

(defn authenticate [db request]
  (let [username (get-in request [:form-params "username"])
        password (get-in request [:form-params "password"])
        session (:session request)]
    (try+
     (auth-user db username password)
     (-> (redirect "/")
         (set-session-id username))
     (catch [:error :bild-ord.db/invalid-username-or-password] e
       (login {:error :login-failed})))))

(defn create-user [db request]
  (let [username (get-in request [:form-params "username"])
        password (get-in request [:form-params "password"])]
    (try+
     (add-user! db {:username username :password password})
     (-> (redirect "/login?registered=1"))
     (catch Object _
       (register {:error :registration-failed})))))

(defn logout [_]
  (-> (redirect "/")
      clear-session-id))

(defn user-endpoint [config]
  (routes
   (GET  "/login" [] login)
   (POST "/login" [] (partial authenticate (:db config)))

   (GET "/register" [] register)
   (POST "/register" [] (partial create-user (:db config)))

   (GET "/logout" [] logout)))
