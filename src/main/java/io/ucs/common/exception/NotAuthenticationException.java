package io.ucs.common.exception;

/**
 * @author Macrow
 * @date 2022/06/10
 */
public class NotAuthenticationException extends RuntimeException {
    private static final String MESSAGE = "需要登录访问资源";

    public NotAuthenticationException() {
        super(MESSAGE);
    }

    public NotAuthenticationException(String message) {
        super(message);
    }
}
