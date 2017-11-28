
(ns respo-router.util.listener
  (:require [clojure.string :as string] [respo-router.schema :as schema]))

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

(defn slashTrimLeft [address]
  (if (string/blank? address) "" (if (= "/" (first address)) (subs address 1) address)))

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

(defn strip-sharp [text] (if (string/starts-with? text "#") (subs text 1) text))

(def *ignored? (atom false))

(defn listen! [dict dispatch! router-mode]
  (assert (map? dict) "first argument should be a dictionary")
  (assert (fn? dispatch!) "second argument shoud be dispatch function")
  (assert
   (contains? #{:history :hash} router-mode)
   (str "invalid router-demo: " router-mode))
  (case router-mode
    :hash
      (.addEventListener
       js/window
       "hashchange"
       (fn [event]
         (let [path-info (parse-address (strip-sharp (.-hash js/location)) dict)]
           (comment println "is ignored?" @*ignored?)
           (if (not @*ignored?)
             (js/setTimeout (fn [] (dispatch! :router/route path-info)) 0)))))
    :history
      (.addEventListener
       js/window
       "popstate"
       (fn [event]
         (let [current-address (str (.-pathname js/location) (.-search js/location))
               path-info (parse-address current-address dict)]
           (dispatch! :router/route path-info))))
    nil))
