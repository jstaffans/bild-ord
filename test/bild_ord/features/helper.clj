(ns bild-ord.features.helper
  (:require [clj-webdriver.taxi :as t]
            [bild-ord.features.fixtures :refer [test-base-url]]))


;; Interact

(defn log-in []
  (t/to (str test-base-url "login"))
  (t/quick-fill-submit {"#username" "bobbytables"
                        "#password" "password"
                        "button" t/click}))

(defn start-game [index]
  (-> (t/find-element [{:tag :a}
                       {:tag :div}
                       {:tag :div :text (str index)}])
      (t/click)))

(defn click-on [text]
  (t/click {:tag :a, :text text}))


;; Find

(defn find-word [text]
  (t/find-element {:text text}))

(defn find-slot [position]
  (-> (t/find-elements {:css "img.slot.ui-droppable"})
      (nth position)))

(defn find-input [position]
  (-> (t/find-elements {:css "input[type='text']"})
      (nth position)))


;; Play

(defn drag-word [text position]
  (t/drag-and-drop (find-word text)
                   (find-slot position)))

(defn drag-words [& words]
  "Drag words to slots in order.

   Each time the next slot is first available as the preceding slots
   were removed as each word was dropped."
  (doseq [word words]
    (drag-word word 0)))

(defn type-word [text position]
  (t/input-text (find-input position)
                (str "_" text)))

(defn type-words [& words]
  (doall (map-indexed (fn [i w] (type-word w i)) words)))






