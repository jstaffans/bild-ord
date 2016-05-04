(ns cljs.user
  (:require [bild-ord.endpoint.app :refer [main]]
            [devtools.core :as devtools]
            [figwheel.client :as figwheel]))

(js/console.info "Starting in development mode")

(enable-console-print!)

(devtools/install!)

(figwheel/start {:websocket-url "ws://localhost:3449/figwheel-ws"})
