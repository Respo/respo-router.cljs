
(ns respo-router.test
  (:require [cljs.test :refer [deftest is run-tests testing]]
            [respo-router.format :as format]
            [respo-router.parser :refer [parse-address]]))

(deftest
 test-parse-address
 (testing "parse empty path" (is (= (parse-address "/" {}) {:path [], :query {}})))
 (testing
  "parse nested paths"
  (is
   (=
    (parse-address "/a/b/a/a" {"a" [], "b" []})
    {:path [{:name "a", :data {}}
            {:name "b", :data {}}
            {:name "a", :data {}}
            {:name "a", :data {}}],
     :query {}})))
 (testing
  "parse paths with parameters"
  (is
   (=
    (parse-address "/a/b/c" {"a" ["b" "c"]})
    {:path [{:name "a", :data {"b" "b", "c" "c"}}], :query {}}))))

(deftest
 test-stringify-query
 (testing "generate empty query" (is (= "" (format/stringify-query {}))))
 (testing
  "generate simple query"
  (is (= "a=1&b=2" (format/stringify-query {"a" 1, "b" 2})))))

(defn main! [] (run-tests))
