(require '[nrepl.server :as server]
         '[cider.piggieback :as piggieback]
         '[cider.nrepl :as nrepl])

(defn figwheel [] (load-file "scripts/figwheel.clj"))

(def server (atom nil))

(reset! server
        (server/start-server :port 7888
                             :handler
                             (apply server/default-handler
                                    (cons #'piggieback/wrap-cljs-repl
                                          (map resolve nrepl/cider-middleware)))
                             :greeting "Welcome!"))
(println "Started:" @server)
