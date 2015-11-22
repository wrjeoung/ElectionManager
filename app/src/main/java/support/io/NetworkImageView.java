package support.io;

/**
 * @desc 
 * 네트워크 이미지 뷰 
 * 이미지뷰를 상속 받아 네트워크 Url를 처리하는 이미지뷰 
 * @author
 * @date
 *
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class NetworkImageView extends ImageView{
	protected String mUrl;
	protected int mDefaultImageId;

	protected boolean isFadein;
	protected String mCatchDir;
	protected long mCatchSize;
	
	public NetworkImageView(Context context) {
		super(context);
		initalize();
	}
	
	public NetworkImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initalize();
	}
	
	public NetworkImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initalize();
	}
	
	private void initalize()
	{
		
	}
	
	
	public void setImageUrl(String url)
	{
		mUrl = url;
	}
	
}
