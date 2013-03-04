/*
 * File: SudokuReader.java
 * Creator: George Ferguson
 * Created: Mon Feb 18 18:35:43 2013
 * Time-stamp: <Tue Feb 19 20:16:03 EST 2013 ferguson>
 */

import java.io.*;

/**
 * Class for reading Sudoku from files. The file format is as follows:
 * <ul>
 * <li>Comments are allowed at the start of the file, using lines
 * that start with a hash sign.</li>
 * <li>The first non-comment line of the file is the size of puzzle</li>
 * <li>Subsequent lines are the rows of the sudoku</li>
 * <li>A digit means the digit at that location</li>
 * <li>Zero, a dot or a space means empty space at that location</li>
 * <li>All other characters are ignored/skipped, so you can
 * use |, -, +, etc. if you want to format the file.</li>
 * </ul>
 * Example from wikipedia:
 * <img src="http://en.wikipedia.org/wiki/File:Sudoku-by-L2G-20050714.svg">
 * <code>
# Example Sudoku from wikipedia:
9
+---+---+---+
|53.|.7.|...|
|6..|195|...|
|.98|...|.6.|
+---+---+---+
|8..|.6.|..3|
|4..|8.3|..1|
|7..|.2.|..6|
+---+---+---+
|.6.|...|28 |
|...|419|  5|
|...|.8.| 79|
+---+---+---+
 *</code>
 */
public class SudokuReader {

	/**
	 * Read a Sudoku from the given File and return it.
	 */
	public static Sudoku from(File file) throws IOException {
		return new SudokuReader(file).readSudoku();
	}

	/**
	 * Read a Sudoku from the file with the given filename and return it.
	 */
	public static Sudoku from(String filename) throws IOException {
		return new SudokuReader(filename).readSudoku();
	}

	/**
	 * Construct and return a new SudokuReader reading from the file
	 * with the given filename.
	 */
	public SudokuReader(String filename) throws FileNotFoundException {
		this(new FileReader(filename));
	}

	/**
	 * Construct and return a new SudokuReader reading from the given File.
	 */
	public SudokuReader(File file) throws FileNotFoundException {
		this(new FileReader(file));
	}

	/**
	 * Construct and return a new SudokuReader reading from the given
	 * FileInputStream.
	 */
	public SudokuReader(FileInputStream fis) {
		this(new InputStreamReader(fis));
	}

	/**
	 * Construct and return a new SudokuReader reading from the given
	 * Reader.
	 */
	public SudokuReader(Reader reader) {
		input = new PushbackReader(reader);
	}

	/**
	 * The PushbackReader being read by this SudokuReader.
	 */
	protected PushbackReader input;

	/**
	 * Read a Sudoku from this SudokuReader's input and return it.
	 */
	public Sudoku readSudoku() throws IOException {
		skipComments();
		skipWhitespace();
		int size = readNumber();
		Sudoku sudoku = new Sudoku(size);
		for (int row=0; row < size; row++) {
			for (int col=0; col < size; col++) {
				int digit = readNextDigit();
				sudoku.set(row, col, digit);
			}
		}
		return sudoku;
	}

	/**
	 * Skip whitespace on the input.
	 */
	protected void skipWhitespace() throws IOException {
		int ch;
		while ((ch = input.read()) != -1 && Character.isWhitespace(ch)) {
			// Nada
		}
		if (ch != -1) {
			input.unread(ch);
		}
	}

	/**
	 * Skip consecutive comments on the input.
	 */
	protected void skipComments() throws IOException {
		skipWhitespace();
		while (skipComment()) {
			skipWhitespace();
		}
	}

	/**
	 * If the next character on the input is a hash sign, skip
	 * characters to a newline (or EOF). Returns true if a comment
	 * was skipped.
	 */
	protected boolean skipComment() throws IOException {
		int ch = input.read();
		if (ch == '#') {
			while ((ch = input.read()) != -1 && ch != '\n') {
				// Nada
			}
			if (ch == -1) {
				throw new EOFException();
			}
			return true;
		} else {
			input.unread(ch);
			return false;
		}
	}

	/**
	 * Read consecutive digits from the input and return the corresponding
	 * number.
	 */
	protected int readNumber() throws IOException {
		int n = 0;
		int ch;
		while ((ch = input.read()) != -1 && Character.isDigit(ch)) {
			n = n * 10 + (ch - '0');
		}
		if (ch != -1) {
			input.unread(ch);
		}
		return n;
	}

	/**
	 * Skip non-digit characters and return the next digit from the
	 * input. A period or space is also read as 0.
	 */
	protected int readNextDigit() throws IOException {
		int ch;
		while ((ch = input.read()) != -1 &&
			   !Character.isDigit(ch) &&
			   ch != '.' && ch != ' ') {
			// Nada
		}
		if (ch == -1) {
			throw new EOFException();
		} else if (ch == '.' || ch == ' ') {
			return 0;
		} else {
			return ch - '0';
		}
	}

}
