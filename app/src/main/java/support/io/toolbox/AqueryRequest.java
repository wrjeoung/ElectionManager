package support.io.toolbox;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.apache.http.cookie.Cookie;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Map;

import support.Config;
import support.io.BaseRequest;
import support.io.model.BaseInterface;

/**
 * @desc
 * 
 *
 * @author
 * @date
 *
 */
public class AqueryRequest extends BaseRequest {
	private AjaxCallback<String> mAjaxCallback;

	public AqueryRequest(Class<? extends BaseInterface> apiClass) {
		super(apiClass);
	}

	public AqueryRequest(BaseInterface api) {
		super(api);
	}

	public AqueryRequest(String url, Class jsonTargetCls, Map<String, Object> params, Map<String, String> header) {
		super(url, jsonTargetCls, params, header);
	}

	@Override
	protected void request(Context context, OnRequestCallback cb, int method, String Url, Map<String, Object> params, Map<String, String> header) {
        AbstractAjaxCallback.setNetworkLimit(8);
        AbstractAjaxCallback.setTimeout(Config.NETWORK_TIMEOUT_MS);
        
		mRequestUrl = Url;

		mAjaxCallback = new AjaxCallback<String>();
		if (method == RequestMethod.GET) {
			if (params != null) {
				StringBuilder paramStr = new StringBuilder("?");
				for (String key : params.keySet()) {
					if (params.get(key) != null) {
						paramStr.append(key + "=" + params.get(key));
						paramStr.append("&");
					}
				}
				paramStr.deleteCharAt(paramStr.length() - 1);

				String getParams = paramStr.toString();

				mRequestUrl = mRequestUrl + getParams;
			}
		}
		mAjaxCallback.url(mRequestUrl).type(String.class);

		mAjaxCallback.headers(api.getHeader());
		mAjaxCallback.cookies(api.getCookie());

		if (method == RequestMethod.POST)
			mAjaxCallback.params(params);

		mAjaxCallback.weakHandler(this, "onResult");

		AQuery oAQuery = new AQuery(context);
		oAQuery.ajax(mAjaxCallback);

		
//		TEST
//		AjaxCallback<String> oAjaxCallback = new AjaxCallback<String>();
//		oAjaxCallback.url("https://aztalk.melon.com:4557/aztalk/login/web/login_login.json").type(String.class);
//		oAjaxCallback.weakHandler(this, "getTest");
//
//		
//		oAjaxCallback.setAgent("AS43; Android 4.4.2; 2.0.0; IM-A890S");
//		oAjaxCallback.header("Accept-Encoding", "");
//		oAjaxCallback.header("Accept-Language", "ko-KR; en-US");
//		oAjaxCallback.header("Accept-Charset", "utf-8");   
//		 
//		
//		Map<String, Object> params1 = new HashMap<String, Object>();
//		String _psw = "asdfg1";
//		
//		params1.put("cpid", "AS43");
//		params1.put("cpkey", "13LOM1");
//		params1.put("id", "azt0006");
//		params1.put("loginType", 2);
//
//        params1.put("pwd", _psw);
//        params.put("menuId", 2000020101); // 직접 로그인
//
//        params1.put("token", null);
//
//		oAjaxCallback.params(params1);
//		oAjaxCallback.weakHandler(this, "onResult");
//		
//		AQuery oAQuery = new AQuery(context);
//		oAQuery.ajax(oAjaxCallback);

	}

	public synchronized void onResult(final String url, final String jsonString, final AjaxStatus status) {

		if(mSetCookie == null) mSetCookie = new ArrayList<HttpCookie>();
		for(Cookie cookie : status.getCookies())
		{
			
			HttpCookie data = new HttpCookie(cookie.getName(), cookie.getValue());
			data.setComment(cookie.getComment());
			data.setCommentURL(cookie.getCommentURL());
			data.setDomain(cookie.getDomain());
			data.setPath(cookie.getPath());
			mSetCookie.add(data);
		}
	}

	/**
	 * @desc
	 * @see
	 */
	@Override
	public void cancel(Context context) {
		if (mAjaxCallback != null)
			mAjaxCallback.cancel();

	}

}
