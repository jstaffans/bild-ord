(ns bild-ord.db
  (:require [bild-ord.domain.game :as game]
            [bild-ord.domain.words :as words]))

(def default-state
  {:stage       :drag
   :games       {:drag (game/new-game 0 words/example-words (shuffle words/example-options))
                 :type (game/new-game 0 words/example-words)}
   :wrong-moves 0})

(defn next-stage
  [stage]
  (condp = stage
    :drag :type
    :hint :type
    nil))

(defn valid-stage?
  [stage]
  (#{:drag :hint :type} stage))
