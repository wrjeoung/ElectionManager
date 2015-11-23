package memo.multipart;

import android.text.TextUtils;
import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.UnsupportedEncodingException;

public enum Multipart {

    INSTANCE;

    private String USER_SEQ = "addressSeq";
    private String CUST_NAME = "custName";
    private String CUST_PHONE = "custPhone";
    private String MAIN_ADDRESS = "mainAddress";
    private String SUB_ADDRESS = "subAddress";
    private String TAG = "tag";
    private String IMAGE0 = "attachment1";
    private String IMAGE1 = "attachment2";
    private String IMAGE2 = "attachment3";
    private String IMAGE3 = "attachment4";
    private String IMAGE4 = "attachment5";
    private String GROUP_SEQ = "groupSeq";
    private String IMAGE_YN = "imgYn";
    private String MEMO_CONTENTS = "contents";
    private String MAIN_IMAGE = "mainImg";
    private String GROUPLIST = "groupList";
    private String IMAGE_URL = "imgUrl";
    private String MEMO_SEQ = "memoSeq";
    private String ADM_CD = "ADM_CD";

    public MultipartEntity defaultMultipartBuilder() {
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        return multipart;
    }
    public MultipartEntity getMemoAddMultipartBuilder(String memoSeq, String admCd, String tag, String imgYn, String memo, String attachmentPath, String imgUrl) {
        MultipartEntity multipart = defaultMultipartBuilder();

        try{
            if(!TextUtils.isEmpty(memoSeq))
                multipart.addPart(MEMO_SEQ, new StringBody(memoSeq));

            if(!TextUtils.isEmpty(ADM_CD))
                multipart.addPart(ADM_CD, new StringBody(admCd));

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
