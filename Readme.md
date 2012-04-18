# logic-extensions

A collection of extensions for the Clojure core.logic relational/logic programming library.

Currently only contains [logic-extensions.multo](https://github.com/werg/logic-extensions/blob/master/src/logic_extensions/multo.clj), but I have a few more ideas ;).

## Install

Add the following entry to your `project.clj` `:dependencies` vector to include logic-extensions in your project, from [clojars](https://clojars.org/werg/logic-extensions):

```clojure
    [werg/logic-extensions "0.0.1-SNAPSHOT"]
```

Install datomic according to these [instructions](http://datomic.com/company/resources/integrating-peer-lib).

## multo

logic-extensions.multo is an extension of the core.logic `defne` mechanism
to account for predicates/relations where the goalset can be dynamically extended at runtime. I.e. a relational equivalent of multimethods, hence the naming.

### Usage

In the repl:

```clojure
   (use 'clojure.core.logic)
   (use 'logic-extensions.multo)

   (defmulte mymulte [a b])

   (defclause mymulte [a [:bar]] (== a :foo))
   (run* [q] (fresh [r s] (mymulte r s) (== q [r s])))
   ;([:foo [:bar]])

   (defclause mymulte [:baz b] (== b 42))
   (run* [q] (fresh [r s] (mymulte r s) (== q [r s])))
   ;([:baz 42] [:foo [:bar]])
   ; order may differ (it's a set by design)
```

## License

Copyright (C) 2012 Gabriel Pickard

Distributed under the Eclipse Public License, the same as Clojure.
