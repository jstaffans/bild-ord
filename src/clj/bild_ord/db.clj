(ns bild-ord.db
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as component]))


;; Protocol describing the storage functionality we want to have

(defprotocol Storage
  (get-record [this k])
  (insert-record! [this k r]))


;; File-based persistence implementation

(defn serialize! [data file]
  (spit file (pr-str data)))

(defn deserialize [file]
  (read-string (slurp file)))

(defn file-exists? [file]
  (.exists (io/as-file file)))

(defn init-db! [file]
  (serialize! {} file))


;; Component

(defrecord FileDbComponent [config]
  component/Lifecycle
  (start [component]
    (if (file-exists? (:file config))
      (assoc component :file (:file config))
      (do
        (init-db! (:file config))
        (assoc component :file (:file config)))))

  (stop [component] (assoc component :file nil)))


(extend-type FileDbComponent
  Storage
  (get-record [component k]
    (-> (:file component)
        deserialize
        (get k)))

  (insert-record! [component k r]
    (-> (:file component)
        deserialize
        (assoc k r)
        (serialize! (:file component)))))


(defn db-component [config]
  (->FileDbComponent config))


(comment
  (require '[bild-ord.db :refer :all])
  (insert-record! (:db system) :foo "bar")
  (get-record (:db system) :foo)
  )
