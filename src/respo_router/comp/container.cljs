
(ns respo-router.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div span]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-text]]
            [respo-router.comp.router :refer [comp-router]]
            [respo-value.comp.value :refer [render-value]]
            [respo-ui.style :as ui]))

(defn route-team [e dispatch!]
  (dispatch!
    :router/route
    {:router nil, :name "team", :query {}, :data {"team-id" "t1234"}}))

(defn route-404 [e dispatch!] (dispatch! :router/nav "/missing"))

(defn route-room [e dispatch!]
  (dispatch!
    :router/route
    {:router
     {:router nil,
      :name "room",
      :query {"a" 1, "b" 2},
      :data {"room-id" "r1234"}},
     :name "team",
     :query {},
     :data {"team-id" "t12345"}}))

(defn route-search [e dispatch!] (dispatch! :router/nav "search"))

(defn route-home [e dispatch!] (dispatch! :router/nav "/home"))

(defn render [store dict router-mode]
  (fn [state mutate!]
    (div
      {:style (merge ui/global ui/row)}
      (div
        {}
        (div
          {:style ui/button, :event {:click route-home}}
          (comp-text "home" nil))
        (div
          {}
          (div
            {:style ui/button, :event {:click route-team}}
            (comp-text "team" nil))
          (div
            {:style ui/button, :event {:click route-room}}
            (comp-text "room" nil)))
        (div
          {}
          (div
            {:style ui/button, :event {:click route-search}}
            (comp-text "search" nil)))
        (div
          {}
          (div
            {:style ui/button, :event {:click route-404}}
            (comp-text "404" nil))))
      (render-value (:router store))
      (comp-router (:router store) dict router-mode))))

(def comp-container (create-comp :container render))
