package data;

/**
 * Represents a single token produced by the lexer.
 * 
 */
public final class Token {
	public enum Type {
		IDENTIFIER, NUMBER, TYPE,

		LPAREN, RPAREN, DO, END,

		IF, ELSE, WHILE, ASSIGN,

		PLUS, MINUS, TIMES, QUOTIENT,

		AND, OR,

		GREATER, GREATER_OR_EQUAL, LESS, LESS_OR_EQUAL, EQUALS_EQUALS, NOT_EQUALS, NOT,

		TRUE, FALSE,

		FUNCTION, PRINT, RETURN,

		SEMICOLON, COMMA,

		IMPORT, AS, DOT,

		EOF,

	}

	/**
	 * Text of this token.
	 */
	public final String lexeme;
	/**
	 * Type of this token.
	 */
	public final Token.Type tokenType;
	/**
	 * Line number of the token. 0-indexed.
	 */
	public final int lineNum;
	
	/**
	 * Char offset of the token within the line. 0-indexed.
	 */
	public final int charNum;

	public Token(String lexeme, Token.Type type, int lineNum, int charNum) {
		this.lexeme = lexeme;
		this.tokenType = type;
		this.lineNum = lineNum;
		this.charNum = charNum;
	}

	@Override
	public String toString() {
		return String.format("\"%s\" (%s @ l%d o%d)", lexeme, tokenType, lineNum + 1, charNum);
	}
	
	/**
	 * More clinical representation of a token, in case the default toString is obscure.
	 * @return clinical string representation
	 */
	public String toLongString() {
		return String.format("Token with type %s and value \"%s\", line %d offset %d.", tokenType, lexeme, lineNum + 1,
				charNum);
	}
}
