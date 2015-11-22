package support.io;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import java.io.File;
import java.util.ArrayList;

import support.io.MultiPartRequestHttp.MultipartProgressListener;

/**
 * 업로드 수행 서비스
 * 업로드 시에만 서비스가 활성 되도록 한다.
 * @author
 *
 */
public class UploaderService extends Service{
	private final IBinder mBinder = new UploaderServiceBinder();
	private UploadStateCallback mCallback;
    private boolean isUploading;
    
	public class UploaderServiceBinder extends Binder{
		UploaderService getService()
		{
			return UploaderService.this;
		}
	}
	
	public interface UploadStateCallback{
		public static final int STATE_PREPARE	= 0;
		public static final int STATE_START 	= 1;
		public static final int STATE_PROGRESS	= 2;
		public static final int STATE_SUCCESS	= 3;
		public static final int STATE_ERROR		= 4;
		public static final int STATE_COMPLETE	= 5;
		public void onUploadState(int state, String args);
	}
	
	public void setCallback(UploadStateCallback callback)
	{
		mCallback = callback;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

    @Override
    public void onCreate() {
        super.onCreate();
        isUploading = false;
    }
    public boolean isUploading()
    {
        return isUploading;
    }

    public void upload(final UploaderManager.UploaderParam option)
	{
        MultiPartRequestHttp request  = new MultiPartRequestHttp(option.url, new ErrorListener() {


			@Override
			public void onErrorResponse(VolleyError error) {
				if(mCallback != null) 
				{
                    error.printStackTrace();
                    mCallback.onUploadState(UploadStateCallback.STATE_ERROR, error.getMessage());
                    mCallback.onUploadState(UploadStateCallback.STATE_COMPLETE, "");
                }
				
			}
		}, new Listener<String>() {

			@Override
			public void onResponse(String response) {
                Log.d("cvrt","response:"+response);
				if(mCallback != null) 
				{
                    mCallback.onUploadState(UploadStateCallback.STATE_SUCCESS, response);
                    mCallback.onUploadState(UploadStateCallback.STATE_COMPLETE, "");
				}

                isUploading = false;
			}
		}
		, null, new MultipartProgressListener() {
			
			@Override
			public void transferred(long transfered, int progress) {
				if(mCallback != null) {
//                    mCallback.onUploadState(UploadStateCallback.STATE_PROGRESS, new Gson().toJson(option.data));
                    mCallback.onUploadState(UploadStateCallback.STATE_PROGRESS, progress+"");
                }
				
			}
		});

        for(String key: option.param.keySet())
        {
            request.addStringUpload(key, option.param.get(key));

        }


        ArrayList<File> fileList = new ArrayList<File>();
        long fileLength = 0L;
        for(int i = 0 ; i < option.filePaths.length ; i++)
		{
			File file = new File(option.filePaths[i]);
            fileLength += file.length();
            if(file != null && file.exists())
                fileList.add(file);
		}

        if(fileList.size() > 0)
            request.addFileUpladArray(option.fileParamKey, fileList,fileLength);



        request.request();

        isUploading = true;
		
		if(mCallback != null) 
			mCallback.onUploadState(UploadStateCallback.STATE_START, "");
	}
	
	public void abort()
	{

	}
	

}
