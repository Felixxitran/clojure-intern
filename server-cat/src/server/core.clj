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



  


(defn counting []
  (println "counted"))

(defn change-data []
  (let [conn (mg/connect)
        db   (mg/get-db conn "countries")]
  (mc/insert-and-return db "countries" {:name "Germany" :official-language "German" :population 82000000})))
(defn get-data []
  (let [conn (mg/connect)
        db   (mg/get-db conn "countries")]
   (mc/find db "countries" {:name "Germany"})
  (mc/find-maps db "countries" {:name "Germany"})))

(defn remove-data []
  (let [conn (mg/connect)
        db   (mg/get-db conn "countries")]
  (mc/remove db "countries" {:name "Germany"})))


(defroutes app
  (GET "/cat" [] "hello world")
  (GET "/get-data" [] (get-data))
  (POST "/remove-data" [] (remove-data))
  (POST "/change-data" [] (change-data)))
  
(defn start-server [] 
  (counting)
  (run-server app {:port 3000}))

(defn -main
  []
  (start-server)
  )


