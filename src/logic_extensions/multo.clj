(ns logic-extensions.multo
  "A first draft of multi-predicates for core.logic.
   Enables attaching additional clauses at runtime.

   In the current implementation goals follow the pattern of being extra defne clauses;
   Every time a new clause is added the var cointaining the predicate 
   is overwritten by a new defne invocation.

   Potential alternatives include dynamic predicates, reading goals from a seq
   at runtime. This option currently does not seem very compatible with the macro
   setup in core.logic.


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
  "
  (:refer-clojure :exclude [==])
  (:use clojure.core.logic))


; todo: treat docstrings appropriately
(defmacro defmulte [nom args]
  "Initialize an empty multi-predicate. (Clauses can be added at runtime using defclause.)

   (defmulte mymulte [a b])"
  (let [unsuc   #'u#
        goalset (ref '#{})]
    (list #'defne
      (with-meta nom {:multo {:goalset goalset :goaltype #'defne}})
      args
      ; attach trailing failing clause
      (list args unsuc))))


(defmacro defclause [nom  & body]
  "Add a new clause to a multi-predicate. Unfortunately uses eval.

  (defclause mymulte [a [:bar]] (== a :foo))"
  (let [unsuc #'u#]
    `(let [~'meta-data (meta (var ~nom))
           ~'args      (first (~'meta-data :arglists))
           ~'goalset   ((:multo ~'meta-data) :goalset)
           ~'kind      ((:multo ~'meta-data) :goaltype)]
      ; we do a dosync in order to get a side-effect whenever the goalset gets updated ;)
      (dosync
        (alter ~'goalset into (list '~body ))
        (eval 
          (concat 
            (list ~'kind (with-meta '~nom ~'meta-data) ~'args)
            (deref ~'goalset) 
            (list (list ~'args ~unsuc))))))))