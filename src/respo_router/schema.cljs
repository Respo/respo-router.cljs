
(ns respo-router.schema )

(def dict {"team" ["team-id"], "room" ["room-id"], "search" []})

(def guidepost {:name nil, :data nil})

(def router {:path [], :query {}})

(def store {:router router, :states {}})
