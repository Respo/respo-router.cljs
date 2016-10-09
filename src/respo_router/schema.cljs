
(ns respo-router.schema)

(def router {:router nil, :name 404, :segments [], :query {}, :data {}})

(def store {:router (assoc router :name "home")})
