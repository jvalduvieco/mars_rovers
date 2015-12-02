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

; (doall) is necessary to force evaluation of lazy sequences as thrown? does not iterate over result
(deftest no-data
  (testing "No data, Exception"
    (is (thrown? clojure.lang.ExceptionInfo (doall (new-mission
                                                     '[]))))))

(deftest rover-starts-outside-bounds
  (testing "A rover starting outside bounds does not move"
    (is (= (new-mission
             '[[2 2]
               [6 6 \N]
               ["LMLMLMLMM"]])
           '([6 6 \N])))))

(deftest north-bound-is-enforced
  (testing "a rover can go beyond N bound"
    (is (= (new-mission
             '[[2 2]
               [1 1 \N]
               ["MMMMMMMMMMMM"]])
           '([1 2 \N])))))

(deftest east-bound-is-enforced
  (testing "a rover can go beyond E bound"
    (is (= (new-mission
             '[[2 2]
               [1 1 \E]
               ["MMMMMMMMMMMM"]])
           '([2 1 \E])))))

(deftest west-bound-is-enforced
  (testing "a rover can go beyond W bound"
    (is (= (new-mission
             '[[2 2]
               [1 1 \W]
               ["MMMMMMMMMMMM"]])
           '([0 1 \W])))))

(deftest south-bound-is-enforced
  (testing "a rover can go beyond s bound"
    (is (= (new-mission
             '[[2 2]
               [1 1 \S]
               ["MMMMMMMMMMMM"]])
           '([1 0 \S])))))

(deftest rotate-left
  (testing "a rover rotates left"
    (is (= (new-mission
             '[[2 2]
               [1 1 \N]
               ["L"]])
           '([1 1 \W])))))

(deftest rotate-right
  (testing "a rover rotates right"
    (is (= (new-mission
             '[[2 2]
               [1 1 \N]
               ["R"]])
           '([1 1 \E])))))

(deftest invalid-command
  (testing "Invalid command raises exception"
    (is (thrown? IllegalArgumentException (doall (new-mission
                                                      '[[2 2]
                                                        [1 1 \N]
                                                        ["Z"]]))))))
