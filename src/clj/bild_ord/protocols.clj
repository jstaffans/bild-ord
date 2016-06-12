(ns bild-ord.protocols)

;; Protocol describing the storage functionality we want to have

(defprotocol Storage
  (get-record [this k])
  (insert-record! [this k r]))


;; User persistence

(defprotocol User
  (add-user! [db user])
  (auth-user [db credentials]))
