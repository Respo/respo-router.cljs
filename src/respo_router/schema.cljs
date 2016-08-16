
(ns respo-router.schema)

(def router {:sub nil, :name 404, :segments [], :data {}})

(def store {:router (assoc router :name "home")})
