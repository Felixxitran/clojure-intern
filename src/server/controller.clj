(ns server.controller
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :refer :all]
            [monger.conversion :refer [from-db-object]]
            ))
(defn connect-mongo []
  (let [conn (mg/connect)
        db   (mg/get-db conn "users")]
    {:connect db :col "users"})
  )
(defn find-maps [{input :object}]
  (let [db ((connect-mongo) :connect)
          col ((connect-mongo) :col)]
    (prn input)
     (def return (from-db-object (mc/find-maps db col input) true))
    (prn return)
    {:data return}
     )
  )
(defn find-with-params [input]
  (let [db ((connect-mongo) :connect)
        col ((connect-mongo) :col)
        page (input :page)
        lim (input :lim)
        ]
    (def return (with-collection db col
                  (find {})
                  (paginate :page page :per-page lim)))
    {:data return}
    )
  )
(defn create-users [input]
  (let [db ((connect-mongo) :connect)
        col ((connect-mongo) :col)]
    (mc/insert db col input)
    ))

(defn delete-users [id]
  (let [db ((connect-mongo) :connect)
        col ((connect-mongo) :col)]
    (mc/remove db "users" {:_id (long (bigdec id))})
    )
  )

(defn find-one-user [id]
  (let [db ((connect-mongo) :connect)
        col ((connect-mongo) :col)]
   (def user (from-db-object (mc/find-one db "users" {:_id id}) true))
   {user :user})
  )
(defn save-user [id save]
  (let [db ((connect-mongo) :connect)
        col ((connect-mongo) :col)]
    (mc/update-by-id db "users" id save)
    )
  )
;; (defn get-by-role [role]
;;   (let [db ((connect-mongo) :connect)
;;         col ((connect-mongo) :col)]
;;     (def dt (from-db-object (mc/find-maps db "users" {:role role}) true))
;;     {dt :dt}
;;     )
;;   )
(defn get-by-id [id]
  (let [db ((connect-mongo) :connect)
        col ((connect-mongo) :col)]
    (def user-got-by-id (from-db-object (mc/find-maps db "users" {:id id}) true))
    {user-got-by-id :dt}
    )
  )
;; (defn get-by-name [name]
;;   (let [db ((connect-mongo) :connect)
;;         col ((connect-mongo) :col)]
;;     (def dt (from-db-object (mc/find-maps db "users" {:name name}) true))
;;     {dt :dt}
;;     )
;;   )
