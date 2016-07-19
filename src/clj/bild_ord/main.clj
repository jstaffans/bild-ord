(ns bild-ord.main
  (:gen-class)
  (:require [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [duct.middleware.errors :refer [wrap-hide-errors]]
            [duct.util.runtime :refer [add-shutdown-hook]]
            [meta-merge.core :refer [meta-merge]]
            [bild-ord.config :as config]
            [bild-ord.system :refer [new-system]]
            [nrepl.embed :as nrepl]))

(def prod-config
  {:db  {:connection-uri "jdbc:sqlite:/var/lib/sqlite/bild_ord"}
   :app {:middleware     [[wrap-hide-errors :internal-error]]
         :internal-error (io/resource "errors/500.html")}})

(def config
  (meta-merge config/defaults
              config/environ
              prod-config))

(def system (atom nil))

(defn -main [& args]
  (reset! system (new-system config))

  (println "Starting nREPL on port 7888")
  (add-shutdown-hook ::stop-nrepl nrepl/stop-nrepl!)
  (nrepl/start-nrepl! {:bind "127.0.0.1" :port 7888})

  (println "Starting HTTP server on port" (-> @system :http :port))
  (add-shutdown-hook ::stop-system #(swap! system component/stop))
  (swap! system component/start))
