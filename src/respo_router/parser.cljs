
(ns respo-router.parser
  (:require [clojure.string :as string] [respo-router.format :refer [slashTrimLeft]]))

(defn parse-query [text]
  (if (string/blank? text)
    {}
    (->> (string/split text "&")
         (map (fn [piece] (string/split piece (re-pattern "="))))
         (into {}))))

(defn extract-address [address]
  (let [text-path (if (string/includes? address "?")
                    (first (string/split address "?"))
                    address)
        query (if (string/includes? address "?")
                (let [segments (string/split address "?")]
                  (if (= (count segments) 1) {} (parse-query (last segments))))
                {})
        segments (filterv
                  (fn [piece] (not (string/blank? piece)))
                  (string/split text-path "/"))]
    [segments query]))

(defn parse-path [acc paths dict]
  (if (empty? paths)
    acc
    (let [path-name (first paths)]
      (if (contains? dict path-name)
        (let [params (get dict path-name), len (count params)]
          (if (< (dec (count paths)) len)
            (conj acc {:name 404, :data paths})
            (recur
             (conj acc {:name path-name, :data (zipmap params (rest paths))})
             (subvec paths (inc (count params)))
             dict)))
        (conj acc {:name 404, :data paths})))))

(defn parse-address [address dict]
  (assert (string? address) "first argument should be a string")
  (assert (map? dict) "second argument should be dictionary")
  (let [trimed-address (slashTrimLeft address)
        [segments query] (extract-address trimed-address)]
    {:path (parse-path [] segments dict), :query query}))
