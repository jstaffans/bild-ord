(ns bild-ord.db
  (:require [bild-ord.domain.game :as game]
            [bild-ord.domain.words :as words]))

(def default-state
  {:game (game/new-game 0 words/example-words words/example-options)})
