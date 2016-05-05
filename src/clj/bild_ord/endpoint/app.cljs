(ns bild-ord.endpoint.app
  (:require [bidi.bidi :as bidi]))

(def routes
  ["/" {"" :game}])

(defn main
  []
  (.log js/console {:foo 123}))

(main)
