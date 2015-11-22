package support.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import support.io.GlideImageLoader;
import support.io.NetworkImageView;

/**
 * @desc
 * 
 * @author
 * @date
 *
 */
public class GlideImageView extends NetworkImageView {

	private GlideImageLoader mImageLoader;

	public GlideImageView(Context context) {
		super(context);
		initalize();
	}

	public GlideImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initalize();
	}

	public GlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initalize();
	}

	private void initalize() {
		mImageLoader = new GlideImageLoader(getContext());
	}

	/**
	 * @desc
	 * @see
	 */
	@Override
	public void setImageUrl(String url) {
		setImageUrl(url, null);

	}

	public void setImageUrl(String url,final Drawable placeholder) {
		super.setImageUrl(url);
		
		if(placeholder != null)
			setImageDrawable(placeholder);

		mImageLoader.loadImage(mUrl, this, new RequestListener<String, Bitmap>() {

			@Override
			public boolean onException(Exception arg0, String arg1, Target<Bitmap> arg2, boolean arg3) {
				GlideImageView.this.setImageDrawable(placeholder);
				return false;
			}

			@Override
			public boolean onResourceReady(Bitmap arg0, String arg1, Target<Bitmap> arg2, boolean arg3, boolean arg4) {
				return false;
			}
		}, null, true);
	}

	/**
	 * @desc
	 * @see
	 */
	public void setImageGifUrl(String url) {
		super.setImageUrl(url);
		mImageLoader.loadImageGif(mUrl, this, new RequestListener<String, GlideDrawable>() {

			@Override
			public boolean onException(Exception arg0, String arg1, Target<GlideDrawable> arg2, boolean arg3) {
				return false;
			}

			@Override
			public boolean onResourceReady(GlideDrawable arg0, String arg1, Target<GlideDrawable> arg2, boolean arg3, boolean arg4) {
				return false;
			}
		}, null, true);

	}
}
