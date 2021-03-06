package memo.net;

import java.util.HashMap;
import java.util.Map;

import support.io.model.ResponseBody;

/**
 * Created by Suock on 2015-07-29.
 */
public class MemoDeleteApi extends BaseApi {

    public String memoSeq;

    public MemoDeleteApi(String memoSeq) {
        this.memoSeq = memoSeq;
    }

    @Override
    public Map<String, Object> makeParam() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("Type","Del");
        params.put("MemoSeq", memoSeq);

        return params;
    }

    @Override
    public Class<? extends ResponseBody> getResponseBody() {
        return DefaultBody.class;
    }

    @Override
    public String getPath() {
        return "/MemoServlet";
    }
}
