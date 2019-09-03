(ns uc-clojure.big-step
  "Understanding Computation in Clojure: Big-Step Semantics.

  This namespace contains functions and data structures for implementing big-
  step semantics as per the Big-Step Semantics subsection under section 1.2
  of Tom Stuart's ``Understanding Computation''."
  (:require [clojure.string :as string]
            [uc-clojure.constructs :refer :all]))

;; Evaluate methods for the core components of our language

(defmulti evaluate (fn [construct env] (:type construct)))

(defn binary-evaluate
  "Evaluate a 2-arity construct by calling ret-constructor on the value returned
  by evaluating operation on the result of evaluating the left and right values
  of construct."
  [construct env ret-constructor operation]
  (ret-constructor
   (operation
    (-> construct :left (evaluate env) :value)
    (-> construct :right (evaluate env) :value))))

(defmethod evaluate :number [x env] x)
(defmethod evaluate :boolean [x env] x)

(defmethod evaluate :variable
  [x env] (-> x :name env))

(defmethod evaluate :add
  [x env]
  (binary-evaluate x env number- +))

(defmethod evaluate :multiply
  [x env]
  (binary-evaluate x env number- *))

(defmethod evaluate :less-than
  [x env]
  (binary-evaluate x env boolean- <))

(defmethod evaluate :assign
  [x env]
  (merge env {(:name x) (evaluate (:expression x) env)}))

(defmethod evaluate :do-nothing
  [x env] env)

(defmethod evaluate :if
  [x env]
  (let [cond-evaled (evaluate (:condition x) env)]
    (condp = cond-evaled
      (boolean- true) (evaluate (:consequence x) env)
      (boolean- false) (evaluate (:alternative x) env))))

(defmethod evaluate :sequence
  [x env]
  (->> env
       ((fn [e] (-> x :first- (evaluate e))))
       ((fn [e] (-> x :second- (evaluate e))))))

(defmethod evaluate :while
  [x env]
  (let [cond-evaled (evaluate (:condition x) env)]
    (condp = cond-evaled
      (boolean- false) env
      (boolean- true) (recur x (evaluate (:body x) env)))))
