package io.ucs.common.exception;

/**
 * @author Macrow
 * @date 2022/06/10
 */
public class NotAuthorizationException extends RuntimeException {
    private static final String MESSAGE = "用户权限不足";

    public NotAuthorizationException() {
        super(MESSAGE);
    }

    public NotAuthorizationException(String message) {
        super(message);
    }
}
