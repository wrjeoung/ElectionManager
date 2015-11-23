package memo.multipart;

import android.content.Context;
import android.text.Html;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class RequestMultiApi {
    public static final String RESULT_SUCCESS = "success";
    public static final String RESULT_FAIL = "fail";

    public static MultipartResponse requesetMultipartApiCall(Context context, String url, MultipartEntity multipart) {
        MultipartResponse multipartResponse = null;
//        HttpEntity httpEntity = multipart.build();
        String resultContents = "";


//        HttpPost post = new HttpPost(url);
//        post.setEntity(httpEntity);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(multipart);


//        HttpClient client = AndroidHttpClient.newInstance("Android");
//        client.getConnectionManager().closeIdleConnections(1, TimeUnit.MINUTES);


        try {
//            HttpResponse httpResponse = client.execute(post);
            HttpResponse httpResponse = httpClient.execute(httpPost);

            multipartResponse = new MultipartResponse();

            String result = EntityUtils.toString(httpResponse.getEntity()).trim();
            resultContents = Html.fromHtml(result).toString();

            Log.e("nam", resultContents);

        } catch (UnsupportedEncodingException e) {
            multipartResponse.setResult(RESULT_FAIL);
            e.printStackTrace();
            return null;
        } catch (ClientProtocolException e1) {
            multipartResponse.setResult(RESULT_FAIL);
            e1.printStackTrace();
            return null;
        } catch (IOException e1) {
            multipartResponse.setResult(RESULT_FAIL);
            e1.printStackTrace();
            return null;
        } catch (ParseException e) {
            multipartResponse.setResult(RESULT_FAIL);
            e.printStackTrace();
            return null;
        }

        try {
            resultContents = resultContents.replace("{", "");
            resultContents = resultContents.replace("}", "");
            String[] contentArray = resultContents.split("error");
            String resultcomplete = contentArray[0];
            if(resultcomplete.contains(RESULT_SUCCESS)) {
                resultcomplete = RESULT_SUCCESS;
            } else {
                resultcomplete = RESULT_FAIL;
            }

            String error = "";
            String[] errorArray  = contentArray[1].split("errorcode");
            error = errorArray[0].replace(":", "");
            if(error.length() > 2) {
                error = error.substring(1, error.length() - 1);
            } else {
                error = "";
            }

            String errorCode = contentArray[2].split(":")[1];
            if(errorCode.length() > 2) {
                errorCode = errorCode.substring(1, errorCode.length() - 1);
            } else {
                errorCode = "-1";
            }


            if(null != errorCode && !errorCode.isEmpty()) {
                multipartResponse.setErrorCode(Integer.parseInt(errorCode));
            } else {
                multipartResponse.setErrorCode(-1);
            }

            if(null != error && !error.isEmpty()) {
                multipartResponse.setError(error);
            } else {
                multipartResponse.setError("");
            }

            if(null != resultcomplete && !resultcomplete.isEmpty()) {
                multipartResponse.setResult(resultcomplete);
            } else {
                multipartResponse.setResult(resultcomplete);
            }

            Log.e("nam", "=========================================");
            Log.e("nam", "result : " + resultcomplete);
            Log.e("nam", "error : " + error);
            Log.e("nam", "errorCode : " + errorCode);
            Log.e("nam", "=========================================");

        } catch (Exception e) {
            multipartResponse.setResult(RESULT_FAIL);
            Log.e("nam", "=========================================");
            Log.e("nam", "multipart Exception");
            Log.e("nam", "=========================================");
        }
        return multipartResponse;
    }
}
