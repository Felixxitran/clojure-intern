(ns server.core
  (:gen-class)
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]
            [cheshire.core :as json]
            [monger.core :as mg]
            [monger.collection :as mc]
            )
   (:import [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern]))
;;connect to mongodb database

  (let [conn (mg/connect)
        db   (mg/get-db conn "monger-test")]


(defn counting []
  (println "counted"))


(defn change-data [db]
  (mc/insert db "countries" {:name "Germany" :official-language "German" :population 82000000}))
(defn get-data [db]
   (mc/find db "countries" {:name "Germany"})
  (mc/find-maps db "countries" {:name "Germany"}))
(defn remove-data [db]
  (mc/remove db "countries" {:name "Germany"}))


(defroutes app [db]
  (GET "/cat" [] "Hello world")
  (GET "/get-data" [db] (get-data [db]))
  (POST "/remove-data" [db] (remove-data [db]))
  )
(defn start-server []
  
  (counting)
  (run-server app {:port 3000})))

(defn -main
  []
  (start-server)
  )


