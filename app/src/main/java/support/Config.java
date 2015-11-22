/**
 * 
 */
package support;

import android.content.Context;

import com.jsloves.election.activity.R;

import support.io.GlideImageLoader;

/**
 * @desc 
 *  아지톡 라이브러리의 모든 모듈을 설정. 
 *
 * @author
 * @date
 *
 */
public class Config {
	public static boolean isDebugable;
	/************************************************
	 * 
	 * 라이브러리 기본 설정 
	 * 
	 ************************************************/
	public static void setConfig(boolean isDebug)
	{
		isDebugable = isDebug;
	}
	
	
	/************************************************
	 * 
	 * 네트워크 설정 
	 * 
	 ************************************************/
	public static final int REQUEST_TYPE_VOLLEY = 0;
	public static final int REQUEST_TYPE_AQUERY = 1;
//	public static final int REQUEST_TYPE_ASYNCTASK_HTTP= 2;
//	public static final int REQUEST_TYPE_MELON = 3;
//
//	
	public static int DEFAULT_REQUEST_TYPE = REQUEST_TYPE_VOLLEY;
	
	public static final int NETWORK_TIMEOUT_MS = 10 * 1000;
	
	public static final String RESPONSE_RESULT = "result";		// ex {resultCode:0, resultMessage:"suc", data:{}}
	public static final String RESPONSE_RESULT_ERROR_MSG = "error";
	public static final String RESPONSE_RESULT_ERROR_CODE = "errorcode";		// ex {resultCode:0, resultMessage:"suc", data:{}}
	public static final String RESPONSE_BODY_DATA_KEY = "data";		// ex {resultCode:0, resultMessage:"suc", data:{}}
	
	public static void setNetworkConfig()
	{
		
	}
	
	
	/*************************************************
	 * 
	 * 이미지 로더 설정 
	 * 
	 *************************************************/
	public static int IMAGE_DEFAULT_PLACEHOLDER_ERROR_IMAGE = R.drawable.sample1;
	
	
	public static void setImageConfig(Context context, int defaultPlaceHolderRes)
	{
		GlideImageLoader.setGlideOption(context);
		IMAGE_DEFAULT_PLACEHOLDER_ERROR_IMAGE = defaultPlaceHolderRes;
	}
	
	

	
}
