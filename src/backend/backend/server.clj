(ns backend.server
  (:require
   [com.stuartsierra.component :as component]
   [ring.adapter.jetty :as jetty]
   [ring.util.response :as resp]
   [datomic.api :as d]))

(defn handler [{:keys [db] :as this}]
  (fn [req]
    (prn req)
    (let [conn (:conn db)
          request-count
          (-> (d/entity (d/db conn) :my-app/request-count)
              (get :my-app/request-count 0)
              (inc))
          _txr
          @(d/transact
            (:conn db)
            [[:db/add :my-app/request-count :my-app/request-count request-count]])]
      (resp/response
       (str "Requests so far: " request-count)))))

(defrecord Server [db config
                   jetty]
  component/Lifecycle
  (start [this]
    (assoc this
      :jetty (jetty/run-jetty (handler this)
                              {:join? false
                               :port (:port config 80)})))
  (stop [this]
    (.stop jetty)
    (-> this
        (dissoc :jetty))))
