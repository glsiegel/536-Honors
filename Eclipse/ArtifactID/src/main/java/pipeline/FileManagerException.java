package pipeline;

/**
 * Exception for FileManager when something horrible happens.
 */
public class FileManagerException extends RuntimeException {

	public FileManagerException(String msg) {
		super(msg);
	}

}
