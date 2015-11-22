package com.jsloves.election.net;

import java.util.HashMap;
import java.util.Map;

import support.io.model.ResponseBody;

/**
 * Created by Nam on 15. 7. 22..
 * 사용하지 않음.
 */
public class DefaultApi extends BaseApi {

    @Override
    public Map<String, Object> makeParam() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        return params;
    }

    @Override
    public Class<? extends ResponseBody> getResponseBody() {
        return DefaultBody.class;
    }

    @Override
    public String getPath() {
        return "/login.jsp";
    }
}
