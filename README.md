
Respo Router
----

> This project is in early stage...

Demo http://repo.respo.site/router/

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/router.svg)](https://clojars.org/respo/router)

```clojure
[respo/router "0.3.0-a1"]
```

```clojure
[respo-router.util.listener :refer [listen! parse-address]]
[respo-router.core :refer [render-url!]]
```

```clojure
; router rules
(def dict
 {"room" ["room-id"], "team" ["team-id"], "search" []})

; :hash | :history
(def mode :history)

; listen to router and dispatch actions
(listen! dict dispatch! mode)

; /a/b?c=d
(parse-address address dict)

; render url
(add-watch *store :changes
  (fn [] (render-url! (:router @*store) dict mode)))
```

Special routes

* '/' which is identical to '/home'
* `404`

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
