
(ns respo-router.util.format (:require [clojure.string :as string]))

(defn stringify-query [query]
  (string/join "&" (map (fn [pairs] (string/join "=" pairs)) query)))

(defn router->string
  ([router dict] (router->string "" (:path router) (:query router) dict))
  ([acc path query dict]
   (if (empty? path)
     (let [query-str (stringify-query query)
           query-part (if (string/blank? query-str) "" (str "?" query-str))]
       (str acc query-part))
     (let [guidepost (first path)
           params (get dict (:name guidepost))
           segments (map (fn [key-path] (get (:data guidepost) key-path)) params)
           segment-path (string/join "/" (cons (:name guidepost) segments))]
       (recur (str acc "/" segment-path) (rest path) query dict)))))
