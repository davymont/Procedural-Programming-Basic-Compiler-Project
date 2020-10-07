package coursework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Q3 {

	public enum TokenType {
		OP_MULTIPLY, OP_DIVIDE, OP_MOD, OP_ADD, OP_SUBTRACT, OP_LESS, OP_LESSEQUAL, OP_GREATER, OP_GREATEREQUAL,
		OP_EQUAL, OP_NOTEQUAL, OP_NOT, OP_ASSIGN, OP_AND, OP_SINGLE_AND, OP_SINGLE_OR, OP_OR, OP_DOT,

		LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET, SEMICOLON, COLON, COMMA,

		KEYWORD_IF, KEYWORD_ELSE, KEYWORD_WHILE, KEYWORD_RETURN, KEYWORD_MAIN, KEYWORD_INT, KEYWORD_DOUBLE,
		KEYWORD_BOOLEAN, KEYWORD_STRING, KEYWORD_PUBLIC, KEYWORD_CLASS, KEYWORD_VOID, KEYWORD_FOR, KEYWORD_CASE,
		KEYWORD_STATIC, KEYWORD_BREAK, KEYWORD_CONTINUE, KEYWORD_DEFAULT, KEYWORD_CHARACTER, KEYWORD_SWITCH,

		IDENTIFIER, INTEGER, DOUBLE, STRING, BOOLEAN, COMMENT, STARSLASH, SLASHSTAR
	}

	public static void main(String[] args) {

		String file = readFile("src/coursework/Q3example.txt"); /* ERROR: variable name syntax error */

		scan(file);

	} // end main method

	// to scan a string to tokens
	public static void scan(String file) {

		String[] lines = file.split("\n"); // splits into string array at each \n to find line number

		// declaring variables used throughout scan method
		TokenType tt = null;
		String[] errorComment = {};
		String[] errorString = {};
		int[] errorLineNum = {};
		int errorcount = 0;

		for (int l = 0; l < lines.length; l++) { // for each line

			// declaring variables used throughout for each line loop
			int lineNumber = l + 1; // linenumber = element of array + 1 due to 0 index of array.
			String prog = lines[l]; // string is redefined with next element each time
			prog = prog + "\n"; // put \n back in after split removes it so that I can use it as end point for
								// comments
			int n = prog.length(); // index of last character of line
			int index = 0; // index of first character of line

			while (index < n) { // while index of current character is less than index of last character of
								// line, loop

				// declaring variables used throughout while on same line loop
				char ch = prog.charAt(index); // current character
				TokenType op = getOp(ch); // operator name or null
				TokenType sym = getSymbol(ch); // symbol name or null
				boolean bWhiteSpace = isWhiteSpace(ch); // boolean to indicate white space
				boolean bLetter = isLetter(ch); // boolean to indicate letter
				boolean bSpeechMark = isSpeechMark(ch); // boolean to indicate if char is speechmark
				String longOp = null;

				// if current character is space, skip it; increase index and continue while
				// loop
				if (bWhiteSpace) {
					index++;
					continue;
				} // end if whitespace

				// if operator
				else if (op != null || ch == '&' || ch == '|') // if operator or & or |
				{

					// if next char (operator) is same as current char, or is the start of a comment
					if (prog.charAt(index + 1) == ch || prog.charAt(index + 1) == '*' || prog.charAt(index + 1) == '/')

					{
						longOp = ""; // initialise string word
						longOp += prog.charAt(index); // word = word + char
						longOp += prog.charAt(index + 1); // word = char + next char
						tt = getOp(longOp); // run getOp string method on word

						// if not a comment, it's a double character operator, print operator
						if (tt != null && tt != TokenType.COMMENT && tt != TokenType.SLASHSTAR
								&& tt != TokenType.STARSLASH) {
							System.out.println(lineNumber + ", " + tt + ", " + longOp); // print longOp
							index += longOp.length(); // index = index + length of word
						}

						// if '//' comment
						else if (tt == TokenType.COMMENT) {
							ch = prog.charAt(index);
							String comment = ""; // if next ch = / .... //, initiate comment.
							index = index + 2; // take '//' out of comment string.
							int count = 1;

							while (ch != '\n') { // while comment, ignore, go to next line
								ch = prog.charAt(index);
								index++;
								comment = comment + ch;
								count++;

							}

							comment = null; // reset comment string
							index++; // move onto next character
						}

						// if '/* */' comment
						else if (tt == TokenType.SLASHSTAR) {
							ch = prog.charAt(index);
							String comment = ""; // initiate comment string
							index = index + 2; // take '/*' out of comment string.
							int count = 1;

							while (false == comment.contains("*/")) { // build comment string
								ch = prog.charAt(index);
								index++;
								comment = comment + ch;
								count++;

								// if comment is opened with /* and not closed with */ save location and
								// intended comment to array and flag at end of output
								if (index == n && (false == comment.contains("*/"))) {
									errorLineNum = addInt(errorcount, errorLineNum, lineNumber);
									errorComment = addString(errorcount, errorComment, comment);
									errorcount++;
									break;
								}

								else if (comment.contains("*/")) { // finish string when comment is closed with */
									comment = comment.substring(0, (comment.length() - 2)); // remove */ from comment
									break;
								}

							}

							comment = null; // reset comment string
							index++; // move onto next character

						}

						// if not Wee Java operator or comment, flag unknown operator
						else if (tt == null) {
							System.out.println(lineNumber + ", You have entered an unknown operator, " + longOp);
							index += longOp.length();
							continue;
						}
					}

					// else if operator and next char is =, it is a comparison operator, print
					// operator
					else if ((op != null) && (prog.charAt(index + 1) == '=')) {
						longOp = ""; // initialise string
						longOp += prog.charAt(index);
						longOp += prog.charAt(index + 1);
						tt = getOp(longOp);
						System.out.println(lineNumber + ", " + tt + ", " + longOp);
						index += longOp.length(); // increase index by length of operator
					}

					else { // else if single char op, print it
						System.out.println(lineNumber + ", " + op + ", " + ch); // print
						index++; // increase index by 1
						continue; // continue
					}
				}

				// else if current character is symbol, symbol is a token, so we want to print
				// type and name, then move onto next index
				else if (sym != null) { // if ch is a symbol
					System.out.println(lineNumber + ", " + sym + ", " + ch);
					index++;
					continue;
				} // end if symbol

				// else if current character is digit
				else if (isDigit(ch)) {
					ch = prog.charAt(index);
					String digits = "";
					// tt = null;

					while (isDigit(ch) || '.' == ch) { // while ch = digit
						digits = digits + ch; // build digit string
						ch = prog.charAt(index + 1); // digits plus decimal place
						index++; // move to next char

					} // end while
					if (digits.contains(".")) {
						tt = TokenType.DOUBLE;
						System.out.println(lineNumber + ", " + tt + ", " + digits);
					} // end if

					else {
						tt = TokenType.INTEGER;
						System.out.println(lineNumber + ", " + tt + ", " + digits);
					} // end else
				} // end if digit

				// if char is speechmark "
				else if (bSpeechMark) {

					String word = ""; // initiate string
					word += ch;
					int count = 1;

					// while next char is not speechmark or new line, build string
					while (false == isSpeechMark(prog.charAt(index + count)) && prog.charAt(index + count) != '\n') {
						count++; // increase count
						word = prog.substring(index, index + (count + 1)); // word = string from index to index+count+1
					}

					// if next char is speechmark, finish string
					if (true == isSpeechMark(prog.charAt(index + count))) {
						break;
					}

					// if new line and string has not been completed, add error to array to flag at
					// end of output
					else if (prog.charAt(index + count) == '\n') {

						errorLineNum = addInt(errorcount, errorLineNum, lineNumber);
						errorString = addString(errorcount, errorString, word);
						errorcount++;
						break;
					} // end if unfinished string

					{
						System.out.println(lineNumber + ", " + TokenType.STRING + ", " + word); // print string
						index += word.length(); // move to next char after string
						continue;
					}
				} // end if speechmark

				// if letter but not in string
				else if (bLetter) {
					String word = ""; // initialise string word
					word = word + ch;
					index++;

					while (index < n) { // while ch is a letter, add to string 'word'
						ch = prog.charAt(index);
						if (isLetter(ch)) {
							word = word + ch;
							index++;
							continue;
						} else {
							break;
						}
					} // end while letter

					tt = getKeyword(word); // run getKeyword method on word string to see if it is a keyword
					
						if (tt == null) {
							tt = TokenType.IDENTIFIER;
							System.out.println(lineNumber + ", " + tt + ", " + word);
							continue;
						} else // if other keyword, print
							System.out.println(lineNumber + ", " + tt + ", " + word);
						continue;
					}

				// if new line character, increase line count, continue to next character
				else if (isNewLine(ch)) {
					lineNumber++;
					index++;
					continue;
				} // end else if new line

				else { // if completely foreign character, flag this input, then continue to next
						// character
					System.out.println(lineNumber + ", You are giving me an unknown: " + ch);
					index++;
					continue;
				} // end else unknown

			} // end while on same line
		} // end for all lines in file

		if (errorComment.length >= 1) { // if a comment error has been found, flag this at the end of the output
			int index = 0;
			System.out.println();
			while (index < errorComment.length) {
				System.out.println("Incomplete comment, please try again (add */ to complete comment)");
				System.out.println("Line " + errorLineNum[index] + ", " + errorComment[index]);
				index++;
			}
		}

	} // end scan method
	
	/**
	 * Method to add new int element to array
	 * 
	 * @param n
	 * @param arr
	 * @param x
	 * @return int array
	 */
	public static int[] addInt(int n, int arr[], int x) {
		int i;

		// create a new array of size n+1
		int newarr[] = new int[n + 1];

		// insert the elements from
		// the old array into the new array
		// insert all elements till n
		// then insert x at n+1
		for (i = 0; i < n; i++)
			newarr[i] = arr[i];

		newarr[n] = x;

		return newarr;
	} // end method

	/**
	 * Function to add new String element to array
	 * 
	 * @param n
	 * @param arr
	 * @param x
	 * @return String array
	 */
	public static String[] addString(int n, String arr[], String x) {
		int i;

		// create a new array of size n+1
		String newarr[] = new String[n + 1];

		// insert the elements from
		// the old array into the new array
		// insert all elements till n
		// then insert x at n+1
		for (i = 0; i < n; i++)
			newarr[i] = arr[i];

		newarr[n] = x;

		return newarr;
	} // end method

	/**
	 * Method 'readFile' reads file as string
	 * 
	 * @param fname
	 * @return content
	 */
	public static String readFile(String fname) {

		String content = null;
		try {
			content = new String(Files.readAllBytes(Paths.get(fname)));
		} catch (IOException e) {
			System.out.println("Fail to read a file");
		}

		return content;
	} // end method readFile

	/**
	 * method 'getOp(ch)' scans characters for Wee Java Operators and returns the
	 * Token Type it finds.
	 * 
	 * @param ch
	 * @return TokenType tt else null
	 */
	public static TokenType getOp(char ch) {

		TokenType tt = null;

		switch (ch) {

		case '*':
			tt = TokenType.OP_MULTIPLY;
			break;
		case '/':
			tt = TokenType.OP_DIVIDE;
			break;
		case '%':
			tt = TokenType.OP_MOD;
			break;
		case '+':
			tt = TokenType.OP_ADD;
			break;
		case '-':
			tt = TokenType.OP_SUBTRACT;
			break;
		case '<':
			tt = TokenType.OP_LESS;
			break;
		case '>':
			tt = TokenType.OP_GREATER;
			break;
		case '!':
			tt = TokenType.OP_NOT;
			break;
		case '=':
			tt = TokenType.OP_ASSIGN;
			break;
		case '.':
			tt = TokenType.OP_DOT;
			break;
		case '&':
			tt = TokenType.OP_SINGLE_AND;
			break;
		case '|':
			tt = TokenType.OP_SINGLE_OR;
			break;

		default:
			break;
		} // end switch
		return tt;
	} // end method getOp ch

	/**
	 * method 'getOp(s)' scans multiple characters (strings) for Wee Java Operators
	 * and returns the Token Type it finds.
	 * 
	 * @param s
	 * @return TokenType tt else null
	 */
	public static TokenType getOp(String s) {

		TokenType tt = null;

		switch (s) {

		case "<=":
			tt = TokenType.OP_LESSEQUAL;
			break;
		case ">=":
			tt = TokenType.OP_GREATEREQUAL;
			break;
		case "==":
			tt = TokenType.OP_EQUAL;
			break;
		case "!=":
			tt = TokenType.OP_NOTEQUAL;
			break;
		case "&&":
			tt = TokenType.OP_AND;
			break;
		case "||":
			tt = TokenType.OP_OR;
			break;
		case "//":
			tt = TokenType.COMMENT;
			break;
		case "/*":
			tt = TokenType.SLASHSTAR;
			break;
		case "*/":
			tt = TokenType.STARSLASH;
			break;

		default:
			break;
		} // end switch
		return tt;
	} // end method getOp s

	/**
	 * method 'getSymbol(ch)' scans characters for Wee Java Symbols and returns the
	 * Token Type it finds.
	 * 
	 * @param ch
	 * @return TokenType tt else null
	 */
	public static TokenType getSymbol(char ch) {

		TokenType tt = null;

		switch (ch) {

		case '(':
			tt = TokenType.LEFT_PAREN;
			break;
		case ')':
			tt = TokenType.RIGHT_PAREN;
			break;
		case '{':
			tt = TokenType.LEFT_BRACE;
			break;
		case '}':
			tt = TokenType.RIGHT_BRACE;
			break;
		case '[':
			tt = TokenType.LEFT_BRACKET;
			break;
		case ']':
			tt = TokenType.RIGHT_BRACKET;
			break;
		case ';':
			tt = TokenType.SEMICOLON;
			break;
		case ':':
			tt = TokenType.COLON;
			break;
		case ',':
			tt = TokenType.COMMA;
			break;

		default:
			break;
		} // end switch
		return tt;
	} // end method getSymbol

	/**
	 * method 'getKeyword(s)' scans multiple characters (strings) for Wee Java
	 * Keywords and returns the Token Type it finds.
	 * 
	 * @param s
	 * @return TokenType tt else null
	 */
	public static TokenType getKeyword(String s) {

		TokenType tt = null;

		switch (s) {

		case "if":
			tt = TokenType.KEYWORD_IF;
			break;
		case "else":
			tt = TokenType.KEYWORD_ELSE;
			break;
		case "while":
			tt = TokenType.KEYWORD_WHILE;
			break;
		case "return":
			tt = TokenType.KEYWORD_RETURN;
			break;
		case "main":
			tt = TokenType.KEYWORD_MAIN;
			break;
		case "int":
			tt = TokenType.KEYWORD_INT;
			break;
		case "char":
			tt = TokenType.KEYWORD_CHARACTER;
			break;
		case "double":
			tt = TokenType.KEYWORD_DOUBLE;
			break;
		case "boolean":
			tt = TokenType.KEYWORD_BOOLEAN;
			break;
		case "String":
			tt = TokenType.KEYWORD_STRING;
			break;
		case "public":
			tt = TokenType.KEYWORD_PUBLIC;
			break;
		case "class":
			tt = TokenType.KEYWORD_CLASS;
			break;
		case "void":
			tt = TokenType.KEYWORD_VOID;
			break;
		case "for":
			tt = TokenType.KEYWORD_FOR;
			break;
		case "case":
			tt = TokenType.KEYWORD_CASE;
			break;
		case "static":
			tt = TokenType.KEYWORD_STATIC;
			break;
		case "break":
			tt = TokenType.KEYWORD_BREAK;
			break;
		case "continue":
			tt = TokenType.KEYWORD_CONTINUE;
			break;
		case "default":
			tt = TokenType.KEYWORD_DEFAULT;
			break;
		case "switch":
			tt = TokenType.KEYWORD_SWITCH;
			break;

		default:
			break;
		} // end switch

			return tt;
	} // end getKeyword method

	/**
	 * method 'isLetter(ch)' scans characters for Letters and returns true if ch =
	 * letter and false if ch != letter.
	 * 
	 * @param ch
	 * @return boolean
	 */
	public static boolean isLetter(char ch) {
		// abcd....z, ABCD...Z

		if (ch >= 'a' && ch <= 'z')
			return true;
		else if (ch >= 'A' && ch <= 'Z')
			return true;
		else
			return false;

	} // end isLetter method.

	/**
	 * method 'isDigit(ch)' scans characters for Digits and returns true if ch =
	 * digit and false if ch != digit.
	 * 
	 * @param ch
	 * @return boolean
	 */
	public static boolean isDigit(char ch) {
		// 0,1,2...8,9

		int tempD = 0;

		String digits = "0123456789";
		char[] digitArray = digits.toCharArray();

		for (int i = 0; i < digitArray.length; i++) {

			if (ch == (digitArray[i])) {
				tempD = 1;
			}
		} // end for loop
		return tempD == 1;

	} // end method isDigit

	/**
	 * method 'isWhiteSpace(ch)' scans characters for Whitespace and returns true if
	 * ch = Whitespace and false if ch != Whitespace.
	 * 
	 * @param ch
	 * @return boolean
	 */
	public static boolean isWhiteSpace(char ch) {

		if (ch == ' ') {
			return true;
		} // space
		else if (ch == '	') {
			return true;
		} // tab space
		else
			return false;

	} // end method isWhiteSpace

	public static boolean isSpeechMark(char ch) {
		if (ch == '"') {
			return true;
		} else
			return false;
	}

	public static boolean isNewLine(char ch) {
		if (ch == '\n') {
			return true;
		} else
			return false;
	} //end method

} // end Q3
