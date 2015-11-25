package memo.net;

/**
 * Created by Nam on 15. 7. 22..
 */
/**
 *
 */

import com.android.volley.Request.Method;

import java.util.HashMap;
import java.util.Map;

import support.io.model.BaseInterface;

public abstract class BaseApi extends BaseInterface {

    public final static String URL_HOST = "http://222.122.149.161";
    public final static String PORT = "7070";

    /*
    public final static String URL_HOST = "http://10.11.1.164";
    public final static String PORT = "8080";
    */

    public final static int PAGE_DEFAULT_SIZE = 15;
    public final static int PAGE_START_INDEX = 1;

    public BaseApi() {

    }

    @Override
    public abstract Map<String, Object> makeParam();

    @Override
    public String getUrl() {
        return URL_HOST + ":" + PORT + getPath();
    }

    public abstract String getPath();

    @Override
    public final Map<String, String> makeHeader() {

        return getNHeader();
    }

    public static Map<String, String> getNHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
//        header.put("Accept-Encoding", "gzip, deflate");
//        header.put("Accept-Encoding", "");
        header.put("Accept-Language", "ko-KR; en-US");
        header.put("Accept-Charset", "utf-8");
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

        return header;
    }

    @Override
    public int getMethod() {
        return Method.GET;
    }
}
