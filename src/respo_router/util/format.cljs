
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
        sub-router (:router router)
        segment-path (string/join "/" (cons (:name router) segments))]
    (println "formatting router:" segments router segment-path)
    (if (some? sub-router)
      (str "/" segment-path (router->string sub-router dict))
      (let [query-str (stringify-query (:query router))
            query-part (if (string/blank? query-str)
                         ""
                         (str "?" query-str))]
        (if (= segment-path "home")
          (str "/" query-part)
          (str "/" segment-path query-part))))))
