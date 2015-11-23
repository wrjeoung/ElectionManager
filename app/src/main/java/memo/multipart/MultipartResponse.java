package memo.multipart;

public class MultipartResponse {
    public String error;
    public String result;
    public int code;

    public int getErrorCode() {
        return code;
    }

    public void setErrorCode(int code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
