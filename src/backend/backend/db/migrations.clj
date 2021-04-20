(ns backend.db.migrations
  "Minimalist tool for incremental Datomic schema migrations"
  (:require
   [datomic.api :as d]))

(def base-schema-txn
  [[{:db/ident ::migration-descr
     :db/cardinality :db.cardinality/one
     :db/valueType :db.type/string
     :db/unique :db.unique/value
     :db/doc "Unique string describing a migration "}]])

(defn install-base-schema [conn]
  (doseq [base-schema-tx base-schema-txn]
    @(d/transact conn base-schema-tx)))

(defn- first-dup
  "Return the first value that occurs twice in coll"
  [coll]
  (loop [[x & xs] coll
         seen #{}]
    (if x
      (if (contains? seen x)
        x
        (recur xs (conj seen x))))))

(defn- ensure-format
  [migrations]
  (let [parts (partition 2 migrations)
        descrs (map first parts)]
    (when-let [dup (first-dup descrs)]
      (throw (IllegalArgumentException.
              (str "Duplicate descr: " dup))))
    (doseq [[descr tx :as part] parts]
      (when-not (and (string? descr)
                     (vector? tx))
        (throw (IllegalArgumentException.
                (pr-str "Invalid migration tx " part)))))))

(defn migrate
  "Transact new transactions from migrations, a flat vector of descr:tx
  pairs.  descr is a string and must over time identify the
  transaction following it.  A descr is installed on the resulting
  transaction entity at ::migration-descr.  Intentionally 1:1 descr:tx
  mapping, so that a descr can't succeed partially.  Returns the
  dereferenced transaction result."
  [conn migrations]
  (ensure-format migrations)
  (doseq [[descr tx] (partition 2 migrations)
          :when (empty? (d/entity (d/db conn) [::migration-descr descr]))]
    (try
      @(d/transact conn
                   (conj tx [:db/add (d/tempid :db.part/tx) ::migration-descr descr]))
      (catch Exception e
        (throw (ex-info "Error transacting migration"
                        {:descr descr
                         :tx tx}
                        e))))))
