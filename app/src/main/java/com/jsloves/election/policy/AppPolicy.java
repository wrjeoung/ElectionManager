package com.jsloves.election.policy;

import android.content.Intent;

public class AppPolicy {
	//테스트 서버 / 상용서버 구분 처리 플래그
	public static boolean isRealServer											= false;
	public static int mIsSecureEnable												= 0;// 0:disable , 1:enable

	private static final String HTTP_TYPE 										= (mIsSecureEnable == 1) ? "https://" : "http://";

	private static final String ROOT_PATH 										= "ElectionManager_server";
	//테스트 서버
	private static final String TEST_URI_HOST 									= "222.122.149.161";
	private static final String TEST_PORT 										= "7070";
	public static final String URI_TEST_SERVER									= HTTP_TYPE + TEST_URI_HOST + ":"+TEST_PORT+"/"+ROOT_PATH;
	//상용 서버
	private static final String REAL_URI_HOST 									= "222.122.149.161";
	private static final String REAL_PORT 										= "7070";
	public static final String URI_REAL_SERVER									= HTTP_TYPE + REAL_URI_HOST + ":"+REAL_PORT+"/"+ROOT_PATH;

	//서버 주소 세팅
	public static final String URI_SERVER										= isRealServer ? URI_REAL_SERVER : URI_TEST_SERVER;
}
