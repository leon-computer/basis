(ns backend.system
  (:require
   [com.stuartsierra.component :as component]
   [backend.db :refer [map->Db]]
   [backend.server :refer [map->Server]]))

(defn- using [ctor deps] (fn [m] (component/using (ctor m) deps)))

(def constructors
  {:db map->Db
   :server (-> map->Server (using [:db]))})

(defn configure-system
  "Produces an (unstarted) system-map applying each ctor to the
  corresponding config, a key in config, merged ontop on :global, a
  key in config."
  [config]
  (into (component/system-map)
        (map (fn [[k ctor]]
               [k (ctor {:config (merge (:global config)
                                        (get config k))})]))
        constructors))
