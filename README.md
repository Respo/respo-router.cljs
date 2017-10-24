
Respo Router
----

> This project is in early stage...

Demo http://repo.respo.site/router/

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/router.svg)](https://clojars.org/respo/router)

```clojure
[respo/router "0.3.0-a2"]
```

```clojure
[respo-router.util.listener :refer [listen! parse-address strip-sharp]]
[respo-router.core :refer [render-url!]]
```

```clojure
; router rules
(def dict
 {"room" ["room-id"]
  "team" ["team-id"]
  "search" []})

; :hash | :history
(def mode :history)

; listen to router and dispatch actions
(listen! dict dispatch! mode)

; /a/b?c=d
(parse-address path dict)

; render url
(add-watch *store :changes
  (fn [] (render-url! (:router @*store) dict mode)))
```

### Router IR

Based on a dict:

```clojure
(def dict {"team" ["team-id"]
           "room" ["room-id"]
           "search" []})
```

Router data structure for:

```url
/team/t12345/room/r1234?a=1&b=2
```

looks like:

```edn
{:path [{:name "team", :data {"team-id" "t12345"}}
        {:name "room", :data {"room-id" "r1234"}}],
 :query {"a" 1, "b" 2}}
```

Some special routes:

* `[]` represents `/`
* `404` is generated when no route is matched

### Develop

```bash
yarn watch
# another terminal
yarn dev
# open http://localhost:8080
```

https://github.com/mvc-works/coworkflow

### Test

```bash
yarn compile-test
node target/test.js
```

### License

MIT
