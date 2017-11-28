
(defn read-password [guide]
  (String/valueOf (.readPassword (System/console) guide nil)))

(set-env!
  :resource-paths #{"src"}
  :dependencies '[]
  :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"
                                     :username "jiyinyiyong"
                                     :password (read-password "Clojars password: ")}]))

(def +version+ "0.4.0")

(deftask deploy []
  (comp
    (pom :project     'respo/router
         :version     +version+
         :description "Respo Router"
         :url         "https://github.com/Respo/respo-router"
         :scm         {:url "https://github.com/Respo/respo-router"}
         :license     {"MIT" "http://opensource.org/licenses/mit-license.php"})
    (jar)
    (push :repo "clojars" :gpg-sign false)))
