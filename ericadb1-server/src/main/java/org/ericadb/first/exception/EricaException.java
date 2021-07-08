package org.ericadb.first.exception;

/**
 * @author Jerry Will
 * @since 2021-07-06
 */
public class EricaException extends RuntimeException {

    public EricaException() {
        super();
    }

    public EricaException(String message) {
        super(message);
    }

    public EricaException(String message, Throwable cause) {
        super(message, cause);
    }

    public EricaException(Throwable cause) {
        super(cause);
    }

}
