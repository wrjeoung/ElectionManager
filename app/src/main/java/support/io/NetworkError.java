package support.io;

import android.content.Context;

import com.android.volley.VolleyError;

import support.io.model.BaseInterface;

/**
 * @desc 
 * Volley 기반으로 한 네트워크 에러 처리 클래스 
 * @author
 * @date
 *
 */
public class NetworkError extends Exception {
    private static final long serialVersionUID = 5672141457952027408L;
    
    /**
     * 네트워크 에러 타입
     * 추후 각 Exception으로 분기 
     */
    public static final int ERROR_NETWORK = 0;
    public static final int ERROR_DATA_REQUEST = 1;
    public static final int ERROR_NETWORK_CONNECT = 2;
    
    
    private int mErrorType;				//에러 타입 
    
    private int mStatusCode;				//에러 코드
    private String mMessage;				//에러 메시지 
    
    private BaseInterface mRequestInterface;
    
    private String mRequestUrl;
    private String mRequestParam;

    private Context mRequestContext;

    public NetworkError(VolleyError error)
    {
    	super(error);
    	
    	if(error.networkResponse != null)
    	{
        	mStatusCode = error.networkResponse.statusCode;
        	if(error.networkResponse.data != null)
        		mMessage = new String(error.networkResponse.data);
        	else
        		mMessage = error +"";
    	}else
    	{
    		mMessage = error.getMessage();
    	}

        mErrorType = ERROR_NETWORK;
    }

    public NetworkError(RequestError error)
    {
    	super(error);
        mErrorType = ERROR_DATA_REQUEST;
        mStatusCode = error.getResultCode();
        mMessage = error.getResultMessage();
    }
    
    public NetworkError(int errortype, int code, String msg)
    {
    	super();
        mErrorType = errortype;
        mStatusCode = code;
        mMessage = msg;
    }
    

    
    public int getStatusCode()
    {
    	return mStatusCode;
    }
    
	public int getErrorType() {
		return mErrorType;
	}

	public String getMessage()
	{
		return mMessage;
	}
	
	public void setRequestInterface(BaseInterface api)
	{
		mRequestInterface = api;
	}
	
	public BaseInterface getRequestInterface()
	{
		return mRequestInterface;
	}

	public Context getRequestContext() {
		return mRequestContext;
	}

	public void setRequestContext(Context mRequestContext) {
		this.mRequestContext = mRequestContext;
	}
	

    

}
