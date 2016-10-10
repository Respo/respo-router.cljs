
(ns respo-router.core
  (:require [respo-router.util.format :refer [router->string]]
            [respo-router.util.listener :refer [parse-address
                                                ignored?-ref
                                                strip-sharp]]))

(def cached-router-ref (atom nil))

(defn render-url! [router dict router-mode]
  (if (exists? js/location)
    (if (not (identical? router @cached-router-ref))
      (do
        (reset! cached-router-ref router)
        (case
          router-mode
          :hash
          (let [current-hash (.-hash js/location)
                old-router (parse-address
                             (strip-sharp current-hash)
                             dict)]
            (if (not= old-router router)
              (let [new-hash (str "#" (router->string router dict))]
                (comment println "force set path to:" new-hash)
                (reset! ignored?-ref true)
                (set! (.-hash js/location) new-hash)
                (js/setTimeout
                  (fn []
                    (reset! ignored?-ref false)
                    (comment println "ignore end"))))))
          :history
          (let [old-address (str
                              (.-pathname js/location)
                              (.-search js/location))
                old-router (parse-address old-address dict)
                new-address (router->string router dict)]
            (if (not= old-router router)
              (.pushState js/history nil nil new-address)))
          nil)))))
