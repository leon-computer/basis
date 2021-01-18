(ns dev
  (:require
   [my-app.system :refer [configure-system]]
   [leon.computer.repl.reloaded :refer [set-system-to-load! reset system]]
   [datomic.api :as d])
  (:use
   [clojure.repl]))

;; Knobs
(def LOAD-CFG
  "Which config to load (see configs below)"
  :mem #_:staging #_:live)

(def RESET-DEV-DB
  "Whether to delete in memory db across calls to reset"
  true)

(def configs
  "Configurations available in this namespace"
  {:mem
   {:db
    {:uri "datomic:mem://mem-db"
     :reset-db? RESET-DEV-DB
     :no-migrations? false}
    :server
    {:port 8080}}})

(set-system-to-load!
 (configure-system (get configs LOAD-CFG)))

;; Shortcuts
(defn conn []
  (-> system :db :conn))

(defn db []
  (-> (conn) (d/db)))

(defn touch
  [e]
  (d/touch (d/entity (db) e)))
