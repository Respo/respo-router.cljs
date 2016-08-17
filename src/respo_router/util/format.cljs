
(ns respo-router.util.format
  (:require [clojure.string :as string]))

(defn stringify-query [query]
  (string/join "&" (map (fn [pairs] (string/join "=" pairs)) query)))

(defn router->string [router dict]
  (let [params (get dict (:name router))
        router-data (:data router)
        segments (map
                   (fn [key-path] (get router-data key-path))
                   params)
        sub-router (:sub router)
        segment-path (string/join "/" (cons (:name router) segments))]
    (if (some? sub-router)
      (str segment-path "/" (router->string sub-router dict))
      (str segment-path "?" (stringify-query (:query router))))))
