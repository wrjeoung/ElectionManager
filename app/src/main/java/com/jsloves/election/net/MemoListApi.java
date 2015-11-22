package com.jsloves.election.net;

import com.jsloves.election.DTO.BoardListBody;

import java.util.HashMap;
import java.util.Map;

import support.io.model.ResponseBody;

/**
 * Created by juhyukkim on 2015. 11. 22..
 */
public class MemoListApi extends BaseApi {
    private String admCd;
    private int offset;

    public MemoListApi(String admCd, int offset) {
        this.admCd = admCd;
        this.offset = offset;
    }

    @Override
    public Map<String, Object> makeParam() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("TYPE","MEMOLIST");
        params.put("ADM_CD", admCd);
        params.put("OFFSET", offset);

        return params;
    }

    @Override
    public Class<? extends ResponseBody> getResponseBody() {
        return BoardListBody.class;
    }

    @Override
    public String getPath() {
        return "/ElectionManager_server/MobileReq.jsp";
    }
}
