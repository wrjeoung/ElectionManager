package support.io;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @date
 */

public class MultiPartRequestHttp {
	public static interface MultipartProgressListener {
		void transferred(long transfered, int progress);
	}

	private Map<String, String> mTextBody;
	private Map<String, File> mFileBody;
	private String mFileArrayParamKey;
	private ArrayList<File> mFileArrayBody;
	private final Response.Listener<String> mListener;
	private final Response.ErrorListener mErrorListener;
	private Map<String, String> headerParams;
	private final MultipartProgressListener mPrgoressListener;

	private final String mUrl;
	private long fileLength = 0L;
	
	private HttpPost mPostRequest;

	public MultiPartRequestHttp(String url, Response.Listener<String> l) {
		this(url, null, l, null, null);
	}

	public MultiPartRequestHttp(String url, Response.ErrorListener errorListener, Response.Listener<String> listener, Map<String, String> mStringPart, MultipartProgressListener progLitener) {

		mUrl = url;
		mListener = listener;
		mErrorListener = errorListener;

		mTextBody = new HashMap<String, String>();
		if (mStringPart != null)
			mTextBody.putAll(mStringPart);

		mFileBody = new HashMap<String, File>();

		headerParams = new HashMap<String, String>();

		mPrgoressListener = progLitener;
		mFileArrayParamKey = null;

	}

	public void addFileUpload(String param, File file) {
		this.fileLength += file.length();
		mFileBody.put(param, file);
	}

	public void addFileUpladArray(String paramKey, ArrayList<File> files, long allFileLength) {
		this.fileLength = allFileLength;
		mFileArrayParamKey = paramKey;
		mFileArrayBody = files;
	}

	public void addStringUpload(String param, String content) {
		mTextBody.put(param, content);
	}

	public void addHeader(String param, String value) {
		headerParams.put(param, value);
	}

	public void request() {
		new Thread(new UploadThread()).start();
	}

	public class UploadThread implements Runnable {

		@Override
		public void run() {
			try {
				DefaultHttpClient m_httpClient = new DefaultHttpClient();
				if(mPostRequest != null)
				{
					mPostRequest.abort();
					mPostRequest = null;
				}
				mPostRequest = new HttpPost(mUrl);

				for (String key : headerParams.keySet()) {
					mPostRequest.addHeader(key, headerParams.get(key));
				}

				ProgressMultiPartEntity reqEntity = new ProgressMultiPartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));

				if (mFileBody.size() > 0) {
					for (String key : mFileBody.keySet()) {
						File file = mFileBody.get(key);
						ContentBody isb = new FileBody(file);

						reqEntity.addPart(key, isb);

					}
				}

				if (mFileArrayParamKey != null) {
					for (File file : mFileArrayBody) {
						ContentBody isb = new FileBody(file);
						reqEntity.addPart(mFileArrayParamKey, isb);

					}
				}

				for (String key : mTextBody.keySet()) {
					reqEntity.addPart(key, new StringBody(mTextBody.get(key)));

				}

				mPostRequest.setEntity(reqEntity);
				Log.d("cvrt", "postRequest : " + mPostRequest.getAllHeaders());
				Log.d("cvrt", "postRequest : " + mPostRequest.getHeaders("Cookie"));

				// Execute HTTP Post Request
				HttpResponse response = m_httpClient.execute(mPostRequest);
				InputStreamReader r;
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();
				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}

				JSONObject element = null;
				try {
					JSONObject jsonObject = new JSONObject(s.toString());
					mListener.onResponse(jsonObject.toString());

				} catch (JSONException e) {
					e.printStackTrace();
					if (mErrorListener != null)
						mErrorListener.onErrorResponse(new VolleyError(e.getMessage()));
					return;
				}

			} catch (Exception e) {
				e.printStackTrace();
				if (mErrorListener != null)
					mErrorListener.onErrorResponse(new VolleyError(e.getMessage()));
				return;
			}

		}
	}
	
	public void abort()
	{
		if(mPostRequest != null)
		{
			mPostRequest.abort();
			mPostRequest = null;
			
		}
	}

	public class ProgressMultiPartEntity extends MultipartEntity {

		public ProgressMultiPartEntity() {
			super();
		}

		public ProgressMultiPartEntity(final HttpMultipartMode mode) {
			super(mode);
		}

		public ProgressMultiPartEntity(HttpMultipartMode mode, final String boundary, final Charset charset) {
			super(mode, boundary, charset);
		}

		@Override
		public void writeTo(final OutputStream outstream) throws IOException {
			super.writeTo(new CountingOutputStream(outstream));
		}

	}

	public class CountingOutputStream extends FilterOutputStream {

		private long transferred;

		public CountingOutputStream(final OutputStream out) {
			super(out);
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			int prog = (int) (transferred * 100 / fileLength);
			if (mPrgoressListener != null)
				mPrgoressListener.transferred(this.transferred, prog);
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			int prog = (int) (transferred * 100 / fileLength);
			if (mPrgoressListener != null)
				mPrgoressListener.transferred(this.transferred, prog);
		}
	}
}
