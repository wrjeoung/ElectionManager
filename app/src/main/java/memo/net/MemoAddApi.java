package memo.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.entity.mime.MultipartEntity;

import java.util.ArrayList;

import memo.multipart.Multipart;
import memo.multipart.MultipartResponse;
import memo.multipart.RequestMultiApi;

public class MemoAddApi extends AsyncTask<Void, Void, MultipartResponse> {

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
    private String mAttachmentPath;

    private InsertMemoListener mMemoListener;

    public MemoAddApi(Context context,String memo, String admCd, String tag, String attachmentPath, InsertMemoListener listener) {
        mContext = context;
        mMemoListener = listener;

        //mUrl = "http://10.11.1.164:8080/ElectionManager_server/MemoServlet?Type=Add";
        mUrl = "http://222.122.149.161:7070/ElectionManager_server/MemoServlet?Type=Add";
        mMemo = memo;
        mAdmCd = admCd;
        mTag = tag;
        mAttachmentPath = attachmentPath;

        if(attachmentPath.isEmpty())
            mImgYn = "N";
        else
            mImgYn = "Y";
    }

    @Override
    protected MultipartResponse doInBackground(Void... params) {
        MultipartResponse responseEntry = new MultipartResponse();
        try {
            MultipartEntity builder = Multipart.INSTANCE.getMemoAddMultipartBuilder("", mAdmCd, mTag, mImgYn, mMemo, mAttachmentPath,  "");
            Log.e("nam", mTag + " " + mImgYn + " " + mMemo);
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
