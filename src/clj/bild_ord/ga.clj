(ns bild-ord.ga
  (:require [com.stuartsierra.component :as component]))

(defrecord GaComponent [token]
  component/Lifecycle
  (start [component] component)
  (stop [component] component))

(defn ga-component [config]
  (->GaComponent (:token config)))
