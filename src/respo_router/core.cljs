
(ns respo-router.core
  (:require [respo.core :refer [render! clear-cache!]]
            [respo-router.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [respo-router.util.listener :refer [listen!]]
            [respo-router.schema :as schema]))

(def dict
 {"home" [],
  "room" ["room-id" "kind"],
  "team" ["team-id"],
  "search" []})

(defonce store-ref (atom schema/store))

(defonce states-ref (atom {}))

(defn dispatch! [op op-data]
  (let [new-store (case
                    op
                    :router/route
                    (assoc @store-ref :router op-data)
                    @store-ref)]
    (reset! store-ref new-store)))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (render!
      (comp-container @store-ref dict)
      target
      dispatch!
      states-ref)))

(defn on-jsload []
  (clear-cache!)
  (render-app!)
  (println "code update."))

(defn -main []
  (enable-console-print!)
  (render-app!)
  (listen! dict dispatch!)
  (add-watch store-ref :changes render-app!)
  (add-watch states-ref :changes render-app!)
  (println "app started!"))

(set! (.-onload js/window) -main)
