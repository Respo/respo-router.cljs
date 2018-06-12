
(ns respo-router.format (:require [clojure.string :as string]))

(defn stringify-query [query]
  (->> query (map (fn [pair] (->> pair (string/join "=")))) (string/join "&")))

(defn router->string
  ([router dict] (router->string "" (:path router) (:query router) dict))
  ([acc path query dict]
   (if (empty? path)
     (let [query-str (stringify-query query)
           query-part (if (string/blank? query-str) "" (str "?" query-str))]
       (str acc query-part))
     (let [guidepost (first path)
           params (get dict (:name guidepost))
           segments (->> params (map (fn [key-path] (get (:data guidepost) key-path))))
           segment-path (->> segments (cons (:name guidepost)) (string/join "/"))]
       (recur (str acc "/" segment-path) (rest path) query dict)))))

(defn slashTrimLeft [address]
  (if (string/blank? address) "" (if (= "/" (first address)) (subs address 1) address)))

(defn strip-sharp [text] (if (string/starts-with? text "#") (subs text 1) text))
