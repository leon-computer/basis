#!/bin/bash
set -ex
clojure -J-XX:-OmitStackTraceInFastThrow -Afrontend:frontend-dev:cider-nrepl -Sverbose scripts/cljs_nrepl.clj
