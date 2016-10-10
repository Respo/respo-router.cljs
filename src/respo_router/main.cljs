
(ns respo-router.main
  (:require [respo.core :refer [render! clear-cache!]]
            [respo-router.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [respo-router.util.listener :refer [listen! parse-address]]
            [respo-router.schema :as schema]
            [respo-router.core :refer [render-url!]]))

(def dict
 {"home" [], "room" ["room-id"], "team" ["team-id"], "search" []})

(defonce store-ref (atom schema/store))

(defn dispatch! [op op-data]
  (println "dispatch!" op op-data)
  (let [new-store (case
                    op
                    :router/route
                    (assoc @store-ref :router op-data)
                    :router/nav
                    (assoc
                      @store-ref
                      :router
                      (parse-address op-data dict))
                    @store-ref)]
    (reset! store-ref new-store)))

(defonce states-ref (atom {}))

(defn render-app! []
  (println "render-app:" @store-ref)
  (let [target (.querySelector js/document "#app")]
    (render! (comp-container @store-ref) target dispatch! states-ref)))

(defn on-jsload []
  (clear-cache!)
  (render-app!)
  (println "code update."))

(def router-mode :history)

(defn render-router! []
  (render-url! (:router @store-ref) dict router-mode))

(defn -main []
  (enable-console-print!)
  (render-app!)
  (listen! dict dispatch! router-mode)
  (render-router!)
  (add-watch store-ref :changes render-app!)
  (add-watch states-ref :changes render-app!)
  (add-watch store-ref :router-changes render-router!)
  (println "app started!"))

(set! (.-onload js/window) -main)
