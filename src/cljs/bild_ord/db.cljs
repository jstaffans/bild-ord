(ns bild-ord.db
  (:require [bild-ord.domain.game :as game]
            [bild-ord.domain.words :as words]))

(def default-state
  {:stage       :drag
   :games       {:drag (game/new-game 0 (words/words-for-group 0) (words/options-for-group 0))
                 :type (game/new-game 0 (words/words-for-group 0))}
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
