(ns uc-clojure.denotational
  "Understanding Comoputation in Clojure: Denotational Semantics.

  This namespace contains functions and data structures for implementing
  denotational semantics as per the Denotational Semantics section of Tom
  Stuart's ``Understanding Computation''."
  (:require [clojure.string :as string]
            [uc-clojure.constructs :refer :all]))


(defmulti to-clojure (fn [construct] (:type construct)))

(defn evaluate [c env]
  ((-> c
       to-clojure
       read-string
       eval)
   env))

(defmethod to-clojure :number
  [x]
  (format "(fn [env] %s)" (:value x)))

(defmethod to-clojure :boolean
  [x]
  (format "(fn [env] %s)" (:value x)))

(defmethod to-clojure :variable
  [x]
  (format "(fn [env] (get env %s))" (:name x)))

(defn binary-to-clojure
  [construct operation]
  (format "(fn [env] (%s (%s env) (%s env)))"
          operation
          (to-clojure (:left construct))
          (to-clojure (:right construct))))

(defmethod to-clojure :add
  [x] (binary-to-clojure x "+"))

(defmethod to-clojure :multiply
  [x] (binary-to-clojure x "*"))

(defmethod to-clojure :less-than
  [x] (binary-to-clojure x "<"))

(defmethod to-clojure :assign
  [x]
  (format "(fn [env] (merge env {%s (%s env)}))"
          (:name x)
          (to-clojure (:expression x))))

(defmethod to-clojure :do-nothing
  [x]
  "(fn [env] env)")

(defmethod to-clojure :if
  [x]
  (format "(fn [env] (if (%s env) (%s env) (%s env)))"
          (to-clojure (:condition x))
          (to-clojure (:consequence x))
          (to-clojure (:alternative x))))

(defmethod to-clojure :sequence
  [x]
  (format "(fn [env] (-> env (%s) (%s)))"
          (to-clojure (:first- x))
          (to-clojure (:second- x))))

(defmethod to-clojure :while
  [x]
  (format
   (str "(fn [env] (let [cond-evaled (%s env)]"
        " (if (= cond-evaled false) env (recur (%s env)))))")
   (to-clojure (:condition x))
   (to-clojure (:body x))))
