package data;

import pipeline.FileManager;
import pipeline.StatementData;

/**
 * Look, I know this is terrible. I'm trying to learn Rust so my mind is there
 * right now. This is used solely in one place within the Interpreter. Pretty
 * much like a Rust enum that lets me store either of the potential values from
 * a function depending on if we evaluated a singleton or non-singleton.
 * 
 */
public class Outcome {
	/**
	 * Determines whether we have a manager. NEEDS to be checked to determine if
	 * there is a manager or statement data to retrieve.
	 */
	public final boolean hasManager;
	/**
	 * Result of the outcome. Could either be manager or statement data.
	 */
	private final Object result;

	/**
	 * Make an Outcome with a FileManager.
	 * 
	 * @param manager the value stored in the outcome
	 */
	public Outcome(FileManager<VarType> manager) {
		this.result = manager;
		hasManager = true;
	}

	/**
	 * Make an Outcome with StatementData.
	 * 
	 * @param data the value stored in the outcome
	 */
	public Outcome(StatementData data) {
		this.result = data;
		hasManager = false;
	}

	/**
	 * Gets the file manager. If a file manager is NOT stored, horrors will ensue.
	 * 
	 * @return the stored file manager, or garbage otherwise
	 */
	public FileManager<VarType> getManager() {
		return (FileManager<VarType>) result;
	}

	/**
	 * Gets the statement data. If statement data is NOT stored, horrors will ensue.
	 * 
	 * @return the stored statement data, or garbage otherwise.
	 */
	public StatementData getStatementData() {
		return (StatementData) result;
	}
}