(ns anansi.app.static.routes
  (:use [anansi handlers config]
        clojure.contrib.logging
        ring.util.response))

(def my-routes
  (handlers (:get "/*"
                  {{file-path :*} :params config :config :as request}
                  (file-response file-path {:root (str (:app-root config) "public")}))

            (nil "/*" {} "Not found")))
