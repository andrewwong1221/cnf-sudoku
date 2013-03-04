# cnf-sudoku

A Clojure application used to solve sudoku puzzles using boolean satisfiability
by creating a number of rules in conjunctive normal form then calling MiniSat.

## Usage

Use lein and call:

    lein run
	lein run [filename]
	
The application expects that MiniSAT is in the project's home directory.  If 
not run with any arguments, it will attempt to run the 
puzzles/example-wikipedia.sdk sample puzzle

# Notes

Uses George Ferguson's Sudoku and SudokuReader classes for reading and 
outputting sudoku puzzles.

