package support.io.toolbox;

import android.content.Context;

import java.util.Map;

import support.io.BaseRequest;
import support.io.model.BaseInterface;

/**
 * @desc 
 * 
 *
 * @author
 * @date
 *
 */
public class SsokRequest extends BaseRequest {

	public SsokRequest(BaseInterface api) {
		super(api);
	}

	/** 
	 * @desc 
	 * @see
	 */
	@Override
	protected void request(Context context, OnRequestCallback cb, int method, String Url, Map<String, Object> params, Map<String, String> header) {
	}

	/** 
	 * @desc 
	 * @see
	 */
	@Override
	public void cancel(Context context) {
	}

}
