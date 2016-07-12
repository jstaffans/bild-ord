(ns bild-ord.db
  (:require [bild-ord.domain.game :as game]
            [bild-ord.domain.words :as words]))

(def default-state
  {:stage :drag
   :games {:drag (game/new-game 0 words/example-words (shuffle words/example-options))
           :type (game/new-game 0 words/example-words)}})
