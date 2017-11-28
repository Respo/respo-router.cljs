
(ns respo-router.listener
  (:require [clojure.string :as string]
            [respo-router.schema :as schema]
            [respo-router.parser :refer [parse-address]]
            [respo-router.format :refer [strip-sharp]]))

(def *ignored? (atom false))

(defn listen! [dict dispatch! router-mode]
  (assert (map? dict) "first argument should be a dictionary")
  (assert (fn? dispatch!) "second argument shoud be dispatch function")
  (assert
   (contains? #{:history :hash} router-mode)
   (str "invalid router-demo: " router-mode))
  (case router-mode
    :hash
      (.addEventListener
       js/window
       "hashchange"
       (fn [event]
         (let [path-info (parse-address (strip-sharp (.-hash js/location)) dict)]
           (comment println "is ignored?" @*ignored?)
           (if (not @*ignored?)
             (js/setTimeout (fn [] (dispatch! :router/route path-info)) 0)))))
    :history
      (.addEventListener
       js/window
       "popstate"
       (fn [event]
         (let [current-address (str (.-pathname js/location) (.-search js/location))
               path-info (parse-address current-address dict)]
           (dispatch! :router/route path-info))))
    nil))
