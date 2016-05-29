(ns bild-ord.db)

(def default-state
  {:group   0
   :part    :drag
   :answers (vec (repeat 5 {:word nil}))})
