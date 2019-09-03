(ns uc-clojure.constructs)

(def expressions [:number :boolean :add :multiply :less-than :variable])

(def statements [:if :assign :do-nothing :sequence :while])

;; A "construct" is my own generalization over either an expression or a
;; statement.
;; There is probably a correct term for it.
;; An expression does not modify the environment.
;; A statement does modify the environment.
(defn get-construct-kind
  [construct-type]
  (cond
    (some #{construct-type} expressions) :expression
    (some #{construct-type} statements) :statement
    :else :unknown-construct-kind))

;; Constructors for primitive syntactic constructs (which are just Clojure maps)

(defn number- [value]
  {:type :number
   :reducible? false
   :value value})

(defn boolean- [value]
  {:type :boolean
   :reducible? false
   :value value})

(defn add- [left right]
  {:type :add
   :reducible? true
   :left left
   :right right})

(defn multiply- [left right]
  {:type :multiply
   :reducible? true
   :left left
   :right right})

(defn less-than- [left right]
  {:type :less-than
   :reducible? true
   :left left
   :right right})

(defn variable- [name]
  {:type :variable
   :reducible? true
   :name name})

(defn do-nothing- []
  {:type :do-nothing
   :reducible? false})

(defn assign- [name expression]
  {:type :assign
   :reducible? true
   :name name
   :expression expression})

(defn if- [condition consequence alternative]
  {:type :if
   :reducible? true
   :condition condition
   :consequence consequence
   :alternative alternative})

(defn sequence- [first- second-]
  {:type :sequence
   :reducible? true
   :first- first-
   :second- second-})

(defn while- [condition body]
  {:type :while
   :reducible? true
   :condition condition
   :body body})
