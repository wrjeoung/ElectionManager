package support.io;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Map;

import support.io.UploaderService.UploadStateCallback;

/**
 * 업로드를 관리하는 매니저 클래스 
 * 업로드 서비스 시작, 업로드 상황 인터페이스 등을 포함한다.
 * @author LOEN
 *
 */
public class UploaderManager {
	private UploaderService mUploaderService;
	private boolean isSerivce;
	private UploaderParam mCurrentUploadParam;
	private UploadStateCallback mUploadStateCallback;
	private Context mContext;
	private Handler.Callback mCallback;
	
	private static UploaderManager mInstance;
	
	/***
	 * 하나의 업로더 및 서비스 관리를 위해 싱글톤으로 작성
	 * @return
	 */
	public static UploaderManager getInstance(Context context)
	{
		if(mInstance == null)
		{
			mInstance = new UploaderManager(context);
		}
		
		return mInstance;
	}
	
	private UploaderManager(Context context)
	{
		mContext = context;

	}
	
	private ServiceConnection mUploadConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d("cvrt" ,"onServiceDisconnected");
			mUploaderService.stopSelf();
			mUploaderService = null;	
			isSerivce = false;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d("cvrt", "onServiceConnected");
			if(mCurrentUploadParam == null) return;
			mUploaderService = ((UploaderService.UploaderServiceBinder)service).getService();
			mUploaderService.setCallback(mServiceCallback);

			mUploaderService.upload(mCurrentUploadParam);
			isSerivce = true;

		}
		
		
	};
	
	public void setUploadStateCallback(UploadStateCallback uploadCallback)
	{
		mUploadStateCallback = uploadCallback;
	}
	
	
	public void onUpload(UploaderParam uploadParams)
	{ 
		mCurrentUploadParam = uploadParams;

		mServiceCallback.onUploadState(UploadStateCallback.STATE_PREPARE, null);
        Intent uploadService = new Intent(mContext, UploaderService.class);

		if(mUploaderService == null)
		{

			mContext.bindService(uploadService, mUploadConnection, Context.BIND_AUTO_CREATE);
		}else
		{
			// 이미 업로드 중일때, 다중 업로드면 해당 코드를 지우고 다중업로드에 맞는 코드를 작성한다.
            if(mUploaderService.isUploading())
            {
                mUploaderService.abort();
            }
            mUploaderService.upload(mCurrentUploadParam);
		}
		
	}
	
	public void abortUpload(){
		if( !isSerivce || mUploaderService == null) return;
		
		mUploaderService.abort();
		onComplete();
	}
	
	private UploadStateCallback mServiceCallback = new UploadStateCallback() {
		
		@Override
		public void onUploadState(final int state, final String args) {
			if(mContext instanceof Activity)
				
				((Activity)mContext).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(mUploadStateCallback != null) mUploadStateCallback.onUploadState(state, args);
					}
				});
			
			if(state == UploadStateCallback.STATE_COMPLETE)
			{
				onComplete();
			}
			
		}
	};
	
	public void onComplete()
	{
		mUploaderService.stopSelf();
	}

	public class UploaderParam{
        public String url;
        public Map<String, String> header;
        public Map<String, String> param;
        public String[] filePaths;
        public String fileParamKey;
        
		public UploaderParam(String url,Map<String, String> header, Map<String, String> param, String[] filePaths, String fileParamKey) {
			super();
			this.url = url;
			this.param = param;
			this.filePaths = filePaths;
			this.fileParamKey = fileParamKey;
			this.header = header;
		}
        
        
	}

}
