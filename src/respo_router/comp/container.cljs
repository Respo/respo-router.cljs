
(ns respo-router.comp.container
  (:require-macros [respo.macros :refer [defcomp div span <>]])
  (:require [hsl.core :refer [hsl]]
            [respo.core :refer [create-comp create-element]]
            [respo.comp.space :refer [=<]]
            [respo-value.comp.value :refer [render-value]]
            [respo-ui.style :as ui]))

(defn route-home [e dispatch!] (dispatch! :router/nav "/home"))

(defn route-room [e dispatch!]
  (dispatch!
   :router/route
   {:name "team",
    :data {"team-id" "t12345"},
    :query {},
    :router {:name "room", :data {"room-id" "r1234"}, :query {"a" 1, "b" 2}, :router nil}}))

(defn route-team [e dispatch!]
  (dispatch! :router/route {:name "team", :data {"team-id" "t1234"}, :router nil, :query {}}))

(defn route-search [e dispatch!] (dispatch! :router/nav "search"))

(defn route-404 [e dispatch!] (dispatch! :router/nav "/missing"))

(defcomp
 comp-container
 (store)
 (let [states (:states store)]
   (div
    {:style (merge ui/global ui/row)}
    (div
     {}
     (div {:style ui/button, :event {:click route-home}} (<> "home"))
     (div
      {}
      (div {:style ui/button, :event {:click route-team}} (<> "team"))
      (div {:style ui/button, :event {:click route-room}} (<> "room")))
     (div {} (div {:style ui/button, :event {:click route-search}} (<> "search")))
     (div {} (div {:style ui/button, :event {:click route-404}} (<> "404"))))
    (render-value states (:router store)))))
