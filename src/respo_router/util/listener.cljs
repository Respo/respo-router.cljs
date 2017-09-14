
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
        paths (filterv
               (fn [piece] (not (string/blank? piece)))
               (string/split text-path "/"))]
    [paths query]))

(defn slashTrimLeft [address]
  (if (string/blank? address) "" (if (= "/" (first address)) (subs address 1) address)))

(defn parse-path [paths dict query]
  (comment println "parse-path:" (pr-str paths) dict query (empty? paths) (count paths))
  (if (empty? paths)
    (merge schema/router {:name "home", :query query})
    (let [path-name (first paths)]
      (if (contains? dict path-name)
        (let [params (get dict path-name), len (count params)]
          (if (< (dec (count paths)) len)
            (merge schema/router {:segments (rest paths), :query query})
            (merge
             schema/router
             {:name path-name,
              :data (zipmap params (rest paths)),
              :router (let [sub-paths (subvec paths (inc (count params)))]
                (if (empty? sub-paths) nil (parse-path sub-paths dict query))),
              :query query})))
        (merge schema/router {:segments (rest paths), :query query})))))

(defn parse-address [address dict]
  (let [trimed-address (slashTrimLeft address)
        [paths query] (extract-address trimed-address)]
    (parse-path paths dict query)))

(defn strip-sharp [text] (if (string/starts-with? text "#") (subs text 1) text))

(def ignored?-ref (atom false))

(defn listen! [dict dispatch! router-mode]
  (case router-mode
    :hash
      (.addEventListener
       js/window
       "hashchange"
       (fn [event]
         (let [path-info (parse-address (strip-sharp (.-hash js/location)) dict)]
           (comment println "is ignored?" @ignored?-ref)
           (if (not @ignored?-ref)
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
