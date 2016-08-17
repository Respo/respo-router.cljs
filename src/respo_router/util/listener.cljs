
(ns respo-router.util.listener
  (:require [clojure.string :as string]
            [respo-router.schema :as schema]))

(defn parse-path [paths dict query]
  (println paths dict)
  (if (empty? paths)
    nil
    (let [path-name (first paths)]
      (if (contains? dict path-name)
        (let [params (get dict path-name) len (count params)]
          (if (< (dec (count paths)) len)
            (merge schema/router {:segments paths, :query query})
            (merge
              schema/router
              {:sub
               (let [sub-paths (subvec paths (inc (count params)))]
                 (if (empty? sub-paths)
                   nil
                   (parse-path sub-paths dict query))),
               :name path-name,
               :query query,
               :data (zipmap params paths)})))
        (merge schema/router {:segments paths, :query query})))))

(defn strip-sharp [text]
  (if (string/starts-with? text "#") (subs text 1) text))

(defn parse-query [text]
  (->>
    (string/split text "&")
    (map (fn [piece] (string/split piece "=")))
    (into {})))

(defn handle-change [dispatch!]
  (let [hash (strip-sharp (.-hash js/location))
        text-path (if (string/includes? hash "?")
                    (first (string/split hash "?"))
                    hash)
        query (if (string/includes? hash "?")
                (parse-query (last (string/split hash "?")))
                {})]
    [(string/split text-path "/") query]))

(defn listen! [dict dispatch!]
  (.addEventListener
    js/window
    "hashchange"
    (fn [event]
      (let [[paths query] (handle-change dispatch!)
            path-info (parse-path paths dict query)]
        (dispatch! :router/route path-info)))))
