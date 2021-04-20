(require '[nrepl.server :as server]
         '[cider.nrepl :as nrepl]
         '[nrepl.transport])

(defn reset []
  (eval
   '(do
      (println "Performing initial load of project.  If this fails, you must fix reported errors in the reported namespace, and then invoke (require 'reported-namespace :reload-all) or reload buffer in cider, and then invoke (reset) again.  Once the first reset succeeded, you will only have to invoke (reset) after a fix.")
      (require 'backend.dev)
      (in-ns 'backend.dev)
      (reset))))

(def server (atom nil))

(reset! server
        (server/start-server :port 7889
                             :handler
                             (apply server/default-handler
                                    (map resolve nrepl/cider-middleware))
                             ))
(println "Started:" @server)
