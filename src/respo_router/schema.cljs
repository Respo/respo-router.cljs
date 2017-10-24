
(ns respo-router.schema )

(def router {:path [], :query {}})

(def store {:router router, :states {}})

(def guidepost {:name nil, :data nil})

(def dict {"team" ["team-id"], "room" ["room-id"], "search" []})
