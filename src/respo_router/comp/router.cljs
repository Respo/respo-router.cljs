
(ns respo-router.comp.router
  (:require [respo.alias :refer [create-comp div span]]))

(defn render [router on-change] (fn [state mutate!] (span {})))

(def comp-router (create-comp :router render))
