(ns anansi.config)

(def *config* nil)

(defmacro with-config
  [config & body]
  `(binding [*config* ~config]
     ~@body))

(defn wrap-config
  [handler config]
  (fn [request]
    (with-config config
      (handler (assoc request :config config)))))