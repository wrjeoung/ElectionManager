package com.jsloves.election.net;

import com.jsloves.election.policy.AppPolicy;

/**
 * Created by jhkim01 on 2015-12-18.
 */
public class RestApiProvider {
    public static final String API_SERVER_URL			= AppPolicy.URI_SERVER;

    public static final String API_MEMO_URL				= API_SERVER_URL+"/MemoServlet";
    public static final String API_COMMON_URL			= API_SERVER_URL+"/MobileReq.jsp";
}
