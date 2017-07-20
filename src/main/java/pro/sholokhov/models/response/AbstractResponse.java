package pro.sholokhov.models.response;

public abstract class AbstractResponse {

    private Boolean success;
    private String message;

    public AbstractResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
