package io.ucs.common.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Macrow
 * @date 2022/06/10
 */
public class Result<T> extends ResponseEntity<RawResult<T>> {
    public static final String MessageSuccess = "成功";
    public static final String MessageUnAuthorized = "你无权访问该页面";
    public static final String MessageTokenFormatError = "令牌格式错误";
    public static final String MessageTokenExpired = "令牌失效";
    public static final String MessageNotFound = "您访问的页面不存在";
    public static final String MessageInternalServerError = "服务内部错误";
    public static final String MessageParamInvalid = "参数错误";
    public static final String MessageOperationNotPermit = "操作不被允许";
    public static final String MessageContactAdmin = "内部错误，请联系管理员";

    public static final int CodeSuccess = 0;
    public static final int CodeError = 1;
    public static final int CodeUnAuthentication = 11;
    public static final int CodeUnAuthorization = 12;
    public static final int CodeNotFound = 13;
    public static final int CodeParamInvalid = 14;
    public static final int CodeOperationNotPermit = 15;
    public static final int CodeTimeout = 401;
    public static final int CodeInternalError = 500;

    public Result(RawResult<T> body) {
        super(body, HttpStatus.OK);
    }

    public static <T> Result<T> success(T result, String message) {
        return Result.generateResult(CodeSuccess, message, result);
    }

    public static <T> Result<T> success(String message) {
        return Result.generateResult(CodeSuccess, message, null);
    }

    public static <T> Result<T> successWithResult(T result) {
        return Result.generateResult(CodeSuccess, MessageSuccess, result);
    }

    public static <T> Result<T> success(T result) {
        return Result.generateResult(CodeSuccess, MessageSuccess, result);
    }

    public static <T> Result<T> success() {
        return Result.generateResult(CodeSuccess, MessageSuccess, null);
    }

    public static <T> Result<T> failed(String message) {
        return Result.generateResult(CodeError, message, null);
    }

    public static <T> Result<T> failed(T result, String message) {
        return Result.generateResult(CodeError, message, result);
    }

    public static <T> Result<T> failed() {
        return Result.generateResult(CodeError, MessageInternalServerError, null);
    }

    public static <T> Result<T> failed(int code) {
        return Result.generateResult(code, MessageInternalServerError, null);
    }

    public static <T> Result<T> failed(int code, T result) {
        return Result.generateResult(code, MessageInternalServerError, result);
    }

    public static <T> Result<T> failed(int code, T result, String message) {
        return Result.generateResult(code, message, result);
    }

    protected static <T> Result<T> generateResult(int code, String message, T result) {
        return new Result<>(
                RawResult.<T>builder()
                        .code(code)
                        .message(message)
                        .result(result)
                        .build()
        );
    }
}
