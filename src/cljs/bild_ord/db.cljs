(ns bild-ord.db
  (:require [bild-ord.domain.words :as words]))

(def default-state
  (let [group-words (words/example-words 0)]
    {:group   0
     :part    :drag
     :questions (words/new-game (take 5 group-words))
     :options (shuffle group-words)}))
