(ns uc-clojure.big-step-test
  (:require [uc-clojure.big-step :as sut]
            [uc-clojure.constructs :as c]
            [clojure.test :as t]))

(t/deftest big-step-test-number
  (t/testing "Evaluating number expressions"
    (let [r (sut/evaluate (c/number- 23) {})]
      (t/is (= (:type r) :number))
      (t/is (= (:value r) 23)))))

(t/deftest big-step-test-variable
  (t/testing "Evaluating variables"
    (let [r (sut/evaluate (c/variable- :x) {:x (c/number- 23)})]
      (t/is (= (:type r) :number))
      (t/is (= (:value r) 23)))))

(t/deftest big-step-test-less-than-add
  (t/testing "Evaluating less-than and addition: x + 2 < y, where x = 2 and y = 3"
    (let [r (sut/evaluate
             (c/less-than-
              (c/add-
               (c/variable- :x)
               (c/number- 2))
              (c/variable- :y))
             {:x (c/number- 2)
              :y (c/number- 5)})]
      (t/is (= (:type r) :boolean))
      (t/is (= (:value r) true)))))

(t/deftest big-step-test-variable-assignment-from-other-variable
  (t/testing "We can assign a value to a variable using the value of an existing variable"
    (let [r (sut/evaluate
             (c/assign-
              :x
              (c/add-
               (c/variable- :y)
               (c/number- 3)))
             {:y (c/number- 2)})
          x (:x r)]
      (t/is (= (:type x) :number))
      (t/is (= (:value x) 5)))))

(t/deftest big-step-test-do-nothing
  (t/testing "Do-nothing just returns the environment unmodified"
    (let [r (sut/evaluate (c/do-nothing-) {})]
      (t/is (= r {})))))

(t/deftest big-step-test-if-statements
  (t/testing "If statements work"
    (let [r (sut/evaluate
             (c/if-
              (c/less-than-
               (c/number- 33)
               (c/number- 4))
              (c/assign-
               :x
               (c/number- 7))
              (c/assign-
               :x
               (c/number- 777)))
             {})
          x (:x r)]
      (t/is (= (:type x) :number))
      (t/is (= (:value x) 777)))))

(t/deftest big-step-test-sequencing
  (t/testing "Statement sequencing works"
    (let [r (sut/evaluate
             (c/sequence-
              (c/assign- :x (c/number- 2))
              (c/assign- :y (c/number- 3)))
             {})
          {:keys [x y]} r]
      (t/is (= (:type x) :number))
      (t/is (= (:value x) 2))
      (t/is (= (:type y) :number))
      (t/is (= (:value y) 3)))))

(t/deftest big-step-test-interdependent-sequencing
  (t/testing "Interdependent statement sequencing works"
    (let [r (sut/evaluate
             (c/sequence-
              (c/assign-
               :x
               (c/add-
                (c/number- 1)
                (c/number- 1)))
              (c/assign-
               :y
               (c/add-
                (c/variable- :x)
                (c/number- 3))))
             {})
          y (:y r)]
      (t/is (= (:type y) :number))
      (t/is (= (:value y) 5)))))
