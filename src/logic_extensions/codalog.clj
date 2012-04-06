(ns logic-extensions.codalog
  "core.logic interface with datalog and datomic (and cascalog?)"
  (:use [datomic.api :only [db q] :as d]))

(def uri "datomic:mem://test")
(def conn (d/connect uri))
(def results (q '[:find ?e :where [?e :db/doc]] (db conn)))


; If you do then you should probably just write you own goals
; that can source data from your graph. This can easily be
; done by returning a Choice from your custom goal.

; (defn custom-goal [x y z]
;    ...
;    (choice a (fn [] ...))

; choice is like a lazy sequence, you have your first value,
; and then a thunk to produce the remainder of the sequence.
; You can do whatever you want in the body of the custom-goal
; to make sure the optimal resultset is operated on.

; For example, you might have indexed your data on the possible 
; values x, or y, or z. You can check to see if in this call to 
; your custom-goal whether x, y, or z are "ground", that is, they 
; are either not logic variables, or they are logic variables that 
; are bound to values. If they are you can use those values to looked 
; at the indexed version of your data.

; By examining to-stream and extend-rel you should be able to see 
; how to turn any Clojure sequence into a stream of facts that you can
; plug into a core.logic program.
