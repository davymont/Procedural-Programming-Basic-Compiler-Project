package coursework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

public class Q5 {

	public enum TokenType {
		OP_MULTIPLY, OP_DIVIDE, OP_MOD, OP_ADD, OP_SUBTRACT, OP_LESS, OP_LESSEQUAL, OP_GREATER, OP_GREATEREQUAL,
		OP_EQUAL, OP_NOTEQUAL, OP_NOT, OP_ASSIGN, OP_AND, OP_SINGLE_AND, OP_SINGLE_OR, OP_OR, OP_DOT,

		LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET, SEMICOLON, COLON, COMMA,

		KEYWORD_IF, KEYWORD_ELSE, KEYWORD_WHILE, KEYWORD_RETURN, KEYWORD_MAIN, KEYWORD_INT, KEYWORD_DOUBLE,
		KEYWORD_BOOLEAN, KEYWORD_STRING, KEYWORD_PUBLIC, KEYWORD_CLASS, KEYWORD_VOID, KEYWORD_FOR, KEYWORD_CASE,
		KEYWORD_STATIC, KEYWORD_BREAK, KEYWORD_CONTINUE, KEYWORD_DEFAULT, KEYWORD_CHARACTER, KEYWORD_SWITCH,

		IDENTIFIER, INTEGER, DOUBLE, STRING, BOOLEAN, NOTHING, COMMENT, STARSLASH, SLASHSTAR
	}

	public static void main(String[] args) {

		String file = readFile("src/coursework/Q5example3.txt");

		scan(file);

	} // end main method

	// to scan a string to tokens
	public static void scan(String file) {

		String[] lines = file.split("\n"); // splits into string array at each \n to find line number

		TokenType tt = null;

		List<String> opsAndInts = new LinkedList<String>();
		List<String> errorVar = new LinkedList<String>();
		int[] errorLineNum = {};
		int errorCount = 0;
		List<String> integerKeyword = new LinkedList<String>();
		List<String> integerNames = new LinkedList<String>();
		List<Integer> integerValues = new LinkedList<Integer>();
		int integerCount = 0;

		for (int l = 0; l < lines.length; l++) { // for each line

			int lineNumber = l + 1; // linenumber = element of array + 1 due to 0 index.

			String prog = lines[l]; // string is redefined with next element each time

			prog = prog + "\n"; // put \n back in after split removes it so that I can use it as end point for
								// comments
			// System.out.println("\n*** " + lineNumber + ", " + prog + " ***\n");

			int n = prog.length(); // index of last character of line
			int index = 0; // index of first character of line

			while (index < n) { // while index of current character is less than index of last character of
								// line, loop

				char ch = prog.charAt(index); // current character

				TokenType op = getOp(ch); // operator name or null
				TokenType sym = getSymbol(ch); // symbol name or null
				boolean bWhiteSpace = isWhiteSpace(ch); // boolean to indicate white space
				boolean bLetter = isLetter(ch); // boolean to indicate letter
				boolean bSpeechMark = isSpeechMark(ch);
				String longOp = null;

				// if current character is space, skip it; increase index and continue while
				// loop
				if (bWhiteSpace) {
					index++;
					continue;
				} // end if whitespace

				// operator
				else if (op != null || ch == '&' || ch == '|') // if operator or & or |
				{

					if (prog.charAt(index + 1) == ch || prog.charAt(index + 1) == '*' || prog.charAt(index + 1) == '/') // if
																														// next
																														// char
																														// =same
																														// as
																														// previous
																														// char
					{
						longOp = ""; // initialise string word
						longOp += prog.charAt(index); // word = word + char
						longOp += prog.charAt(index + 1); // word = char + next char
						tt = getOp(longOp); // run getOp string method on word

						if (tt != null && tt != TokenType.COMMENT && tt != TokenType.SLASHSTAR
								&& tt != TokenType.STARSLASH) {
							// System.out.println(lineNumber + ", " + tt + ", " + longOp); //print
							index += longOp.length(); // index = index + length of word
						}
						//////////////////////////////////////
						else if (tt == TokenType.COMMENT) {
							ch = prog.charAt(index);
							String comment = ""; // if next ch = / .... //, initiate comment.
							index = index + 2; // take '//' out of comment string.
							int count = 1;

							while (ch != '\n') { // while ch
								ch = prog.charAt(index);
								index++;
								comment = comment + ch;
								count++;

							}

							// System.out.print(lineNumber + ", " + tt + ", " + comment); (to 'skip'
							// comment)
							comment = null;
							index++;
						}

						else if (tt == TokenType.SLASHSTAR) {
							ch = prog.charAt(index);
							String comment = ""; // if next ch = / .... //, initiate comment.
							index = index + 2; // take '//' out of comment string.
							int count = 1;

							while (false == comment.contains("*/")) {
								ch = prog.charAt(index);
								index++;
								comment = comment + ch;
								count++;

								if (index == n && (false == comment.contains("*/"))) {
									// System.out.println("* You have incorrectly entered the comment below, please
									// try again (add */ to finish comment) *"); // to 'skip' comment
									break;
								}

								else if (comment.contains("*/")) {
									comment = comment.substring(0, (comment.length() - 2));
									break;
								}

							}

							// System.out.println(lineNumber + ", COMMENT, " + comment); // to 'skip'
							// comment
							comment = null;
							index++;

						}

						else if (tt == null) {// System.out.println(lineNumber + ", You have entered an unknown Op, " +
												// longOp);
							index += longOp.length();
							continue;
						}
					}

					else if ((op != null) && (prog.charAt(index + 1) == '=')) { // else if operator and next char is =
						longOp = ""; // initialise string word
						longOp += prog.charAt(index);
						longOp += prog.charAt(index + 1);
						tt = getOp(longOp);
						// System.out.println(lineNumber + ", " + tt + ", " + longOp);
						index += longOp.length();
					}

					else { // else if single op
							// System.out.println(lineNumber + ", " + op + ", " + ch); //print
						index++; // increase index by 1
						continue; // continue
					}
				}

				// else if current character is symbol, symbol is a token, so we want to print
				// type and name, then move onto next index
				else if (sym != null) { // if ch is a symbol
					// System.out.println(lineNumber + ", " + sym + ", " + ch);
					index++;
					continue;
				} // end if symbol

				else if (isDigit(ch)) { // if ch = digit
					ch = prog.charAt(index);
					String digits = "";
					// tt = null;

					while (isDigit(ch) || '.' == ch) { // while ch = digit
						digits = digits + ch;

						ch = prog.charAt(index + 1);
						index++;

					} // end while
					if (digits.contains(".")) {
						tt = TokenType.DOUBLE;
						// System.out.println(lineNumber + ", " + tt + ", " + digits);
					} // end if

					else {
						tt = TokenType.INTEGER;
						// System.out.println(lineNumber + ", " + tt + ", " + digits);

						// digits = null;
					} // end else
				} // end if digit

				else if (bSpeechMark) {

					String word = "";
					word += ch;
					int count = 1;

					if (false == isSpeechMark(prog.charAt(index + count))) {
						while (false == isSpeechMark(prog.charAt(index + count))) {
							count++; // increase count
							word = prog.substring(index, index + (count + 1)); // number = string from index to
																				// index+count+1
							// System.out.println("string " + word);
							continue;
						}
					} else if (true == isSpeechMark(prog.charAt(index + count))) {
						break;
					}
					// end while
					{// System.out.println(lineNumber + ", " + TokenType.STRING + ", " + word);
						index += word.length();
						continue;
					}
				}

				else if (bLetter) {
					String word = "";
					word = word + ch;
					index++;
					// TokenType tt = null;
					while (index < n) {
						ch = prog.charAt(index);
						if (isLetter(ch)) {
							word = word + ch;
							index++;
							continue;
						} else {
							break;
						}
					} // end while
					tt = getKeyword(word);

					if (tt == TokenType.KEYWORD_INT) {
						if (isWhiteSpace(prog.charAt(index))) {
							index++;
							while (isWhiteSpace(prog.charAt(index))) {
								index++;
								continue;
							}
							String var = "";
							while (false == isWhiteSpace(prog.charAt(index))) {
								ch = prog.charAt(index);
								if (ch == '=' || ch == ';') {
									break;
								}
								var = var + ch;
								index++;

							} // end while

							boolean vNameCheck = variableNameCheck(var);
							if (vNameCheck == false) {

								errorLineNum = addInt(errorCount, errorLineNum, lineNumber);
								// System.out.println(lineNumber + ", " + tt + ", " + word);

								errorVar.add(var);
								errorCount++;

							} else if (vNameCheck == true) {
								// System.out.println(lineNumber + ", " + tt + ", " + word);
								tt = TokenType.IDENTIFIER;
								// System.out.println(lineNumber + ", " + tt + ", " + var);

// Q5  *********************************************************************************************************************************  Q5//	

								if (isWhiteSpace(prog.charAt(index)) || prog.charAt(index) == '=') {

									String intValueString = "";
									int intValue = 0;
									index++;
									while (isWhiteSpace(prog.charAt(index)) || prog.charAt(index) == '=') {
										index++;
										continue;
									} // is identifier (word(int)), called var... gets us to what's on other side of =
									ch = prog.charAt(index);

									if (isDigit(ch) || isLetter(ch) || arithmeticOp(ch) != null) { // if digit, letter
																									// or operator, add
																									// to string.

										while (ch != ';') {

											intValueString = intValueString + ch;

											ch = prog.charAt(index + 1);

											index++;
											continue;

										} // end string creator (from '=' to ';')

										for (int i = 0; i < intValueString.length(); i++) { // traverse string of
																							// characters after '='
																							// before ';'
											char chInt = intValueString.charAt(i);
											int multiplied = 0;

											if (getOp(chInt) != null) { // if any char after '=' is operator
												// make list of each char

												///////////////////// if the operator is *,/,+,-
												if (intValueString.contains("*") || intValueString.contains("/")
														|| intValueString.contains("+")
														|| intValueString.contains("-")) {
													// split at either side of each operator
													opsAndInts = new LinkedList<>(Arrays.asList(intValueString.split(
															"((?<=\\*)|(?=\\*)|(?<=\\/)|(?=\\/)|(?<=\\+)|(?=\\+)|(?<=\\-)|(?=\\-))"))); // split
																																		// into
																																		// list
																																		// around
																																		// '*'

													int multiply = 0;
													int leftMultInt = 0;
													int rightMultInt = 0;
													String leftMultStr = "";
													String rightMultStr = "";
													String multipliedStr = "";

													while (opsAndInts.size() > 1) {

														if (!opsAndInts.contains("*")) { // if list doesn't contain '*',
																							// break, go to '/' method
															break;
														}

														multiply = opsAndInts.indexOf("*"); // find index of *
														multiplied = 0;
														// System.out.println("index of op: " + multiply);
														// System.out.println(opsAndInts.get(multiply-1));

														// for left of '*'

														for (int count = 0; count < opsAndInts.get(multiply - 1)
																.length(); count++) {
															char countch = opsAndInts.get(multiply - 1).charAt(count);

															if (isDigit(countch)) {
																leftMultStr = (opsAndInts.get(multiply - 1));
																// System.out.println("." + leftMultStr + ".");

															} else if (isLetter(countch)) {
																leftMultStr = (opsAndInts.get(multiply - 1));
																// System.out.println("_" + leftMultStr + "_");//end if
																// letter

																if (integerNames.contains(leftMultStr)) { // if variable
																											// is in
																											// array (as
																											// string)
																	int temp = integerNames.indexOf(leftMultStr); // int
																													// temp
																													// =
																													// index
																													// of
																													// variable
																													// in
																													// array
																	int tempLeftMultInt = integerValues.get(temp);
																	leftMultStr = Integer.toString(tempLeftMultInt);
																} else {
																	System.out.println(
																			"Unresolved compilation problem: \""
																					+ leftMultStr
																					+ "\" cannot be resolved to a variable");
																	break;
																	// end else incorrect identifier name

																}
															}
														} // end for left of '*'

														// for right of '*'
														// trying to make string of right of *, until operator.

														for (int count = 0; count < opsAndInts.get(multiply + 1)
																.length(); count++) {
															char countch = opsAndInts.get(multiply + 1).charAt(count);

															if (isDigit(countch)) {
																rightMultStr = (opsAndInts.get(multiply + 1));
																// System.out.println("." + rightMultStr + ".");

															} else if (isLetter(countch)) {
																rightMultStr = (opsAndInts.get(multiply + 1));
																// System.out.println("-" + rightMultStr + "-");//end if
																// letter

																if (integerNames.contains(rightMultStr)) { // if
																											// variable
																											// is in
																											// array (as
																											// string)
																	int temp = integerNames.indexOf(rightMultStr); // int
																													// temp
																													// =
																													// index
																													// of
																													// variable
																													// in
																													// array
																	int tempRightMultInt = integerValues.get(temp);
																	rightMultStr = Integer.toString(tempRightMultInt);
																} else {
																	System.out.println(
																			"Unresolved compilation problem: \""
																					+ rightMultStr
																					+ "\" cannot be resolved to a variable");
																	break;
																	// end else incorrect identifier name

																}
															}
														} // end for left of '*'

														leftMultInt = Integer.parseInt(leftMultStr);
														rightMultInt = Integer.parseInt(rightMultStr);
														// System.out.println(leftMultInt + "." + rightMultInt);
														multiplied = (leftMultInt * rightMultInt);
														multipliedStr = Integer.toString(multiplied);
														// System.out.println(multipliedStr);
														opsAndInts.add(multiply, multipliedStr);
														opsAndInts.remove(multiply - 1);
														opsAndInts.remove(multiply + 1);
														opsAndInts.remove(multiply); // maybe don't need this - just
																						// need the multipliedStr
														// want to add multipliedStr as value in big array.... or save
														// as temp.

														// System.out.println(opsAndInts + "right here, right now");

													}
												}

												// end if *

												///////////////////////////// if /
												if (intValueString.contains("/") || intValueString.contains("+")
														|| intValueString.contains("-")) { // if there's a '/' after '='

													// System.out.println("david" + opsAndInts + " is great " +
													// intValueString);

													int multiply = 0;
													int leftMultInt = 0;
													int rightMultInt = 0;
													String leftMultStr = "";
													String rightMultStr = "";
													String multipliedStr = "";

													while (opsAndInts.size() > 1) {

														if (!opsAndInts.contains("/")) { // if list doesn't contain '/',
																							// break, go to '+' method
															break;
														}
														multiply = opsAndInts.indexOf("/"); // find index of *
														multiplied = 0;
														// System.out.println("index of op: " + multiply);
														// System.out.println(opsAndInts.get(multiply-1));

														// for left of '*'

														for (int count = 0; count < opsAndInts.get(multiply - 1)
																.length(); count++) {
															char countch = opsAndInts.get(multiply - 1).charAt(count);

															if (isDigit(countch)) {
																leftMultStr = (opsAndInts.get(multiply - 1));
																// System.out.println("." + leftMultStr + ".");

															} else if (isLetter(countch)) {
																leftMultStr = (opsAndInts.get(multiply - 1));
																// System.out.println("_" + leftMultStr + "_");//end if
																// letter

																if (integerNames.contains(leftMultStr)) { // if variable
																											// is in
																											// array (as
																											// string)
																	int temp = integerNames.indexOf(leftMultStr); // int
																													// temp
																													// =
																													// index
																													// of
																													// variable
																													// in
																													// array
																	int tempLeftMultInt = integerValues.get(temp);
																	leftMultStr = Integer.toString(tempLeftMultInt);
																} else {
																	System.out.println(
																			"Unresolved compilation problem: \""
																					+ leftMultStr
																					+ "\" cannot be resolved to a variable");
																	break;
																	// end else incorrect identifier name

																}
															}
														} // end for left of '*'

														// for right of '*'
														// trying to make string of right of *, until operator.

														for (int count = 0; count < opsAndInts.get(multiply + 1)
																.length(); count++) {
															char countch = opsAndInts.get(multiply + 1).charAt(count);

															if (isDigit(countch)) {
																rightMultStr = (opsAndInts.get(multiply + 1));
																// System.out.println("." + rightMultStr + ".");

															} else if (isLetter(countch)) {
																rightMultStr = (opsAndInts.get(multiply + 1));
																// System.out.println("-" + rightMultStr + "-");//end if
																// letter

																if (integerNames.contains(rightMultStr)) { // if
																											// variable
																											// is in
																											// array (as
																											// string)
																	int temp = integerNames.indexOf(rightMultStr); // int
																													// temp
																													// =
																													// index
																													// of
																													// variable
																													// in
																													// array
																	int tempRightMultInt = integerValues.get(temp);
																	rightMultStr = Integer.toString(tempRightMultInt);
																} else {
																	System.out.println(
																			"Unresolved compilation problem: \""
																					+ rightMultStr
																					+ "\" cannot be resolved to a variable");
																	break;
																	// end else incorrect identifier name

																}
															}
														} // end for left of '*'

														leftMultInt = Integer.parseInt(leftMultStr);
														rightMultInt = Integer.parseInt(rightMultStr);
														// System.out.println(leftMultInt + "." + rightMultInt);
														multiplied = (leftMultInt / rightMultInt);
														multipliedStr = Integer.toString(multiplied);
														// System.out.println(multipliedStr);
														opsAndInts.add(multiply, multipliedStr);
														opsAndInts.remove(multiply - 1);
														opsAndInts.remove(multiply + 1);
														opsAndInts.remove(multiply); // maybe don't need this - just
																						// need the multipliedStr
														// want to add multipliedStr as value in big array.... or save
														// as temp.

														// System.out.println(opsAndInts + "right here, right now");

													}
												}

												// end if /

												//////////////////////// if +
												if (intValueString.contains("+") || intValueString.contains("-")) { // if
																													// there's
																													// a
																													// '+'

													// System.out.println("oh" + opsAndInts + " feck aye " +
													// intValueString);

													int multiply = 0;
													int leftMultInt = 0;
													int rightMultInt = 0;
													String leftMultStr = "";
													String rightMultStr = "";
													String multipliedStr = "";

													while (opsAndInts.size() > 1) {

														if (!opsAndInts.contains("+")) { // if list doesn't contain '+',
																							// break, go to '-' method
															break;
														}
														multiply = opsAndInts.indexOf("+"); // find index of *
														multiplied = 0;
														// System.out.println("index of op: " + multiply);
														// System.out.println(opsAndInts.get(multiply-1));

														// for left of '*'

														for (int count = 0; count < opsAndInts.get(multiply - 1)
																.length(); count++) {
															char countch = opsAndInts.get(multiply - 1).charAt(count);

															if (isDigit(countch)) {
																leftMultStr = (opsAndInts.get(multiply - 1));
																// System.out.println("." + leftMultStr + ".");

															} else if (isLetter(countch)) {
																leftMultStr = (opsAndInts.get(multiply - 1));
																// System.out.println("_" + leftMultStr + "_");//end if
																// letter

																if (integerNames.contains(leftMultStr)) { // if variable
																											// is in
																											// array (as
																											// string)
																	int temp = integerNames.indexOf(leftMultStr); // int
																													// temp
																													// =
																													// index
																													// of
																													// variable
																													// in
																													// array
																	int tempLeftMultInt = integerValues.get(temp);
																	leftMultStr = Integer.toString(tempLeftMultInt);
																} else {
																	System.out.println(
																			"Unresolved compilation problem: \""
																					+ leftMultStr
																					+ "\" cannot be resolved to a variable");
																	break;
																	// end else incorrect identifier name

																}
															}
														} // end for left of '*'

														// for right of '*'
														// trying to make string of right of *, until operator.

														for (int count = 0; count < opsAndInts.get(multiply + 1)
																.length(); count++) {
															char countch = opsAndInts.get(multiply + 1).charAt(count);

															if (isDigit(countch)) {
																rightMultStr = (opsAndInts.get(multiply + 1));
																// System.out.println("." + rightMultStr + ".");

															} else if (isLetter(countch)) {
																rightMultStr = (opsAndInts.get(multiply + 1));
																// System.out.println("-" + rightMultStr + "-");//end if
																// letter

																if (integerNames.contains(rightMultStr)) { // if
																											// variable
																											// is in
																											// array (as
																											// string)
																	int temp = integerNames.indexOf(rightMultStr); // int
																													// temp
																													// =
																													// index
																													// of
																													// variable
																													// in
																													// array
																	int tempRightMultInt = integerValues.get(temp);
																	rightMultStr = Integer.toString(tempRightMultInt);
																} else {
																	System.out.println(
																			"Unresolved compilation problem: \""
																					+ rightMultStr
																					+ "\" cannot be resolved to a variable");
																	break;
																	// end else incorrect identifier name

																}
															}
														} // end for left of '*'

														leftMultInt = Integer.parseInt(leftMultStr);
														rightMultInt = Integer.parseInt(rightMultStr);
														// System.out.println(leftMultInt + "." + rightMultInt);
														multiplied = (leftMultInt + rightMultInt);
														multipliedStr = Integer.toString(multiplied);
														// System.out.println(multipliedStr);
														opsAndInts.add(multiply, multipliedStr);
														opsAndInts.remove(multiply - 1);
														opsAndInts.remove(multiply + 1);
														opsAndInts.remove(multiply); // maybe don't need this - just
																						// need the multipliedStr
														// want to add multipliedStr as value in big array.... or save
														// as temp.

														// System.out.println(opsAndInts + "right here, right now");

													}
												}

												// end if +

												//////////////////////// if -
												if (intValueString.contains("-")) { // if there's a '+'

													// System.out.println("you" + opsAndInts + " beauty " +
													// intValueString);

													int multiply = 0;
													int leftMultInt = 0;
													int rightMultInt = 0;
													String leftMultStr = "";
													String rightMultStr = "";
													String multipliedStr = "";

													while (opsAndInts.size() > 1) {

														// System.out.println("--------" + opsAndInts);

														multiply = opsAndInts.indexOf("-"); // find index of *
														multiplied = 0;
														// System.out.println("index of op: " + multiply);
														// System.out.println(opsAndInts.get(multiply-1));

														// for left of '*'

														for (int count = 0; count < opsAndInts.get(multiply - 1)
																.length(); count++) {
															char countch = opsAndInts.get(multiply - 1).charAt(count);

															if (isDigit(countch)) {
																leftMultStr = (opsAndInts.get(multiply - 1));
																// System.out.println("." + leftMultStr + ".");

															} else if (isLetter(countch)) {
																leftMultStr = (opsAndInts.get(multiply - 1));
																// System.out.println("_" + leftMultStr + "_");//end if
																// letter

																if (integerNames.contains(leftMultStr)) { // if variable
																											// is in
																											// array (as
																											// string)
																	int temp = integerNames.indexOf(leftMultStr); // int
																													// temp
																													// =
																													// index
																													// of
																													// variable
																													// in
																													// array
																	int tempLeftMultInt = integerValues.get(temp);
																	leftMultStr = Integer.toString(tempLeftMultInt);
																} else {
																	System.out.println(
																			"Unresolved compilation problem: \""
																					+ leftMultStr
																					+ "\" cannot be resolved to a variable");
																	break;
																	// end else incorrect identifier name

																}
															}
														} // end for left of '*'

														// for right of '*'
														// trying to make string of right of *, until operator.

														for (int count = 0; count < opsAndInts.get(multiply + 1)
																.length(); count++) {
															char countch = opsAndInts.get(multiply + 1).charAt(count);

															if (isDigit(countch)) {
																rightMultStr = (opsAndInts.get(multiply + 1));
																// System.out.println("." + rightMultStr + ".");

															} else if (isLetter(countch)) {
																rightMultStr = (opsAndInts.get(multiply + 1));
																// System.out.println("-" + rightMultStr + "-");//end if
																// letter

																if (integerNames.contains(rightMultStr)) { // if
																											// variable
																											// is in
																											// array (as
																											// string)
																	int temp = integerNames.indexOf(rightMultStr); // int
																													// temp
																													// =
																													// index
																													// of
																													// variable
																													// in
																													// array
																	int tempRightMultInt = integerValues.get(temp);
																	rightMultStr = Integer.toString(tempRightMultInt);
																} else {
																	System.out.println(
																			"Unresolved compilation problem: \""
																					+ rightMultStr
																					+ "\" cannot be resolved to a variable");
																	break;
																	// end else incorrect identifier name

																}
															}
														} // end for left of '*'

														leftMultInt = Integer.parseInt(leftMultStr);
														rightMultInt = Integer.parseInt(rightMultStr);
														// System.out.println(leftMultInt + "." + rightMultInt);
														multiplied = (leftMultInt - rightMultInt);
														multipliedStr = Integer.toString(multiplied);
														// System.out.println(multipliedStr);
														opsAndInts.add(multiply, multipliedStr);
														opsAndInts.remove(multiply - 1);
														opsAndInts.remove(multiply + 1);
														opsAndInts.remove(multiply); // maybe don't need this - just
																						// need the multipliedStr
														// want to add multipliedStr as value in big array.... or save
														// as temp.

														// System.out.println(opsAndInts + "right here, right now");

													}
												}

												// end if +

												// add value to lists if just single int number after '='.
											} else if (false == intValueString.contains("[a-zA-Z]+")
													&& false == intValueString.contains("*")
													&& false == intValueString.contains("/")
													&& false == intValueString.contains("+")
													&& false == intValueString.contains("-")) {

												// System.out.println("great!");
												intValue = Integer.parseInt(intValueString);
												integerKeyword.add(word);
												integerNames.add(var);
												integerValues.add(intValue);
												// System.out.println(integerKeyword + " " + integerNames + " : " +
												// integerValues);
												break;

											}

										}
										if (opsAndInts.size() == 1) {

											// System.out.println(opsAndInts + "THIS IS IT");
											String intValueStr = opsAndInts.get(0);
											intValue = Integer.parseInt(intValueStr);
											integerKeyword.add(word);
											integerNames.add(var);
											integerValues.add(intValue);
											opsAndInts.remove(0);
										}
									}
								}

							} else {
								if (tt == null) {
									tt = TokenType.IDENTIFIER;
									// System.out.println(lineNumber + ", " + tt + ", " + word);
									continue;
								} else
									// System.out.println(lineNumber + ", " + tt + ", " + word);
									continue;
							}
						}
					}
				}

				else if (ch == '\r') {
					index++;
					continue;
				}

				else if (isNewLine(ch)) {
					lineNumber++;
					index++;
					continue;
				} // end else if new line

				else {
					System.out.println(lineNumber + ", You are giving me an unknown: " + ch);
					index++;
					continue;
				} // end else unknown

			} // end while
		} // end for
		if (errorLineNum.length >= 1) {
			int length = 0;
			System.out.println();
			while (length < errorLineNum.length) {
				System.out.println(
						"Line " + errorLineNum[length] + ", invalid identifier name, \"" + errorVar.get(length) + "\"");
				length++;
			}
		}
		if (integerNames.size() >= 1) {
			int length = 0;
			System.out.println();
			while (length < integerNames.size()) {
				System.out.println();
				System.out.println(integerNames.get(length) + " : " + integerValues.get(length));
				length++;
			}
		}

	} // end scan method

	public static int multiply(String x) {
		int multiplied = 0;

		return multiplied;
	}

	// Function to add new int element to array
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
	}

	// Function to add new String element to array
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
	}

	public static boolean variableNameCheck(String varName) {
		boolean goodVar = true;

		for (int varNameIndex = 0; varNameIndex < varName.length(); varNameIndex++) {
			char in = varName.charAt(varNameIndex);
			if (getOp(in) != null || getSymbol(in) != null || isDigit(in)) {
				goodVar = false;
				break;

			} else {
				goodVar = true;
			}
		}

		return goodVar;
	}

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
	 * method arithmeticOp is used in Q5 to make a specific group of arithmetic operators used in Q5
	 * 
	 * @param ch
	 * @return TokenType tt
	 */
	public static TokenType arithmeticOp(char ch) {

		TokenType tt = null;

		switch (ch) {

		case '*':
			tt = TokenType.OP_MULTIPLY;
			break;
		case '/':
			tt = TokenType.OP_DIVIDE;
			break;
		case '+':
			tt = TokenType.OP_ADD;
			break;
		case '-':
			tt = TokenType.OP_SUBTRACT;
			break;

		default:
			break;
		} // end switch
		return tt;
	} // end method getOp ch

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

		int temp = 0;
		TokenType tt = null;

		switch (s) {

		case "if":
			tt = TokenType.KEYWORD_IF;
			temp = 1;
			break;
		case "else":
			tt = TokenType.KEYWORD_ELSE;
			temp = 1;
			break;
		case "while":
			tt = TokenType.KEYWORD_WHILE;
			temp = 1;
			break;
		case "return":
			tt = TokenType.KEYWORD_RETURN;
			temp = 1;
			break;
		case "main":
			tt = TokenType.KEYWORD_MAIN;
			temp = 1;
			break;
		case "int":
			tt = TokenType.KEYWORD_INT;
			temp = 1;
			break;
		case "char":
			tt = TokenType.KEYWORD_CHARACTER;
			temp = 1;
			break;
		case "double":
			tt = TokenType.KEYWORD_DOUBLE;
			temp = 1;
			break;
		case "boolean":
			tt = TokenType.KEYWORD_BOOLEAN;
			temp = 1;
			break;
		case "String":
			tt = TokenType.KEYWORD_STRING;
			temp = 1;
			break;
		case "public":
			tt = TokenType.KEYWORD_PUBLIC;
			temp = 1;
			break;
		case "class":
			tt = TokenType.KEYWORD_CLASS;
			temp = 1;
			break;
		case "void":
			tt = TokenType.KEYWORD_VOID;
			temp = 1;
			break;
		case "for":
			tt = TokenType.KEYWORD_FOR;
			temp = 1;
			break;
		case "case":
			tt = TokenType.KEYWORD_CASE;
			temp = 1;
			break;
		case "static":
			tt = TokenType.KEYWORD_STATIC;
			temp = 1;
			break;
		case "break":
			tt = TokenType.KEYWORD_BREAK;
			temp = 1;
			break;
		case "continue":
			tt = TokenType.KEYWORD_CONTINUE;
			temp = 1;
			break;
		case "default":
			tt = TokenType.KEYWORD_DEFAULT;
			temp = 1;
			break;
		case "switch":
			tt = TokenType.KEYWORD_SWITCH;
			temp = 1;
			break;

		default:
			break;
		} // end switch
		if (temp == 1) {
			return tt;
		} else {
			tt = null;
			return tt;
		}
	} // end getKeyword method

	/**
	 * method 'isLetter(ch)' scans characters for Letters and returns true if ch =
	 * letter and false if ch != letter.
	 * 
	 * @param ch
	 * @return true or false
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
	 * @return true or false
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
	 * @return true or false
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

	/**
	 * method isSpechmark returns true if current character is speechmark "
	 * 
	 * @param ch
	 * @return true or false
	 */
	public static boolean isSpeechMark(char ch) {
		if (ch == '"') {
			return true;
		} else
			return false;
	} //end method 
	
	/**
	 * method isNewLine returns true if current character is new line \n
	 * 
	 * @param ch
	 * @return true or false
	 */
	public static boolean isNewLine(char ch) {
		if (ch == '\n') {
			return true;
		} else
			return false;
	} //end method

} //end class Q5
