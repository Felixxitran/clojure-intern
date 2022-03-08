(ns server.routes-function
  (:use ring.util.response)
  (:require [server.controller :refer :all]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c]
            [crypto.password.bcrypt :as password]
            )
  )

(defn get [params]
  (let [page (Integer/parseInt (params :page))
          lim (Integer/parseInt (params :lim))
          input {:page page :lim lim}
          ]
      (prn params)
      (def list_of_users ((find-with-params input) :data))
      (response list_of_users))
  )
(defn delete [id]
  (if (id)
      (let []
        (delete-users id)
        (prn (type (long (bigdec id))))
        (response "done")
        ))
  )
(defn create [request]
  (let []
      ;;create id 
      (def now (c/to-long (t/now)))
      ;;encrypted password 
      (def pass (request :password))
      (prn request)
      (def encrypted (password/encrypt pass))
      (def save (assoc request :password encrypted))

      (def save_final (update save :_id (constantly now)))
      ;; check role
      (def roles ["admin" "manager" "bd" "recruiter" "marketing" "developer" "team_member" "team_agency" "candidate"])
      (if (some #(= (request :role) %) roles)
        (do
          (create-users save_final)
          (response save_final))
        (response "can not save, check again the role")))
  )
(defn get-user-by-id [id]
  (
     let []
      (def user-info ((get-by-id id) :dt))
      (response user-info))
  )

(defn change-user-by-id [request id]
  (let [id (long (bigdec id))]
    (prn id)
    (prn request)
    (if id
      ((if (request :name)
         (let [name (request :name)]
           (prn name)
           (def data ((find-one-user id) :user))
           (prn data)
           (def save (assoc data :name name))
           (prn save)
           (save-user id save)))
       (if (request :age)
         (let [age (request :age)]
           (prn age)
           (def data ((find-one-user id) :user))
           (prn data)
           (def save (assoc data :age age))
           (prn save)
           (save-user id save)))
       (if (request :role)
         (let [role (request :role)]
           (prn role)
           (def data ((find-one-user id) :user))
           (prn data)
           (def save (assoc data :role role))
           (prn save)
           (save-user id save)))
       (if (request :email)
         (let [email (request :email)]
           (prn email)
           (def data ((find-one-user id) :user))
           (prn data)
           (def save (assoc data :email email))
           (prn save)
           (save-user id save))))))
  (response "done updating")
  )