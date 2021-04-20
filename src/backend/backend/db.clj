(ns backend.db
  (:require
   [com.stuartsierra.component :as component]
   [datomic.api :as d]
   [clojure.string :as str]
   [backend.db.migrations :refer [install-base-schema migrate]]
   [backend.db.schema :refer [migrations]]))

(defn produce-conn
  "Obtains and returns connection, if necessary creates database and
  installs schema."
  [{:keys [reset-db? no-migrations? uri] :as config}]
  (let [created? (do (when reset-db?
                       (d/delete-database uri))
                     (d/create-database uri))
        conn (d/connect uri)]
    (when created?
      (install-base-schema conn))
    (when (or created? ;; always install schema
              (not no-migrations?))
      (migrate conn migrations))
    conn))

(defrecord Db [config]
  component/Lifecycle
  (start [this]
    (assoc this
      :conn (produce-conn config)))
  (stop [this]
    (let [uri (:uri config)]
      (when-not (and (string? uri)
                     (str/starts-with? uri "datomic:mem"))
        ;; otherwise, in-memory db will be deleted
        (d/release (:conn this))))
    (dissoc this :conn)))
