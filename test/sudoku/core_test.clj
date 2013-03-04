(ns sudoku.core-test
  (:use clojure.test
        sudoku.core))

(deftest cellToVar-test
  (testing "cellVar returns the correct int"
    (is (= (cell-to-var 1 2 3) 233))))

(deftest varToCell-test
  (testing "var returns correct string of numbers"
    (is (= (var-to-cell 123) '(0 1 3))) 
    (is (= (var-to-cell 999) '(8 8 9)))))
