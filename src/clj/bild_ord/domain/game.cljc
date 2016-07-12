(ns bild-ord.domain.game
  (:require #?(:clj [clojure.spec :as s]
               :cljs [cljs.spec :as s])
            [com.rpl.specter :as specter]))

#?(:cljs (defn boolean? [x] (= (js/typeof x) "boolean")))

(s/def ::word string?)

(s/def ::truth ::word)
(s/def ::guess ::word)
(s/def ::slot (s/keys :req [::truth]
                      :opt [::guess]))
(s/def ::slots (s/tuple ::slot ::slot ::slot ::slot ::slot))
;; TODO: spec that each slot in slots should have different truth?

(s/def ::used? boolean?)
(s/def ::option (s/keys :req [::word]
                        :opt [::used?]))
(s/def ::pile (s/tuple ::option ::option ::option ::option
                       ::option ::option ::option))

(s/def ::group integer?)

(s/def ::game (s/keys :req [::group ::slots]
                      :opt [::pile]))

;; NB: need to check for nils here otherwise could compare nil with nil. Perhaps better to check that truth and guess are valid as either :pre condition or with s/fdef?
(defn correct? [{:keys [::truth ::guess] :as slot}]
  (and (not (nil? truth))
       (not (nil? guess))
       (= truth guess)))

(defn responded? [{:keys [::truth ::guess] :as slot}]
  (not (nil? guess)))

(defn success? [{:keys [::slots] :as game}]
  {:pre [(s/valid? ::slots slots)]}
  (every? correct? slots))

(defn slot
  ([truth]
   {:post [(s/valid? ::slot %)]}
   {::truth truth})
  ([truth guess]
   {:post [(s/valid? ::slot %)]}
   {::truth truth ::guess guess}))

(defn option
  ([word]
   {:post [(s/valid? ::option %)]}
   {::word word
    ::used? false}))

(defn game
  ([group slots]
   {:post [(s/valid? ::game %)]}
   {::group group
    ::slots slots})
  ([group slots pile]
   {:post [(s/valid? ::game %)]}
   (assoc
    (game group slots)
    ::pile pile)))

(defn new-game
  ([group truths]
   (game
    group
    (into [] (map slot truths))))
  ([group truths options]
   (game
    group
    (into [] (map slot truths))
    (into [] (map option options)))))

(defn get-guess [game slot-index]
  (-> game ::slots (nth slot-index) ::guess))

(defn set-guess [game slot-index word]
  {:post [(s/valid? ::game %)]}
  (assoc-in game [::slots slot-index ::guess] word))

(defn remove-guess [game slot-index]
  (specter/transform [::slots (specter/keypath slot-index)]
                     (fn [slot] (dissoc slot ::guess))
                     game))

(defn get-option [game word]
  (->> game ::pile (filter #(= word (::word %))) first))

(defn transform-option [game word transform-fn]
  (specter/transform [::pile specter/ALL #(= (::word %) word)]
                     transform-fn
                     game))

(defn set-used [option]
  (assoc option ::used? true))

(defn set-unused [option]
  (assoc option ::used? false))

(defn remove-option [game word]
  {:post [(s/valid? ::game %)]}
  (transform-option game word set-used))

(defn restore-option [game word]
  {:post [(s/valid? ::game %)]}
  (transform-option game word set-unused))


(defn guess-word [game slot-index word]
  "Puts the word into the slot and removes the option from the pile"
  {:pre [(s/valid? ::game game)
         (integer? slot-index)
         (s/valid? ::word word)]}
  (-> game
      (set-guess slot-index word)
      (remove-option word)))

(defn move-guess [game slot-index-from slot-index-to]
  "Move guess from one slot to another."
  (let [to-slot-guess (get-guess game slot-index-to)
        to-slot-used? (not (nil? to-slot-guess))]
    (cond-> game
      true          (set-guess slot-index-to (get-guess game slot-index-from))
      true          (remove-guess slot-index-from)
      to-slot-used? (restore-option to-slot-guess))))

(defn cancel-guess [game slot-index]
  "Move guess from slot back to pile."
  (-> game
      (restore-option (get-guess game slot-index))
      (remove-guess slot-index)))

(defn replace-guess [game slot-index word]
  "Replace the current slot with a new guess from the pile."
  (-> game
      (remove-option word)
      (restore-option (get-guess game slot-index))
      (set-guess slot-index word)))
