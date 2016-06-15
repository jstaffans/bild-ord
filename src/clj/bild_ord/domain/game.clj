(ns bild-ord.domain.game
  (:require [clojure.spec :as s]))

(s/def ::word string?)

(s/def ::truth ::word)
(s/def ::guess ::word)
(s/def ::slot (s/keys :req [::truth]
                      :opt [::guess]))
(s/def ::slots (s/tuple ::slot ::slot ::slot ::slot ::slot))

(s/def ::used? boolean?)
(s/def ::option (s/keys :req [::word]
                        :opt [::used?]))
(s/def ::pile (s/tuple ::option ::option ::option ::option
                       ::option ::option ::option ::option))

(s/def ::group integer?)

(s/def ::current-stage #{:dragging :typing})

(s/def ::game (s/keys :req [::group ::slots ::current-stage]
                      :opt [::pile]))

