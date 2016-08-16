
(ns respo-router.comp.router
  (:require [respo.alias :refer [create-comp div span]]))

(defn render [router]
  (fn [state mutate!] (println "router renders:" router) (span {})))

(def comp-router (create-comp :router render))
