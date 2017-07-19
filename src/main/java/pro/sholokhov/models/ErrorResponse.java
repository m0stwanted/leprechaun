package pro.sholokhov.models;

import pro.sholokhov.handlers.ErrorHandler;

public class ErrorResponse {

    private Integer httpStatus;
    private String cause;

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(ErrorHandler.HttpErrorCode httpStatus) {
        this.httpStatus = httpStatus.getCode();
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

}
