(ns uc-clojure.small-step-test
  (:require [clojure.test :refer :all]
            [uc-clojure.small-step :refer :all]))

(deftest machine-1-test
  (testing "machine 1"
    (is (= machine-1-result (get-ret machine-1)))))

(deftest machine-2-test
  (testing "machine 2"
    (is (= machine-2-result (get-ret machine-2)))))

(deftest machine-3-test
  (testing "machine 3"
    (is (= machine-3-result (get-ret machine-3)))))

(deftest machine-4-test
  (testing "machine 4"
    (is (= machine-4-result (get-ret machine-4)))))

(deftest machine-5-test
  (testing "machine 5"
    (is (= machine-5-result (get-ret machine-5)))))

(deftest machine-6-test
  (testing "machine 6"
    (is (= machine-6-result (get-ret machine-6)))))

(deftest machine-7-test
  (testing "machine 7"
    (is (= machine-7-result (get-ret machine-7)))))

(deftest machine-8-test
  (testing "machine 8"
    (is (= machine-8-result (get-ret machine-8)))))
