package io.ucs.common.exception;

/**
 * @author Macrow
 * @date 2022/06/10
 */
public class NormalException extends RuntimeException {
    private static final String MESSAGE = "错误";

    public NormalException() {
        super(MESSAGE);
    }

    public NormalException(String message) {
        super(message);
    }
}
