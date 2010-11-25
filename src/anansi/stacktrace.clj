(ns anansi.stacktrace
  (:require [clj-stacktrace.repl :as trace]))

(defn wrap-stacktrace
  "On exceptions, dump clj-stacktrace output to *out* and return a 500 server error to the user"
  [h]
  (fn [r]
    (try
      (h r)
      (catch Exception e
        (.append *out* (str "Exception on uri " (:uri r) "\n"))
        (trace/pst e)
        {:status 500
         :headers {"Content-Type" "text/plain"}
         :body "Server Error"}))))

