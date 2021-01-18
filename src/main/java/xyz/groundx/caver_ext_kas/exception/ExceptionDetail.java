package xyz.groundx.caver_ext_kas.exception;

public class ExceptionDetail {
    int code;
    String message = "";
    String requestId = "";

    public ExceptionDetail() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestId() {
        return requestId;
    }
}
