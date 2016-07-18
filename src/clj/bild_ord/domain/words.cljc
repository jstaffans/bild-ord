(ns bild-ord.domain.words
  (:require [com.rpl.specter :as specter]))

;; Domain logic and models around words

(def word-groups
  [["sol" "ros" "vas" "ram" "sil"] ["ris" "sal"]
   ["ris" "mål" "tak" "lås" "lök"] ["mil" "sås"]
   ["mås" "fil" "deg" "ur" "bur"] ["god" "bil"]
   ["måne" "sila" "gula" "påse" "vila"] ["sola" "lura"]
   ["måla" "luva" "baka" "läsa" "låsa"] ["låna" "kaka"]
   ["nosa" "fyra" "pipa" "höna" "duva"] ["luva" "höra"]
   ["bada" "meta" "jaga" "tala" "såga"] ["lada" "möta"]
   ["resa" "niga" "lysa" "leka" "leta"] ["reta" "saga"]
   ["hare" "mage" "båge" "fena" "lera"] ["mata" "såga"]
   ["sylt" "kork" "väst" "kniv" "ost"] ["korv" "salt"]])

(defn words-for-group
  [group]
  (-> (partition 1 2 word-groups)
      (nth group)
      flatten))

(defn options-for-group
  [group]
  (-> (partition 2 word-groups)
      (nth group)
      flatten
      shuffle))
