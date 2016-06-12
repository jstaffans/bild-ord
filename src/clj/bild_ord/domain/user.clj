(ns bild-ord.domain.user)

(defprotocol User
  (add-user! [db user])
  (auth-user [db credentials]))
