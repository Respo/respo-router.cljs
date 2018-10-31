
(ns respo-router.main
  (:require [respo.core :refer [render! clear-cache!]]
            [respo.cursor :refer [mutate]]
            [respo-router.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [respo-router.listener :refer [listen!]]
            [respo-router.parser :refer [parse-address]]
            [respo-router.format :refer [strip-sharp]]
            [respo-router.schema :as schema]
            [respo-router.core :refer [render-url!]]
            [respo-router.schema :refer [dict]]))

(defonce *store
  (atom
   (assoc schema/store :router (parse-address (strip-sharp js/window.location.hash) dict))))

(defn dispatch! [op op-data]
  (println "dispatch!" op op-data)
  (let [new-store (case op
                    :states (update @*store :states (mutate op-data))
                    :router/route (assoc @*store :router op-data)
                    :router/nav (assoc @*store :router (parse-address op-data dict))
                    @*store)]
    (reset! *store new-store)))

(def mount-target (.querySelector js/document ".app"))

(defn render-app! []
  (comment println "render-app:" @*store)
  (render! mount-target (comp-container @*store) dispatch!))

(def router-mode :hash)

(defn render-router! [] (render-url! (:router @*store) dict router-mode))

(defn main! []
  (render-app!)
  (listen! dict dispatch! router-mode)
  (render-router!)
  (add-watch *store :changes render-app!)
  (add-watch *store :router-changes render-router!)
  (println "app started!"))

(defn reload! [] (clear-cache!) (render-app!) (println "code update."))
