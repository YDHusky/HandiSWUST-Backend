package org.shirakawatyu.handixikebackend.exception;

/**
 * @author ShirakawaTyu
 */
public class OutOfCreditException extends RuntimeException {
    public OutOfCreditException() {
    }

    public OutOfCreditException(String message) {
        super(message);
    }

    public OutOfCreditException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfCreditException(Throwable cause) {
        super(cause);
    }
}
