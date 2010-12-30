(defproject anansi-site "0.0.2"
  :description "basic compojure/ring based web framework"
  :dependencies [[anansi-handlers "1.0.0-SNAPSHOT"]
                 [hiccup "0.2.6"]
                 [swank-clojure "1.2.1"]
                 [clj-time "0.1.0-SNAPSHOT"]
                 [ring/ring-core "0.3.1"]
                 [ring/ring-jetty-adapter "0.3.1"]
                 [ring/ring-devel "0.3.1"]
                 [clj-stacktrace "0.2.0"]
                 [zeekat-utils "0.1.0"]]
  :dev-dependencies [[swank-clojure "1.3.0-SNAPSHOT"]
                     [clojure-refactoring "0.4.0"]])
