(ns sudoku.core
  (:use [clojure.math.numeric-tower]
        [clojure.java.shell]))

(defn cell-to-var [i j k]
  "Converts a cell to an integer"
  (+ (* (inc i) 100 ) (* (inc j) 10) (inc k)))

(defn var-to-cell [var]
  "Converts an integer to cell representaiton"
  (conj '() (rem var 10) (dec (rem (quot var 10) 10)) (dec (quot var 100))))
(var-to-cell 111)

(defn get-digit [puzzle i j]
  (. puzzle (get i j)))

(defn get-size [puzzle]
  (. puzzle (size)))

(defn get-subsize [puzzle]
  (. puzzle (subsize)))

(defn neg [i]
  (* -1 i))

(defn read-puzzle [filename]
  (-> (SudokuReader. filename) (.readSudoku)))

(defn puzzle-to-vec [puzzle]
  (let [end (get-size puzzle)]
    (vec (for [i (range end)]
      (vec (for [j (range end)]
        (get-digit puzzle i j))))) ))

(defn cell-has-value [i j size]
  "There is at least one number true for each cell"
  (vector (vec (for [k (range size)]
    (cell-to-var i j k)))))


(defn cell-is-unique [i j size]
  (vec (remove nil? (for [m (range size)
             n (range size)]  
             (when (not= m n) (vector (neg (cell-to-var i j m))  (neg (cell-to-var i j n))))))))

(defn all-cell-constraints [size]
  (vec (apply concat 
              (for [i (range size) 
                    j (range size)] 
                (into (cell-has-value i j size) (cell-is-unique i j size))))))

(defn row-constraints [size]
  (vec (apply concat (for [k (range size)]
    (vec (for [i (range size)] 
        (vec (for [j (range size)]
            (cell-to-var i j k)))))))))

(defn col-constraints [size]
  (vec (apply concat (for [k (range size)]
    (vec (for [j (range size)] 
        (vec (for [i (range size)]
            (cell-to-var i j k)))))))))


(defn inner-box-constraint [x y size subsize]
  "Returns a constraint for a 'box'"
  (vec (for [k (range size)]
    (vec (for [i (range (* x subsize) (+ subsize (* x subsize)))
          j (range (* y subsize) (+ subsize (* y subsize)))]
      (cell-to-var i j k))))))

(defn box-constraints [size subsize]
  (vec (apply concat (for [i (range (/ size subsize))
                      j (range (/ size subsize))]
                  (inner-box-constraint i j size subsize)))))
(defn puzzle-constraints [puzzle]
  "Load puzzle and get filled numbers"
  (vec (map vector (remove nil?  
     (for [i (range (get-size puzzle)) 
           j (range (get-size puzzle))] 
      (if (not= 0 (get-digit puzzle i j))
        (cell-to-var i j (dec (get-digit puzzle i j)))))))))


(defn all-constraints [puzzle]
  (let [size (get-size puzzle)
        subsize (get-subsize puzzle)]
    (into 
      (into 
        (into 
          (into
            (puzzle-constraints puzzle) 
            (all-cell-constraints size))
            (col-constraints size)) 
          (row-constraints size))
      (box-constraints size subsize))))

(defn dimacs-line [vec]
  (str (clojure.string/join " " vec) " 0"))

(defn dimacs-format [constraints size]
  "Convert to dimacs format"
  (let [header  (str "c comment\np cnf " (cell-to-var (dec size) (dec size) (dec size)) " " (count constraints) "\n")
        strs (map dimacs-line constraints)
        joined (clojure.string/join "\n" strs)] 
    (str header joined)))



(defn write-to-file [filename content]
  (with-open [wtr (clojure.java.io/writer filename)]
    (.write wtr content)))

(defn read-from-file [file]
  (map read-string  
       (re-seq #"[-\d.]+" (second (clojure.string/split (slurp file) #"\n")))))

(defn clean-input [coll size]
  (remove 
    #(or (= 0 (rem % 10)) (< % 111) (> (abs %) (cell-to-var (dec size) (dec size) (dec size))))
  coll))

(defn create-sudoku [coll size]
  (let [p (Sudoku. size)
        resolved-cells (map var-to-cell coll)]
    (doall (map 
             (fn [coords] (. p (set (nth coords 0) (nth coords 1) (nth coords 2))))
             resolved-cells)) 
      p))

(defn -main 
  ([file] 
    (let [puzzle (read-puzzle file)
          size (get-size puzzle)
          minisat "./minisat"
          cnf-file "puzzle.cnf"
          out-file "puzzle.out"]
      (.print puzzle)
      (write-to-file cnf-file (dimacs-format (all-constraints puzzle) size)) 
      (sh minisat cnf-file out-file)
      (shutdown-agents)
      (.print (create-sudoku (clean-input (read-from-file out-file) size) size)))) 
  ([] 
    (-main "puzzles/wikipedia-example.sdk")))

;(-main)
;(clojure.java.shell/sh "./minisat-static")
;(create-sudoku (clean-input (read-from-file "test.out") 9) 9)
;\(create-sudoku (clean-input (read-from-file "test.out") 1) 1)
; (write-to-file "test.cnf" (dimacs-format (all-constraints puzzle) (get-size puzzle)))
; (puzzle-constraints puzzle)
; (def puzzle (read-puzzle "puzzles/example-1.sdk"))
; (def puzzle (read-puzzle "puzzles/example-4.2.sdk"))
; (def puzzle (read-puzzle "puzzles/example-9.1.sdk"))
; (def puzzle (read-puzzle "puzzles/example-9.2.sdk"))
; (def puzzle (read-puzzle "puzzles/example-9.empty.sdk"))
; (puzzle-to-vec puzzle)
; (println puzzle)

