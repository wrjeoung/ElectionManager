package memo.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.entity.mime.MultipartEntity;

import java.util.ArrayList;

import memo.multipart.Multipart;
import memo.multipart.MultipartResponse;
import memo.multipart.RequestMultiApi;

public class MemoUpdateApi extends AsyncTask<Void, Void, MultipartResponse> {

    public interface InsertMemoListener {
        public void onReceiver(MultipartResponse response);
    }

    private Context mContext;

    private String mUrl;
    private String mMemo;
    private String mAdmCd;
    private String mTag;
    private String mImgYn;
    private ArrayList<String> mPhotoList;
    private String mImageUrl;
    private String mMemoSeq;
    private String mAttachmentPath;

    private InsertMemoListener mMemoListener;

    public MemoUpdateApi(Context context, String memoSeq, String admCd, String memo, String tag, String attachmentPath, String imageUrl, InsertMemoListener listener) {
        mContext = context;
        mMemoListener = listener;

        mUrl = "http://222.122.149.161:7070/ElectionManager_server/MemoServlet?Type=Update";
        //mUrl = "http://10.11.1.164:8080/ElectionManager_server/MemoServlet?Type=Update";
        mMemo = memo;
        mTag = tag;
        mAdmCd = admCd;
        mAttachmentPath = attachmentPath;
        mImageUrl = imageUrl;
        mMemoSeq = memoSeq;

//        LoginBody.LoginUserInfo loginInfo = NLoginManager.getLoginInfo().userInfo.get(0);
//        mMobileInfo = loginInfo.mobileInfo;
        if(mAttachmentPath.isEmpty() && imageUrl.isEmpty())
            mImgYn = "N";
        else
            mImgYn = "Y";
    }

    @Override
    protected MultipartResponse doInBackground(Void... params) {
        MultipartResponse responseEntry = new MultipartResponse();
        try {
            MultipartEntity builder = Multipart.INSTANCE.getMemoAddMultipartBuilder(mMemoSeq, "", mTag, mImgYn, mMemo, mAttachmentPath, mImageUrl);
            Log.e("nam", mImageUrl + " /// path : " + mAttachmentPath);
            responseEntry = RequestMultiApi.requesetMultipartApiCall(mContext, mUrl, builder);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return responseEntry;
    }
    @Override
    protected void onPostExecute(MultipartResponse result) {
        super.onPostExecute(result);

        if(mMemoListener != null)
            mMemoListener.onReceiver(result);

        Log.d("TAG", "IMAGEURL :: 완료!!!!!!!!!!!!");
    }
}
