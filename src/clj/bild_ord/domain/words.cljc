(ns bild-ord.domain.words)

;; Domain logic and models around words

(defn example-words
  [group]
  ["lura" "måne" "sila" "gula" "sola" "påse" "vila"])

(defn new-question
  ([answer]
   {answer nil})
  ([answer response]
   {answer response}))

(defn new-game [words]
  (mapv new-question words))

(defn answer [question]
  (-> question first key))

(defn answers [game]
  (mapv answer game))

(defn response [question]
  (-> question first val))

(defn responses [game]
  (mapv response game))

(defn correct? [question]
  (= (answer question)
     (response question)))

(defn success? [game]
  (every? correct? game))

(defn respond [game index response]
  (update-in game [index]
             (fn [question]
               (new-question (answer question) response))))

(defn responded? [question]
  (not (nil? (response question))))

(defn remove-option [options option]
  "Replaces the given option with nil"
  (into [] (map #(if (= % option) nil %) options)))
