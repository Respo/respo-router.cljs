
Respo Router
----

Demo http://repo.respo.site/router/

### Usage

This project is in early stage...

```clojure
[respo/router "0.2.1"]
```

```clojure
[respo-router.util.listener :refer [listen! parse-address]]
[respo-router.core :refer [render-url!]]
```

```clojure
; router rules
(def dict
 {"home" [], "room" ["room-id"], "team" ["team-id"], "search" []})

; :hash | :history
(def mode :history)

; listen to router and dispatch actions
(listen! dict dispatch! mode)

; /a/b?c=d
(parse-address address dict)

; render url
(add-watch store-ref :changes
  (fn [] (render-url! (:router @store-ref) dict mode)))
```

Special routes

* '/' which is identical to '/home'
* `404`

### Develop

https://github.com/mvc-works/stack-workflow

### Test

Since cljs code cannot be loaded into JVM, I use Planck for testing:

Start Planck REPl:

```bash
planck -c src/:test/ -i test/respo/test.cljs -r
```

And run:

```clojure
(cljs.test/run-tests 'respo.test)
```

Reload namespace after code updates:

```clojure
(require 'respo.test :reload)
```

Find out more: http://planck-repl.org/testing.html

### License

MIT
