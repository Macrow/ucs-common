package io.ucs.common.exception;

/**
 * @author Macrow
 * @date 2022/06/10
 */
public class NotFoundException extends RuntimeException {
    private static final String MESSAGE = "未找到对应资源";

    public NotFoundException() {
        super(MESSAGE);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
