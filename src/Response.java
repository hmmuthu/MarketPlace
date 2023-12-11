public class Response {
    private boolean success;
    private String payload;
    private String error;

    public Response(boolean success, String payload, String error){
        this.success = success;
        this.payload = payload;
        this.error = error;
    }

    public Response(boolean success, String payload){
        this.success = success;
        this.payload = payload;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
