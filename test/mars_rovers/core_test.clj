(ns mars-rovers.core-test
  (:require [clojure.test :refer :all]
            [mars-rovers.core :refer :all]))

(deftest supplied-example
  (testing "Supplied example is met"
  (is (= (new-mission
          '[[5 5]
            [1 2 \N]
            ["LMLMLMLMM"]
            [3 3 \E]
            ["MMRMMRMRRM"]])
         '([1 3 \N] [5 1 \E])))))

(deftest no-rovers
  (testing "No rovers, no results"
    (is (= (new-mission
             '[[5 5]])
           '()))))

(deftest no-data
  (testing "No data, no results, no errors"
    (is (= (new-mission
             '[])
           '()))))

(deftest rover-starts-outside-bounds
  (testing "A rover starting outside bounds does not move"
    (is (= (new-mission
             '[[2 2]
               [6 6 \N]
               ["LMLMLMLMM"]])
             '([6 6 \N])))))

(deftest bounds-are-enforced
  (testing "a rover can not leave bounds"
    (is (= (new-mission
             '[[2 2]
               [1 1 \N]
               ["MMMMMMMMMMMM"]])
           '([1 2 \N])))))