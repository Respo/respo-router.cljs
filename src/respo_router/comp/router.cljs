
(ns respo-router.comp.router
  (:require [clojure.string :as string]
            [respo.alias :refer [create-comp div span]]
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
        (let [old-address (str
                            (.-pathname js/location)
                            (.-search js/location))
              old-router (parse-address old-address dict)
              new-address (router->string router dict)]
          (if (not= old-router router)
            (.pushState js/history nil nil new-address)))
        nil))
    (span {})))

(def comp-router (create-comp :router render))
