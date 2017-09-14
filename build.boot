
(set-env!
  :resource-paths #{"polyfill" "src"}
  :dependencies '[[mvc-works/hsl        "0.1.2"   :scope "provided"]
                  [mvc-works/polyfill   "0.1.1"]])

(def +version+ "0.6.0-rc6")

(deftask build []
  (comp
    (pom :project     'respo/router
         :version     +version+
         :description "Respo Router"
         :url         "https://github.com/Respo/respo-router"
         :scm         {:url "https://github.com/Respo/respo-router"}
         :license     {"MIT" "http://opensource.org/licenses/mit-license.php"})
    (jar)
    (install)
    (target)))

(deftask deploy []
  (set-env! :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"}]))
  (comp
    (build)
    (push :repo "clojars" :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))
