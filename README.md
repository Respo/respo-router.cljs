
Respo Router
----

> **Proof of concept** router component for Respo apps.
> Don't use it in real world app!

Demo http://repo.respo.site/router/

### Usage

Not ready for using, experiments only...

```clojure
[respo/router "0.1.0"]
```

```clojure
[respo-router.util.listener :refer [listen!]]
[respo-router.util.format :refer [router->string]]
[respo-router.comp.router :refer [comp-router]]
```

```clojure
; router rules
(def dict
 {"home" [], "room" ["room-id"], "team" ["team-id"], "search" []})

; listen to router and dispatch actions
(listen! dict dispatch!)

; mount component
(comp-router (router->string router-string))
```

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
