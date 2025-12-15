package parser;

public class ParserSoftException extends RuntimeException {
	public enum Variant {
		SKIP_LINE, SKIP_TO_BRACKET, SKIP_TO_PAREN
	}

	/**
	 * The variant of the exception. Determines whether the recovery option is to
	 * skip the line (to next semicolon), skip to paren depth, or skip to bracket
	 * depth.
	 */
	public final Variant variant;
	/**
	 * The target depth to reach, if we are skipping to bracket or paren.
	 */
	public final int depth;
	/**
	 * If set to true, then if we're skipping to a bracket or paren, we will first
	 * require the number of parens/brackets to CHANGE. This means that, if an error
	 * is called wanting to get to depth n, but the error is called with current
	 * depth n, then the depth has to change OFF of n before it can hope to stop
	 * skipping tokens.
	 */
	public final boolean requireEdge;

	public ParserSoftException() {
		super();
		variant = Variant.SKIP_LINE;
		depth = 0;
		requireEdge = false; // irrelevant for skipping lines
	}

	public ParserSoftException(Variant var, int depth) {
		super();
		this.variant = var;
		this.depth = depth;
		requireEdge = true; // setting this to true until i find a scenario where it's not useful
	}
	
	@Override
	public String toString() {
		return String.format("PSE (%s, %d)", variant, depth);
	}
}
