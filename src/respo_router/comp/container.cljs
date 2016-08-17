
(ns respo-router.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div span]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-text]]
            [respo-router.style.widget :as widget]
            [respo-router.comp.router :refer [comp-router]]
            [respo-value.component.value :refer [render-value]]
            [respo-router.util.format :refer [router->string]]))

(defn render [store dict]
  (fn [state mutate!]
    (div
      {:style (merge widget/global)}
      (span {:attrs {:inner-text "Container"}})
      (comp-space "8px" nil)
      (div {:style widget/button} (comp-text "add" nil))
      (render-value (:router store))
      (comp-space "8px" nil)
      (comp-router (router->string (:router store) dict)))))

(def comp-container (create-comp :container render))
