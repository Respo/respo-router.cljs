
(ns respo-router.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div span]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-text]]
            [respo-router.style.widget :as widget]
            [respo-router.comp.router :refer [comp-router]]
            [respo-value.comp.value :refer [render-value]]
            [respo-router.util.format :refer [router->string]]
            [respo-ui.style :as ui]))

(defn route-team [e dispatch!]
  (dispatch!
    :router/route
    {:sub nil, :name "team", :query {}, :data {"team-id" "t1234"}}))

(defn route-room [e dispatch!]
  (dispatch!
    :router/route
    {:sub
     {:sub nil,
      :name "room",
      :query {"a" 1, "b" 2},
      :data {"room-id" "r1234"}},
     :name "team",
     :query {},
     :data {"team-id" "t12345"}}))

(defn route-search [e dispatch!]
  (dispatch!
    :router/route
    {:sub nil, :name "search", :query {}, :data {}}))

(defn route-home [e dispatch!]
  (dispatch!
    :router/route
    {:sub nil, :name "home", :query {}, :data {}}))

(defn render [store dict router-mode]
  (fn [state mutate!]
    (div
      {:style (merge widget/global ui/row)}
      (render-value (:router store))
      (div
        {:style {}}
        (div
          {:style ui/button, :event {:click route-home}}
          (comp-text "home" nil))
        (div
          {:style {}}
          (div
            {:style ui/button, :event {:click route-team}}
            (comp-text "team" nil))
          (div
            {:style ui/button, :event {:click route-room}}
            (comp-text "room" nil)))
        (div
          {:style ui/button, :event {:click route-search}}
          (comp-text "search" nil)))
      (comp-router (router->string (:router store) dict) router-mode))))

(def comp-container (create-comp :container render))
