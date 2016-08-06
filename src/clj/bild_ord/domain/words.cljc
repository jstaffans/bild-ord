(ns bild-ord.domain.words)

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
   ["sylt" "kork" "väst" "kniv" "ost"] ["korv" "salt"]
   ["lampa" "tomte" "ödla" "väska" "vante"] ["limpa" "vinter"]
   ["spade" "skata" "groda" "staket" "snabel"] ["skuta" "gryta"]
   ["startar" "spindel" "viftar" "drake" "kryper"] ["stampar" "liftar"]
   ["eld" "tratt" "glass" "slott" "knapp"] ["trött" "skott"]
   ["krona" "slickar" "faller" "krattar" "spinner"] ["fyller" "skrattar"]
   ["dricker" "krockar" "trumma" "trollet" "snäcka"] ["trycker" "brumma"]
   ["tåg" "boll" "sover" "klocka" "måste"] ["många" "troll"]
   ["läser" "pengar" "verkstad" "väcker" "kläder"] ["lovar" "tretton"]
   ["ringer" "regn" "bank" "genast" "korg"] ["tank" "torg"]
   ["ljud" "hjälpa" "hjälm" "gjorde" "hjul"] ["genom" "jaga"]
   ["kylskåp" "tjock" "tjugo" "sked" "sjuk"] ["sju" "skiner"]
   ["själv" "stjärna" "skjorta" "skjuter" "växer"] ["stjärt" "växter"]])

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
