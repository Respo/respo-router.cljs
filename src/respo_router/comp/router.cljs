
(ns respo-router.comp.router
  (:require [respo.alias :refer [create-comp div span]]
            [respo-router.util.listener :refer [ignored?-ref]]))

(defn render [router-path]
  (fn [state mutate!]
    (let [current-hash (.-hash js/location)
          new-hash (str "#" router-path)]
      (if (not= current-hash new-hash)
        (do
          (println "force set path to:" new-hash)
          (reset! ignored?-ref true)
          (set! (.-hash js/location) new-hash)
          (js/setTimeout
            (fn []
              (reset! ignored?-ref false)
              (println "ignore end")))))
      (span {}))))

(def comp-router (create-comp :router render))
