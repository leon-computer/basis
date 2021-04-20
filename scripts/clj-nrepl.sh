#!/bin/bash
set -ex
clojure -J-XX:-OmitStackTraceInFastThrow -Abackend:backend-dev:cider-nrepl -Sverbose scripts/clj_nrepl.clj
