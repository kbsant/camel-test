(ns camel-test.core
  (:require [clj-camel.core :as c]
            [clojure.tools.logging :as log]
            [environ.core :refer [env]])
  (:import (org.apache.camel Exchange))
  (:gen-class))

(def http-host
  (or
   (env :camel-test-http-host)
   "localhost"))

(def http-path
  (or
   (env :camel-test-http-path)
   "guest"))

(defn log-header-keys-ex [^Exchange ex]
  (log/info "headers:" (.keySet (c/get-in-headers ex))))

(def route1 (c/route-builder (c/from "timer:test?delay=-1&repeatCount=2")
                             (c/log "route1 ${body}")
                             (c/to "direct:test")))

(def route2 (c/route-builder (c/from "direct:test")
                             (c/route-id "test-route")
                             (c/set-body (c/constant "test-body"))
                             (c/set-header Exchange/HTTP_METHOD (c/constant "GET"))
                             (c/set-header Exchange/HTTP_PATH (c/constant http-path))
                             (c/log "route2 request: ${body}")
                             (c/to (str "https://" http-host))
                             (c/log "route2 response headers: ${headers}")
                             (c/log "route2 response cookie: ${headers.Set-Cookie}")
                             (c/process-ex log-header-keys-ex)
                             (c/to "log:output?level=INFO")
                             (c/to (str "file:" (env :home) "/tmp"))
                             ))

(defn run-route []
  (System/setProperty "javax.net.ssl.keyStore"
                      (str (env :java-home) "lib/security/cacerts"))
  (let [ctx (c/camel-context)]
    (c/add-routes ctx route1 route2)
    (.start ctx)
    (Thread/sleep 10000)
    (.shutdown ctx)))

(defn -main
  [& _]
  (run-route))

#_ (comment
     "REPL - C-M-x within the expression, or C-x-e after the expression to evaluate it in the repl."
(require '[camel-test.core :as ctest])
(ctest/run-route)
     )
