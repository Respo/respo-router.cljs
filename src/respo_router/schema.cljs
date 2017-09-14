
(ns respo-router.schema )

(def router {:name 404, :data {}, :router nil, :segments [], :query {}})

(def store {:router (assoc router :name "home"), :states {}})
