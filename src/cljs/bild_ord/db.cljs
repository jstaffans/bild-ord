(ns bild-ord.db)

(def default-state
  {:group   0
   :part    0
   :answers (vec (repeat 5 {:word nil}))})
