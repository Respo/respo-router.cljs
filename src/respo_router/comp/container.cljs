
(ns respo-router.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo.macros :refer [defcomp div span cursor-> pre a <>]]
            [respo.comp.space :refer [=<]]
            [respo-ui.style :as ui]
            [fipp.edn :refer [pprint]]
            [respo-router.format :refer [router->string strip-sharp]]
            [respo-router.schema :refer [dict]]))

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

(def style-codeblock {:line-height "20px", :margin 8})

(defcomp
 comp-container
 (store)
 (let [states (:states store)]
   (div
    {:style (merge ui/global {:padding 16})}
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
     (<> "Dict:")
     (=< 16 nil)
     (<> pre (with-out-str (pprint dict)) style-codeblock))
    (div
     {:style ui/row}
     (<> "Path:")
     (=< 16 nil)
     (<> pre (router->string (:router store) dict) style-codeblock))
    (div
     {:style ui/row}
     (<> "Data:")
     (=< 16 nil)
     (<> pre (with-out-str (pprint (:router store))) style-codeblock))
    (div
     {:style ui/row}
     (<> "GitHub:")
     (=< 10 nil)
     (a
      {:href "https://github.com/Respo/respo-router",
       :inner-text "Respo/router",
       :target "_blank"})))))
