(ns bild-ord.db
  (:require [bild-ord.domain.game :as game]
            [bild-ord.domain.words :as words]))

(def default-state
  {:stage       :drag
   :games       {}

   ;; TODO: track wrong moves, detract from score (https://trello.com/c/PU2DgpEN)
   :wrong-moves 0})

(defn new-games
  [group]
  {:drag (game/new-game group (words/words-for-group group) (words/options-for-group group))
   :type (game/new-game group (words/words-for-group group))})

(defn next-stage
  [stage]
  (condp = stage
    :drag :type
    :hint :type
    nil))

(defn valid-stage?
  [stage]
  (#{:drag :hint :type} stage))
