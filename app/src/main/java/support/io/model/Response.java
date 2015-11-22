package support.io.model;

import android.text.TextUtils;

import java.net.HttpCookie;
import java.util.List;

/**
 * @author
 * @desc
 * @date
 */
public class Response {

    private String mUrl;
//	  private final int mStatus;
//	  private final String mMessage;
	private String mResult;
    private String mErrorCode;
    private String mErrorMsg;
    private String mBody;
    private List<HttpCookie> mCookie;

    public Response(String url, String result, String error, String errorMsg, String body){
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        mUrl = url;
        mBody = body;
        mResult = result;
        mErrorCode = error;
        mErrorMsg = errorMsg;
    }

//    public Response(String url, int status, String message, String body) {
//        if (url == null) {
//            throw new IllegalArgumentException("url == null");
//        }
////	    if (status < 200) {
////	      throw new IllegalArgumentException("Invalid status code: " + status);
////	    }
//        if (message == null) {
//            throw new IllegalArgumentException("reason == null");
//        }
//
//        mUrl = url;
////        mStatus = status;
////        mMessage = message;
//        mBody = body;
//    }

    public String getUrl() {
        return mUrl;
    }

//    /**
//     * @desc resultCode
//     */
//    public int getStatus() {
//        return mStatus;
//    }
//
//    /**
//     * @desc resultMessage
//     */
//    public String getMessage() {
//        return mMessage;
//    }

    public String getResult(){
        return mResult;
    }

    public int getErrorCode(){
        if(!TextUtils.isEmpty(mErrorCode))
            return Integer.parseInt(mErrorCode);
        else
            return 0;
    }

    public String getErrorMsg(){
        return mErrorMsg;
    }

    /**
     * Response body. May be {@code null}.
     */
    public String getBody() {
        return mBody;
    }

    public List<HttpCookie> getCookies() {
        return mCookie;
    }

    public void setCookies(List<HttpCookie> cookie) {
        this.mCookie = cookie;
    }


//	private T mBody;
//	
//	public void setBody(ResponseBody body)
//	{
//		mBody = (T)body;
//	}
//	
//	public <T> T getBody()
//	{
//		return (T) mBody;
//	}
//	
//	public int getResultCode()
//	{
//		if(mBody == null) return -1;
//		
//		return mBody.resultCode;
//	}
//	
//	public String getResultMessage()
//	{
//		if(mBody == null) return null;
//		
//		return mBody.resultString;
//
//	}
}
