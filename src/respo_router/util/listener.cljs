
(ns respo-router.util.listener
  (:require [clojure.string :as string]
            [respo-router.schema :as schema]))

(def ignored?-ref (atom false))

(defn parse-query [text]
  (if (string/blank? text)
    {}
    (->>
      (string/split text "&")
      (map (fn [piece] (string/split piece (re-pattern "="))))
      (into {}))))

(defn extract-address [address]
  (let [text-path (if (string/includes? address "?")
                    (first (string/split address "?"))
                    address)
        query (if (string/includes? address "?")
                (let [segments (string/split address "?")]
                  (if (= (count segments) 1)
                    {}
                    (parse-query (last segments))))
                {})]
    [(string/split text-path "/") query]))

(defn parse-path [paths dict query]
  (println paths dict query)
  (if (empty? paths)
    (merge schema/router {:name "home", :query query})
    (let [path-name (first paths)]
      (if (contains? dict path-name)
        (let [params (get dict path-name) len (count params)]
          (if (< (dec (count paths)) len)
            (merge
              schema/router
              {:segments (rest paths), :query query})
            (merge
              schema/router
              {:router
               (let [sub-paths (subvec paths (inc (count params)))]
                 (if (empty? sub-paths)
                   nil
                   (parse-path sub-paths dict query))),
               :name path-name,
               :query query,
               :data (zipmap params (rest paths))})))
        (merge schema/router {:segments (rest paths), :query query})))))

(defn strip-sharp [text]
  (if (string/starts-with? text "#") (subs text 1) text))

(defn parse-address [address dict]
  (let [[paths query] (extract-address address)]
    (parse-path paths dict query)))

(defn listen! [dict dispatch! router-mode]
  (case
    router-mode
    :hash
    (.addEventListener
      js/window
      "hashchange"
      (fn [event]
        (let [path-info (parse-address
                          (strip-sharp (.-hash js/location))
                          dict)]
          (println "is ignored?" @ignored?-ref)
          (if (not @ignored?-ref)
            (js/setTimeout
              (fn [] (dispatch! :router/route path-info))
              0)))))
    :history
    (println "history mode not finished yet")
    nil))
