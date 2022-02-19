(ns server.core
  (:gen-class)
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]
            [cheshire.core :as json]))

(defroutes app
  (GET "/" [] "Hello world"))
(defn -main
  []
  (run-server app {:port 3000}))


