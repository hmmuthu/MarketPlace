public class Request {
    private Operation operation;

    private String email;
    private String payload;

    public Request(Operation operation, String email, String payload) {
        this.operation = operation;
        this.email = email;
        this.payload = payload;
    }

    public Request(Operation operation) {
        this.operation = operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
