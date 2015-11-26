package support.io.toolbox;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import support.Config;
import support.io.BaseRequest;
import support.io.NetworkError;
import support.io.RequestError;
import support.io.model.BaseInterface;
import support.util.NetworkUtil;

/**
 * @author
 * @desc
 * @date
 */
public class SsokVolleyRequest extends VolleyRequest {
    private boolean mIsDecoding = false;

    public SsokVolleyRequest(Class<? extends BaseInterface> apiClass) {
        super(apiClass);
    }

    public SsokVolleyRequest(BaseInterface api) {
        super(api);
    }

    public SsokVolleyRequest(String url, Class jsonTargetCls, Map<String, Object> params, Map<String, String> header) {
        super(url, jsonTargetCls, params, header);
    }

    /**
     * @desc
     * @see
     */
    @Override
    public void request(Context context, BaseRequest.OnRequestCallback cb) {
        int networkEnable = NetworkUtil.checkNetworkStatus(context);
        if (networkEnable != NetworkUtil.NETWORK_STATUS_NONE && networkEnable != NetworkUtil.NETWORK_STATUS_CONNECTED) {
            mCallback = cb;
            NetworkError error = new NetworkError(NetworkError.ERROR_NETWORK_CONNECT, networkEnable, "데이터망 연결 확인");
            sendError(error);
            return;
        }


        super.request(context, cb);

        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("====================Request====================")
                .append("\n==url : " + mRequestUrl)
                .append("\n==header : " + api.getHeader())
                .append("\n==param : " + api.getParam())
                .append("\n======================================================");
        Log.e("nam", logBuilder.toString());

    }

    public void request(Context context, BaseRequest.OnRequestCallback cb, boolean isDecoding) {
        mIsDecoding = isDecoding;
        request(context, cb);
    }


    /**
     * VolleyRequest Override Method *
     */
    protected Response.Listener<String> onSuccessListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseDataForResult(response);
            }
        };
    }

    /**
     * VolleyRequest Override Method *
     */
    protected Response.ErrorListener onErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkError nerror = new NetworkError(error);
                sendError(nerror);

            }
        };
    }

//	/** AqueryRequest Override Method **/
//	public synchronized void onResult(final String url, final String jsonString, final AjaxStatus status) {
//		super.onResult(url, jsonString, status);
//		if (status.getCode() != 200) {
//			sendError(new NetworkError(NetworkError.ERROR_NETWORK, status.getCode(), status.getMessage()));
//			return;
//		}
//		
//		parseDataForResult(jsonString);
//	}

    private void parseDataForResult(String response) {
        Log.d("nam", "Response : " + response);

        Gson gson = new Gson();

//        int resultCode = -1;
        String data = "";
//        String resultMessage = "";
        String result = "";
        String errorMsg = "";
        String errorCode = "";
        try {
            JSONObject responseObject = new JSONObject(response);

            result = responseObject.getString(Config.RESPONSE_RESULT);
            errorMsg = responseObject.getString(Config.RESPONSE_RESULT_ERROR_MSG);
            errorCode = responseObject.getString(Config.RESPONSE_RESULT_ERROR_CODE);
            data = responseObject.getString(Config.RESPONSE_BODY_DATA_KEY);

        } catch (JSONException e) {
//            if (resultCode == -1 && resultMessage.length() < 1) {
//				NetworkError error = new NetworkError(new RequestError(resultCode, "api 응답구조가 틀리다??"));
//				sendError(error);
//            }
            data = response;
            // return;
        }

        if (mRequestUrl == null)
            mRequestUrl = api.getUrl();

//        support.io.model.Response newResponse = new support.io.model.Response(mRequestUrl, resultCode, resultMessage, response.toString());
        support.io.model.Response newResponse = new support.io.model.Response(mRequestUrl, result, errorCode, errorMsg, response.toString());
        newResponse.setCookies(mSetCookie);

        Object responseBody = null;
        String errorMessage = "";

//        if (mIsDecoding) {
//            String encodedString = null;
//
//            try {
//                encodedString = URLDecoder.decode(data, "UTF-8");
//                encodedString = URLDecoder.decode(encodedString, "UTF-8");
//                encodedString = encodedString.replaceAll("\"\\{", "\\{");
//                encodedString = encodedString.replaceAll("\\}\"", "\\}");
//
//                Log.d("nam", "edcodeString : " + encodedString);
//
//                mIsDecoding = false;
//
//                if (api.getResponseBody() != null)
//                    responseBody = gson.fromJson(encodedString, api.getResponseBody());
//                else {
//                    NetworkError error = new NetworkError(new RequestError(0, "API에 getResponseBody가 선언 되어있지 않습니다. "));
//                    sendError(error);
//                    return;
//                }
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//                mIsDecoding = false;
//                return;
//            } catch (Exception e) {
//                errorMessage = ", " + e.getMessage();
//            }
//        } else {

            try {
                if (api.getResponseBody() != null) {
                    responseBody = gson.fromJson(data, api.getResponseBody());
                } else {
                    NetworkError error = new NetworkError(new RequestError(0, "API에 getResponseBody가 선언 되어있지 않습니다. "));
                    sendError(error);
                    return;
                }
            } catch (Exception e) {
                errorMessage = ", " + e.getMessage();
            }
//        }


        if (responseBody == null) {
            NetworkError error = new NetworkError(new RequestError(0, "Response Data를 파싱 할 수 없습니다." + errorMessage));
            sendError(error);
            return;
        }


        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("====================Response====================")
                .append("\n==api url : " + api.getUrl())
                .append("\n==result : " + result)
                .append("\n==error code : " + errorCode)
                .append("\n==error Message : " + errorMsg)
                .append("\n==response : " + response)
                .append("\n======================================================");
        Log.e("nam", logBuilder.toString());

        if (!newResponse.getResult().equals("success")) {
            sendError(new NetworkError(NetworkError.ERROR_DATA_REQUEST, newResponse.getErrorCode(), newResponse.getErrorMsg()));
            return;
        }

        if (mCallback != null) {
            mCallback.onResult(newResponse, responseBody);
        }
    }

    public void sendError(NetworkError error) {

        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("=================Response Error=================")
                .append("\n==api url : " + api.getUrl())
                .append("\n==error type : " + error.getErrorType())
                .append("\n==error code : " + error.getStatusCode())
                .append("\n==error msg : " + error.getMessage())
                .append("\n======================================================");
        Log.e("nam", logBuilder.toString());
        error.setRequestContext(mContext);

        if (mCallback != null) {
            error.setRequestInterface(api);
            mCallback.onError(error);
        }
    }
}
