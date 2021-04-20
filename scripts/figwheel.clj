#_(require 'clojure.spec.alpha)
#_(require 'expound.alpha)
#_(set! clojure.spec.alpha/*explain-out* expound.alpha/printer)

;; FW main
(require 'figwheel.main.api)

(figwheel.main.api/start "frontend-dev")
