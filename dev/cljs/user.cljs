(ns cljs.user
  (:require [devtools.core :as devtools]
            [figwheel.client :as figwheel]
            bild-ord.app))  ;; need a require to make the namespace available

(js/console.info "Starting in development mode")

(enable-console-print!)

(devtools/install!)

(figwheel/start {:websocket-url "ws://localhost:3449/figwheel-ws"})
