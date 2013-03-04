(defproject sudoku "0.1.0-SNAPSHOT"
  :description "A clojure version of the sudoku solver"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main sudoku.core
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/math.numeric-tower "0.0.2"]]
  :java-source-paths ["src/java"])
