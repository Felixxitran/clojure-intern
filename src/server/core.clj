(ns server.core
  (:use ring.util.response)
  (:refer-clojure :exclude [sort find])
  (:require [compojure.core :refer [defroutes GET POST context DELETE PUT]]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [cheshire.core :as json]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.request :as request]
            [ring.middleware.params :refer [wrap-params assoc-query-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [server.controller :refer :all]
            [server.routes-function :refer :all]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :refer :all]
            [monger.conversion :refer [from-db-object]])
  (:import [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern]
           ))
;;connect to mongodb database
(defn counting []
  (println "server started"))
(defn request-body->map
  [request]
  (json/parse-string (slurp (:body request)) true))
(defroutes app-routes
  (GET "/" [] "server is running")
  (GET "/test/read" {params :params} (response "testing read"))
  (GET "/users" {params :params} (get params))
  (GET "/users/:id" [id] (get-user-by-id id))
  (PUT "/users/:id" {request :body id :id} (change-user-by-id request id))
  (DELETE "/delete/:id" [id] (delete id))
  (POST "/users" {request :body} (create request)))
(def app
  (-> app-routes
      (wrap-json-body {:keywords? true})
      (wrap-keyword-params true)
      (wrap-params)
      (wrap-json-response)))
(defn start-server []
  (counting)
  (run-jetty app {:port 3000}))

(defn -main
  []
  (start-server))


