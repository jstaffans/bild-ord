(defproject bild-ord "0.1.0-SNAPSHOT"
  :description "Swedish-language word game"
  :url "http://bildord.kjellstaffans.fi"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0-alpha5"]
                 [com.stuartsierra/component "0.3.1"]
                 [jarohen/embed-nrepl "0.1.7"]
                 [compojure "1.5.0" :exclusions [medley]]
                 [medley "0.8.2"]
                 [duct "0.5.10"]
                 [environ "1.0.2"]
                 [meta-merge "0.1.1"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [ring/ring-anti-forgery "1.0.1"]
                 [ring-jetty-component "0.3.1"]
                 [org.slf4j/slf4j-nop "1.7.14"]
                 [hiccup "1.0.5"]
                 [duct/hikaricp-component "0.1.0"]
                 [duct/ragtime-component "0.1.4"]
                 [com.rpl/specter "0.10.0"]
                 [org.xerial/sqlite-jdbc "3.8.11.2"]
                 [com.layerware/hugsql "0.4.7"]
                 [buddy "1.0.0"]
                 [dire "0.5.4"]
                 [ginsite "0.1.1"]

                 [ring-webjars "0.1.1"]
                 [org.webjars/jquery-ui-themes "1.11.4"]

                 [org.clojure/clojurescript "1.9.36"]
                 [reagent "0.5.1" :exclusions [cljsjs/react]]
                 [cljsjs/react-with-addons "0.13.3-0"]
                 [re-frame "0.7.0-alpha-2"]
                 [bidi "2.0.9"]
                 [cljsjs/jquery "1.12.2-0"]
                 [cljsjs/jquery-ui "1.11.4-0"]
                 [cljsjs/google-analytics "2015.04.13-0"]
                 [kibu/pushy "0.3.6"]]
  :plugins [[lein-environ "1.0.2"]
            [lein-shell "0.5.0"]
            [lein-gen "0.2.2"]
            [lein-cljsbuild "1.1.2"]
            [lein-scss "0.2.3"]]

  :generators [[duct/generators "0.5.10"]]
  :duct {:ns-prefix bild-ord}
  :main ^:skip-aot bild-ord.main
  :target-path "target/%s/"
  :source-paths ["src/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :prep-tasks [["javac"]
               ["cljsbuild" "once" "prod"]
               ["scss" ":prod" "once"]
               ["shell" "./bust-cache.sh"]
               ["compile"]]

  :scss {:builds {:dev  {:source-dir "src/scss/"
                         :dest-dir   "resources/bild_ord/public/css/"
                         :executable "sassc"
                         :args       ["-m" "-I" "scss" "-t" "nested"]}
                  :prod {:source-dir "src/scss/"
                         :dest-dir   "resources/bild_ord/public/css/"
                         :executable "sassc"
                         :args       ["-I" "scss" "-t" "compressed"]}}}

  :cljsbuild
  {:builds
   {:prod {:jar true
           :source-paths ["src"]
           :compiler {:output-to "target/cljsbuild/bild_ord/public/js/main.js"
                      :optimizations :advanced}}}}

  :aliases {"gen"   ["generate"]
            "setup" ["do" ["generate" "locals"]]}

  :profiles
  {:dev  [:project/dev  :profiles/dev]
   :test [:project/test :profiles/test]
   :repl {:resource-paths ^:replace ["resources" "target/figwheel"]
          :prep-tasks     ^:replace [["javac"] ["compile"]]}
   :uberjar {:aot :all}
   :profiles/dev  {}
   :profiles/test {}
   :project/dev   {:dependencies [[reloaded.repl "0.2.1"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [eftest "0.1.1"]
                                  [kerodon "0.7.0"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [duct/figwheel-component "0.3.2"]
                                  [figwheel "0.5.0-6"]
                                  [binaryage/devtools "0.6.1"]
                                  [philoskim/debux "0.2.1"]
                                  [clj-webdriver "0.7.2"]
                                  [com.codeborne/phantomjsdriver "1.2.1"
                                   :exclusion [org.seleniumhq.selenium/selenium-java
                                               org.seleniumhq.selenium/selenium-server
                                               org.seleniumhq.selenium/selenium-remote-driver]]
                                  [org.seleniumhq.selenium/selenium-java "2.47.1"]]
                   :source-paths ["dev"]
                   :repl-options {:init-ns user
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :env {:port "3000"}}
   :project/test  {}})
