(ns uc-clojure.small-step
  "Understanding Computation in Clojure: Small Step Semantics.

  This namespace contains functions and data structures for implementing small
  step semantics as per the Small Step Semantics subsection under section 1.2
  of Tom Stuart's ``Understanding Computation''."
  (:require [clojure.string :as string]
            [uc-clojure.constructs :refer :all]))

;; Reduce (reducit): transform a statement or an expression into an irreducible entity.

(defmulti reducit (fn [construct env] (:type construct)))

(defn binary-reduce
  "Reduce a 2-arity construct by first reducing its left argument, then its right,
   then returning the result of using ret-constructor (e.g., number-) to build a
   construct by performing operation on the values of left and right."
  [construct env bin-constructor ret-constructor operation]
  (cond
    (-> construct :left :reducible?)
    (bin-constructor (reducit (:left construct) env) (:right construct))
    (-> construct :right :reducible?)
    (bin-constructor (:left construct) (reducit (:right construct) env))
    :else
    (ret-constructor (operation
                      (-> construct :left :value)
                      (-> construct :right :value)))))

(defmethod reducit :add
  [x env]
  (binary-reduce x env add- number- +))

(defmethod reducit :multiply
  [x env]
  (binary-reduce x env multiply- number- *))

(defmethod reducit :less-than
  [x env]
  (binary-reduce x env less-than- boolean- <))

(defmethod reducit :assign
  [x env]
  (let [exp (:expression x)]
    (if (:reducible? exp)
      [(assign- (:name x) (reducit exp env)) env]
      [(do-nothing-) (assoc env (:name x) exp)])))

(defmethod reducit :variable
  [x env]
  ((:name x) env))

(defmethod reducit :if
  [{:keys [condition consequence alternative]} env]
  (if (:reducible? condition)
    [(if- (reducit condition env) consequence alternative) env]
    (if (= (boolean- true) condition)
      [consequence env]
      [alternative env])))

(defmethod reducit :sequence
  [{:keys [first- second-]} env]
  (if (= first- (do-nothing-))
    [second- env]
    (let [[reduced-first reduced-environment]
          (reducit first- env)]
      [(sequence- reduced-first second-) reduced-environment])))

(defmethod reducit :while
  [{:keys [condition body] :as while-stmt} env]
  [(if- condition (sequence- body while-stmt) (do-nothing-)) env])

;; Step: perform a reduction step on a machine by reducing its construct and updating
;; its environment

(defmulti step (fn [machine] (get-construct-kind (-> machine :construct :type))))

(defmethod step :statement
  [machine]
  (let [[statement environment]
        (reducit (:construct machine) (:environment machine))]
    (-> machine
        (assoc :construct statement)
        (assoc :environment environment))))

(defmethod step :expression
  [machine]
  (assoc machine :construct
         (reducit (:construct machine) (:environment machine))))

;; To String: return a string representation of a construct

(defmulti to-str (fn [construct] (:type construct)))

(defmethod to-str :number
  [construct]
  (str (:value construct)))

(defmethod to-str :variable
  [construct]
  (str (-> construct :name name)))

(defmethod to-str :boolean
  [construct]
  (str (:value construct)))

(defmethod to-str :add
  [construct]
  (format "%s + %s" (to-str (:left construct)) (to-str (:right construct))))

(defmethod to-str :multiply
  [construct]
  (format "%s * %s" (to-str (:left construct)) (to-str (:right construct))))

(defmethod to-str :less-than
  [construct]
  (format "%s < %s" (to-str (:left construct)) (to-str (:right construct))))

(defmethod to-str :assign
  [construct]
  (format "%s = %s" (-> construct :name name) (to-str (:expression construct))))

(defmethod to-str :do-nothing
  [construct]
  "do-nothing")

(defmethod to-str nil
  [construct]
  (str construct))

(defmethod to-str :sequence
  [{:keys [first- second-]}]
  (apply (partial format "%s; %s")
         (map to-str [first- second-])))

(defmethod to-str :while
  [{:keys [condition body]}]
  (apply (partial format "while ( %s ) { %s }")
         (map to-str [condition body])))

(defmethod to-str :if
  [{:keys [condition consequence alternative]}]
  (apply (partial format "if ( %s ) { %s } else { %s }")
         (map to-str [condition consequence alternative])))

(defn env-to-str
  [env]
  (into {} (map (fn [[k v]] [k (to-str v)]) env)))

;; Run a machine: step through the reduction of the machine's construct, accumulating
;; a list of strings representing the construct at all of its stages of reduction.

(defn run
  ([m] (run m []))
  ([m ret]
   (let [construct (:construct m)]
     (if (:reducible? construct)
       (run (step m)
         (conj ret [(to-str construct) (-> m :environment env-to-str)]))
       (conj ret [(to-str construct) (-> m :environment env-to-str)])))))

(defn get-ret
  [m] (-> m run last))

(defn get-ret-all
  [machines] (list (for [m machines] (get-ret m))))

(defn run-print
  [m]
  (println (string/join "\n"
                        (map (fn [p] (string/join ", " p)) (run m)))))

(defn run-print-all [machines]
  (doseq [m machines]
    (run-print m)
    (println "\n")))

(defn run-all [machines]
  (for [m machines] (run m)))

;; Some sample machines and constructs (i.e., expressions and statements)

;; (1 * 2) + (3 * 4)
(def machine-1
  {:construct
   (add-
    (multiply-
     (number- 1)
     (number- 2))
    (multiply-
     (number- 3)
     (number- 4)))
   :environment {}})

(def machine-1-result ["14" {}])

;; (< 5 (+ 2 2))
(def machine-2
  {:construct
   (less-than-
    (number- 5)
    (add-
     (number- 2)
     (number- 2)))
   :environment {}})

(def machine-2-result ["false" {}])

;; (< x (+ y y)), where x = 5 and y = 2
(def machine-3
  {:construct
   (less-than-
    (variable- :x)
    (add-
     (variable- :y)
     (variable- :y)))
   :environment {:x (number- 5)
                 :y (number- 2)}})

(def machine-3-result ["false" {:x "5", :y "2"}])

;; x = (+ x 1), where x = 2
(def machine-4
  {:construct
   (assign-
    :x
    (add-
     (variable- :x)
     (number- 1)))
   :environment {:x (number- 2)}})

(def machine-4-result ["do-nothing" {:x "3"}])

;; if (x) { y = 1 } else { y = 2}, where x is true
(def machine-5
  {:construct
   (if-
    (variable- :x)
    (assign- :y (number- 1))
    (assign- :y (number- 2)))
   :environment {:x (boolean- true)}})

(def machine-5-result ["do-nothing" {:x "true", :y "1"}])

;; if (x) { y = 1 }, where x is false
(def machine-6
  {:construct
   (if-
    (variable- :x)
    (assign- :y (number- 1))
    (do-nothing-))
   :environment {:x (boolean- false)}})

(def machine-6-result ["do-nothing" {:x "false"}])

;; x = (+ 1 1); y = (+ x 3)
(def machine-7
  {:construct
   (sequence-
    (assign- :x (add- (number- 1) (number- 1)))
    (assign- :y (add- (variable- :x) (number- 3))))
   :environment {}})

(def machine-7-result ["do-nothing" {:x "2", :y "5"}])

;; while (< x 5) { x = (* x 3)}, where x = 1
(def machine-8
  {:construct
   (while-
    (less-than- (variable- :x) (number- 5))
    (assign- :x (multiply- (variable- :x) (number- 3))))
   :environment {:x (number- 1)}})

(def machine-8-result ["do-nothing" {:x "9"}])

(def machine-bad
  {:construct
   (sequence-
    (assign- :x (boolean- true))
    (assign- :x (add- (variable- :x) (number- 1))))
   :environment {}})

(def machines
  [machine-1
   machine-2
   machine-3
   machine-4
   machine-5
   machine-6
   machine-7
   machine-8])

;; Tests

(def machine-results
  [machine-1-result
   machine-2-result
   machine-3-result
   machine-4-result
   machine-5-result
   machine-6-result
   machine-7-result
   machine-8-result])
