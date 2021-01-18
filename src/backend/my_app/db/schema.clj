(ns my-app.db.schema)

(def migrations
  ["Request count"
   [{:db/ident :my-app/request-count
     :db/valueType :db.type/long
     :db/cardinality :db.cardinality/one
     :db/doc
     "Request counter.  In this example, this attribute is only used on one entity, which is itself, as a singleton."}]])
