
(ns respo-router.comp.container
  (:require-macros [respo.macros :refer [defcomp div span cursor-> pre a <>]])
  (:require [hsl.core :refer [hsl]]
            [respo.core :refer [create-comp create-element]]
            [respo.comp.space :refer [=<]]
            [respo-ui.style :as ui]
            [fipp.edn :refer [pprint]]
            [respo-router.util.listener :refer [strip-sharp]]))

(defn route-home [e dispatch!] (dispatch! :router/route {:path [], :query {}}))

(defn route-room [e dispatch!]
  (dispatch!
   :router/route
   {:path [{:name "team", :data {"team-id" "t12345"}}
           {:name "room", :data {"room-id" "r1234"}}],
    :query {"a" 1, "b" 2}}))

(defn route-team [e dispatch!]
  (dispatch! :router/route {:path [{:name "team", :data {"team=id" "t1234"}}], :query {}}))

(defn route-search [e dispatch!] (dispatch! :router/nav "/search"))

(defn route-404 [e dispatch!] (dispatch! :router/nav "/missing"))

(defn render-link [guide on-click]
  (a {:style {:margin-right 8}, :href "javascript:;", :on {:click on-click}} (<> guide)))

(defcomp
 comp-container
 (store)
 (let [states (:states store)]
   (div
    {:style (merge ui/global {:padding 16})}
    (div
     {:style ui/row}
     (<> "Path:")
     (=< 16 nil)
     (<> (strip-sharp js/window.location.hash)))
    (div
     {:style ui/row}
     (<> "Entries:")
     (=< 16 nil)
     (div
      {}
      (render-link "home" route-home)
      (render-link "team" route-team)
      (render-link "room" route-room)
      (render-link "search" route-search)
      (render-link "404" route-404)))
    (div
     {:style ui/row}
     (<> "Data:")
     (=< 16 nil)
     (pre
      {:inner-text (with-out-str (pprint (:router store))),
       :style {:line-height "20px", :margin 8}})))))
