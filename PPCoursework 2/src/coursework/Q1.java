package coursework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import coursework.TokenType;

public class Q1 {

	public static void main(String[] args) {

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
		else if (ch == '\n') {
			return true;
		} // new line
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
	} // end method

} // end Q1