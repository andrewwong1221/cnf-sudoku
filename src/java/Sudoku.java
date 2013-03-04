/*
 * File: Sudoku.java
 * Creator: George Ferguson
 * Created: Mon Feb 18 18:29:36 2013
 * Time-stamp: <Tue Feb 19 20:13:56 EST 2013 ferguson>
 */

import java.io.*;

/**
 * Class representing Sudoku puzzles.
 */
public class Sudoku {

	/**
	 * Size of this Sudoku. This must be a perfect square, and is equal
	 * to the square of the subsize.
	 */
	protected int size;

	/**
	 * Size of each of the <q>sub-grids</q> of this Sudoku (and also
	 * the number of sub-grids in each dimension). This is the square
	 * root of the size.
	 */
	protected int subsize;

	/**
	 * The digits in this Sudoku (0 means empty location).
	 */
	protected int[][] digits;

	/**
	 * Construct and return a new Sudoku with the given size (which
	 * must be a perfect square, although we don't check that).
	 */
	public Sudoku(int size) {
		this.size = size;
		this.subsize = (int)Math.sqrt(size);
		this.digits = new int[size][size];
	}

	/**
	 * Return the size of this Sudoku.
	 */
	public int size() {
		return size;
	}

	/**
	 * Return the subsize (size of the sub-grids) of this Sudoku.
	 */
	public int subsize() {
		return subsize;
	}

	/**
	 * Return the digit at the given location of this Sudoku.
	 */
	public int get(int row, int col) {
		return digits[row][col];
	}

	/**
	 * Set the given location of this Sudoku to the given digit.
	 * Note that this currently does not test that the constraints on
	 * a Sudoku are still preserved. It would not be hard to test that
	 * and throw an exception if not.
	 */
	public void set(int row, int col, int digit) {
		digits[row][col] = digit;
	}

	// Printing

	/**
	 * Print this Sudoku to the given PrintWriter.
	 */
	public void print(PrintWriter out) {
		out.println(size);
		printHorizontalSeparator(out);
		for (int row=0; row < size; row++) {
			out.print("|");
			for (int col=0; col < size; col++) {
				if (digits[row][col] == 0) {
					out.print(".");
				} else {
					out.print(digits[row][col]);
				}
				if ((col+1) % subsize == 0) {
					out.print("|");
				}
			}
			out.println();
			if ((row+1) % subsize == 0) {
				printHorizontalSeparator(out);
			}
		}
	}

	/**
	 * Print the horizontal separator used in drawing this Sudoku
	 * to the given PrintWriter.
	 */
	protected void printHorizontalSeparator(PrintWriter out) {
		out.print("+");
		for (int col=0; col < size; col++) {
			out.print('-');
			if ((col+1) % subsize == 0) {
				out.print("+");
			}
		}
		out.println();
	}

	/**
	 * Print this Sudoku to the given PrintStream.
	 */
	public void print(PrintStream out) {
		print(new PrintWriter(out, true));
	}

	/**
	 * Print this Sudoku to System.out (stdout).
	 */
	public void print() {
		print(System.out);
	}

	/**
	 * Return the string representation of this Sudoku.
	 */
	public String toString() {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		print(out);
		out.flush();
		return writer.toString();
	}

	/**
	 * Test driver for Sudoku.
	 */
	public static void main(String[] argv) throws IOException {
		String filename = argv[0];
		Sudoku sudoku = SudokuReader.from(filename);
		sudoku.print();
	}
}
