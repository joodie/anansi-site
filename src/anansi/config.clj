(ns anansi.config)

(def *config* nil)

(defmacro with-config
  [config & body]
  `(binding [*config* ~config]
     ~@body))
