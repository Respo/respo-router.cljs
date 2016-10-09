
(ns respo-router.comp.router
  (:require [respo.alias :refer [create-comp div span]]
            [respo-router.util.listener :refer [ignored?-ref
                                                parse-address
                                                strip-sharp]]
            [respo-router.util.format :refer [router->string]]))

(defn render [router dict router-mode]
  (fn [state mutate!]
    (if (exists? js/location)
      (case
        router-mode
        :hash
        (let [current-hash (.-hash js/location)
              old-router (parse-address
                           (strip-sharp current-hash)
                           dict)]
          (if (not= old-router router)
            (let [new-hash (str "#" (router->string router dict))]
              (println "force set path to:" new-hash)
              (reset! ignored?-ref true)
              (set! (.-hash js/location) new-hash)
              (js/setTimeout
                (fn []
                  (reset! ignored?-ref false)
                  (println "ignore end"))))))
        :history
        (println "history mode not finished yet")
        nil))
    (span {})))

(def comp-router (create-comp :router render))
