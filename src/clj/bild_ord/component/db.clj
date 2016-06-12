(ns bild-ord.component.db
  (:require [clojure.java.io :as io]
            [bild-ord.protocols :as protocols]
            [com.stuartsierra.component :as component]))


;; File-based persistence implementation

(defn serialize [data file]
  (spit file (pr-str data)))

(defn deserialize [file]
  (read-string (slurp file)))

(defn file-exists? [file]
  (.exists (io/as-file file)))

(defn init-db! [file]
  (serialize {} file))


;; Component

(defrecord FileDbComponent [config]
  component/Lifecycle
  (start [component]
    (if (file-exists? (:file config))
      (assoc component :file (:file config))
      (do
        (init-db! (:file config))
        (assoc component :file (:file config)))))

  (stop [component] (assoc component :file nil))

  protocols/Storage
  (get-record [component k]
    (-> (:file component)
        deserialize
        (get k)))

  (insert-record! [component k r]
    (-> (:file component)
        deserialize
        (assoc k r)
        (serialize (:file component)))))

(defn db-component [config]
  (map->FileDbComponent {:config config}))


(comment
  (:db system)
  (require '[bild-ord.protocols :refer :all])
  (insert-record! (:db system) :foo2 "bar")
  )
