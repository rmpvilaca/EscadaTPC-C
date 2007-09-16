package escada.tpc.common.clients.jmx;

public class InvalidTransactionException extends Exception {
	private static final long serialVersionUID = 7820151305504399249L;

	public InvalidTransactionException() {
	}

	public InvalidTransactionException(String message) {
		super(message);
	}

	public InvalidTransactionException(Throwable cause) {
		super(cause);
	}

	public InvalidTransactionException(String message, Throwable cause) {
		super(message, cause);
	}
}
