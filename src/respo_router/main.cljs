
(ns respo-router.main
  (:require [respo.core :refer [render! clear-cache!]]
            [respo-router.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [respo-router.util.listener :refer [listen! parse-address]]
            [respo-router.schema :as schema]
            [respo-router.core :refer [render-url!]]))

(def dict {"home" [], "team" ["team-id"], "room" ["room-id"], "search" []})

(defonce store-ref (atom schema/store))

(defn dispatch! [op op-data]
  (println "dispatch!" op op-data)
  (let [new-store (case op
                    :router/route (assoc @store-ref :router op-data)
                    :router/nav (assoc @store-ref :router (parse-address op-data dict))
                    @store-ref)]
    (reset! store-ref new-store)))

(def router-mode :history)

(defn render-router! [] (render-url! (:router @store-ref) dict router-mode))

(defn render-app! []
  (println "render-app:" @store-ref)
  (let [target (.querySelector js/document "#app")]
    (render! target (comp-container @store-ref) dispatch!)))

(defn main! []
  (render-app!)
  (listen! dict dispatch! router-mode)
  (render-router!)
  (add-watch store-ref :changes render-app!)
  (add-watch store-ref :router-changes render-router!)
  (println "app started!"))

(defn reload! [] (clear-cache!) (render-app!) (println "code update."))

(set! (.-onload js/window) main!)
