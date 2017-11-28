
(ns respo-router.core
  (:require [respo-router.util.format :refer [router->string]]
            [respo-router.util.listener :refer [parse-address *ignored? strip-sharp]]))

(def *cached-router (atom nil))

(defn render-url! [router dict router-mode]
  (assert (map? dict) "first argument should be router data")
  (assert (map? dict) "second argument should be dictionary")
  (assert (contains? #{:history :hash} router-mode) "last argument is router-mode")
  (if (exists? js/location)
    (if (not (identical? router @*cached-router))
      (do
       (reset! *cached-router router)
       (case router-mode
         :hash
           (let [current-hash (.-hash js/location)
                 old-router (parse-address (strip-sharp current-hash) dict)]
             (if (not= old-router router)
               (let [new-hash (str
                               "#"
                               (router->string "" (:path router) (:query router) dict))]
                 (comment println "force set path to:" new-hash)
                 (reset! *ignored? true)
                 (set! (.-hash js/location) new-hash)
                 (js/setTimeout
                  (fn [] (reset! *ignored? false) (comment println "ignore end"))))))
         :history
           (let [old-address (str (.-pathname js/location) (.-search js/location))
                 old-router (parse-address old-address dict)
                 new-address (router->string "" (:path router) (:query router) dict)]
             (if (not= old-router router) (.pushState js/history nil nil new-address)))
         nil)))))
