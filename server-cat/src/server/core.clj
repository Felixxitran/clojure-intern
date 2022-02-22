(ns server.core
  (:gen-class)

  (:require [compojure.core :refer [defroutes GET POST context]]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]
            [cheshire.core :as json]
            [monger.core :as mg]
            [monger.collection :as mc]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.util.response :as response]
            [ring.util.request :as request]
            [ring.middleware.params :refer [wrap-params]]
            )
   (:import [org.bson.types ObjectId]
           [com.mongodb DB WriteConcern]
            ))
;;connect to mongodb database


(defn counting []
  (println "counted"))
;;json-parse request 
;; (defn wrap-body-string [handler]
;;   (fn [request]
;;     (let [body-str (ring.util.request/body-string request)]
;;       (handler (assoc request :body (java.io.StringReader. body-str))))))

;;adding middle ware 
;; (defn echo-handler [{params :params}]
;;      (if (some? params)
;;        (let [req-body (get params "input")]
;;          {:status 200, :headers {}, :body "Hello"}
;;          )))
;;  (def app-handler
;;   (-> echo-handler
;;       (wrap-params {:encoding "UTF-8"})
;;   ))    

(defn insert-data [request]
  
  )

(defn get-data [{params :params}]
  )

(defn remove-data []
  (let [conn (mg/connect)
        db   (mg/get-db conn "countries")]
  (mc/remove db "countries" {:name "Germany"})))


(defroutes app
  (GET "/cat" [] "started")
  (GET "/get-data" [request] (get-data [request]))
   
  (GET "/cat/:id" [id] 
      (let [conn (mg/connect)
        db   (mg/get-db conn "countries")
        ;; req-body (json/parse-string (slurp request) true)
        

    return (mc/find db "countries" {:name id})
    return_2 (mc/find-maps db "countries" {:name id}) ]     
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:response id})
      }              
)
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:id id})
      }  )
    (GET "/test/:id" [id]
      (try
        (let [conn (mg/connect)
        db   (mg/get-db conn "countries")
        ;; req-body (json/parse-string (slurp request) true)
        

    return (mc/find db "countries" {:name id})
    return_2 (mc/find-maps db "countries" {:name id}) ]     
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:response return})
      }              
)
        ;; {:status 200
        ;;  :headers {"Content-Type" "application/json"}
        ;;  :body (json/encode {:id id})}
        (catch Exception e
          (println "there is not response"))
      )
    )
  
  

  (POST "/remove-data" [] (remove-data))
  (GET "/insert-data" [] 
    (try
  (let [conn (mg/connect)
        db   (mg/get-db conn "countries")
        ;;the following line of code can cause crash 
        ;; body (slurp (:body request)) 
        ]
  (mc/insert-and-return db "countries" { :_id (ObjectId.) :name "John" :last_name "Lennon" })
    {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/encode {:message "add successful"})
      } 
    )
  (catch Exception e
    (println "response is fucking nil")))
    ))

  
(defn start-server [] 
  (counting)
  (run-jetty app {:port 3000}))

(defn -main
  []
  (start-server)
  )


