package memo.multipart;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public enum Multipart {

    INSTANCE;

    private String TYPE = "Type";
    private String USER_SEQ = "addressSeq";
    private String CUST_NAME = "custName";
    private String CUST_PHONE = "custPhone";
    private String MAIN_ADDRESS = "mainAddress";
    private String SUB_ADDRESS = "subAddress";
    private String TAG = "tag";
    private String IMAGE0 = "Attachment1";
    private String IMAGE1 = "Attachment2";
    private String IMAGE2 = "Attachment3";
    private String IMAGE3 = "Attachment4";
    private String IMAGE4 = "Attachment5";
    private String GROUP_SEQ = "groupSeq";
    private String IMAGE_YN = "ImgYn";
    private String MEMO_CONTENTS = "Contents";
    private String MAIN_IMAGE = "mainImg";
    private String GROUPLIST = "groupList";
    private String IMAGE_URL = "ImgUrl";
    private String MEMO_SEQ = "MemoSeq";
    private String ADM_CD = "AdmCd";

    public MultipartEntity defaultMultipartBuilder() {
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        return multipart;
    }

    public MultipartEntity defaultMultipartBuilderUTF8() {
        MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName("UTF-8"));
        return multipart;
    }

    public MultipartEntity getMemoAddMultipartBuilder(String type, String memoSeq, String admCd, String tag, String imgYn, String memo, String attachmentPath, String imgUrl) {
        //MultipartEntity multipart = defaultMultipartBuilder();
        MultipartEntity multipart = defaultMultipartBuilderUTF8();

        try{
            if(!TextUtils.isEmpty(type))
                multipart.addPart(TYPE, new StringBody(URLEncoder.encode(type,HTTP.UTF_8)));

            if(!TextUtils.isEmpty(memoSeq))
                multipart.addPart(MEMO_SEQ, new StringBody(URLEncoder.encode(memoSeq,HTTP.UTF_8)));

            if(!TextUtils.isEmpty(admCd))
                multipart.addPart(ADM_CD, new StringBody(URLEncoder.encode(admCd,HTTP.UTF_8)));

            if(!TextUtils.isEmpty(tag))
                multipart.addPart(TAG, new StringBody(tag));

            if(!TextUtils.isEmpty(imgYn))
                multipart.addPart(IMAGE_YN, new StringBody(imgYn));

            if(!TextUtils.isEmpty(memo))
                multipart.addPart(MEMO_CONTENTS, new StringBody(memo));

            if(!TextUtils.isEmpty(imgUrl))
                multipart.addPart(IMAGE_URL, new StringBody(imgUrl));

//            if(photoList != null){
//                int nCount = 1;
//                for(String fileName : photoList) {
                    if(!TextUtils.isEmpty(attachmentPath)) {

                        File file = new File(attachmentPath);

                        if (file.exists()) {
                            Log.e("nam", "------------------------------");
                            Log.e("nam", "attach file : " + attachmentPath);
                            Log.e("nam", "------------------------------");
                            ContentBody fb = new FileBody(new File(attachmentPath));

                            multipart.addPart(IMAGE0, fb);
                        }
                    }
//                }
//            }

            Log.e("nam", "------------------------------");
            Log.e("nam", "type : " + type);
            Log.e("nam", "memoSeq : " + memoSeq);
            Log.e("nam", "admCd : " + admCd);
            Log.e("nam", "tag : " + tag);
            Log.e("nam", "imgYn : " + imgYn);
            Log.e("nam", "memo : " + memo);
            Log.e("nam", "attachmentPath : " + attachmentPath);
            Log.e("nam", "imgUrl : " + imgUrl);
            Log.e("nam", "------------------------------");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return multipart;
    }

    public MultipartEntity getMemoMultipartBuilder() {
        MultipartEntity multipart = defaultMultipartBuilder();

        return multipart;
    }

}
