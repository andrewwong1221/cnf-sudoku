(ns sudoku.core-test
  (:use clojure.test
        sudoku.core))

(deftest cellToVar-test
  (testing "cellVar returns the correct int"
    (is (= (cellToVar 1 2 3) 123))))

(deftest varToCell-test
  (testing "var returns correct string of numbers"
    (is (= (varToCell 123) '(1 2 3))) 
    (is (= (varToCell 999) '(9 9 9)))))
