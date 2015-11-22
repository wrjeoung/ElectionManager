package support.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.RequestManager.ImageModelRequest;
import com.bumptech.glide.integration.volley.VolleyUrlLoader;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import support.Config;

/**
 * @desc
 * 
 *
 * @author
 * @date
 *
 */
public class GlideImageLoader {
	private final static int FADE_IN_RESOURCE = android.R.anim.fade_in;

	private static final ModelCache<String, GlideUrl> mUrlCache = new ModelCache<String, GlideUrl>(150);

	private final ImageModelRequest<String> mGlideModelRequest;
	private final CenterCrop mCenterCrop;
	private int mPlaceHolderResId;

	public GlideImageLoader(Context context) {
		ResizeImageUrlLoader imageLoader = new ResizeImageUrlLoader(context);
		mGlideModelRequest = Glide.with(context).using(imageLoader);
		mCenterCrop = new CenterCrop(Glide.get(context).getBitmapPool());
		mPlaceHolderResId = -1;

	}
	
	public void setPlaceHolderResId(int resId)
	{
		mPlaceHolderResId = resId;
	}

	public void loadImage(String url, ImageView imageView, RequestListener<String, Bitmap> requestListener, Drawable placeholderOverride, boolean crop) {
		BitmapRequestBuilder request = mGlideModelRequest.load(url).asBitmap().listener(requestListener).transform(mCenterCrop).animate(FADE_IN_RESOURCE);
		if (placeholderOverride != null) {
			request.placeholder(placeholderOverride);
		} else if (mPlaceHolderResId != -1) {
			request.placeholder(mPlaceHolderResId);
		}
		request.into(imageView);
	}
	
	public void loadImage(String url, ImageView imageView, RequestListener<String, Bitmap> requestListener, int placeholderOverrideId, boolean crop) {
		BitmapRequestBuilder request = mGlideModelRequest.load(url).asBitmap().listener(requestListener).transform(mCenterCrop).animate(FADE_IN_RESOURCE);
		if (placeholderOverrideId != -1) {
			request.placeholder(placeholderOverrideId);

		}
		request.into(imageView);
	}

	public void loadImageGif(String url, ImageView imageView, RequestListener<String, GlideDrawable> requestListener, Drawable placeholderOverride, boolean crop) {
		DrawableRequestBuilder<String> request = mGlideModelRequest.load(url).listener(requestListener).transform(mCenterCrop).diskCacheStrategy(DiskCacheStrategy.NONE).animate(FADE_IN_RESOURCE);
		if (placeholderOverride != null) {
			request.placeholder(placeholderOverride);
		} else if (mPlaceHolderResId != -1) {
			request.placeholder(mPlaceHolderResId);
		}
		request.into(imageView);
	}

	public static void loadImage(Context context, String url, ImageView imageView) {
		loadImage(context, url, imageView, Config.IMAGE_DEFAULT_PLACEHOLDER_ERROR_IMAGE, true, null);
	}

	/**
	 * Glide 를 이용한 이미지 다운로드
	 * 
	 */
	public static void loadImage(Context context, String url, ImageView imageView, int placeholderRes, boolean isFadeIn, RequestListener<String, Bitmap> requestListener) {

		if (url == null) {
			url = "";
		}

		if (isFadeIn) {
			Animation fadeInAnimation = AnimationUtils.loadAnimation(context, FADE_IN_RESOURCE);

			Glide.with(context).from(String.class).asBitmap().error(placeholderRes).animate(fadeInAnimation).load(url).listener(requestListener).into(imageView);
		} else {
			Glide.with(context).from(String.class).asBitmap().error(placeholderRes).load(url).listener(requestListener).into(imageView);
		}
	}

	public static void loadImageGif(Context context, String imageUrl, final ImageView imageView) {
		loadImageGif(context, imageUrl, imageView, Config.IMAGE_DEFAULT_PLACEHOLDER_ERROR_IMAGE, null, null);
	}

	public static void loadImageGif(Context context, String imageUrl, final ImageView imageView, int errorRes, RequestListener<String, GlideDrawable> requestListener, RequestListener<String, GlideDrawable> thumbnailListener) {
		if (imageUrl == null) {
			imageUrl = "";
		}

		if (thumbnailListener == null) {
			Glide.with(context).from(String.class).load(imageUrl).error(errorRes).listener(requestListener).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
		} else {
			Glide.with(context).from(String.class).load(imageUrl).error(errorRes).listener(requestListener).diskCacheStrategy(DiskCacheStrategy.NONE).thumbnail(Glide.with(context).load(imageUrl).listener(thumbnailListener)).into(imageView);
		}
	}

	public static Bitmap getBitmapFromURL(String src) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(src);

			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();

			InputStream input = connection.getInputStream();

			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null)
				connection.disconnect();
		}
	}

	private static class ResizeImageUrlLoader extends BaseGlideUrlLoader<String> {

		public ResizeImageUrlLoader(Context context) {
			super(context, mUrlCache);
		}

		@Override
		protected String getUrl(String model, int width, int height) {

			return model;
		}
	}

	/**
	 * Glide option 설정
	 * 
	 * @param context
	 */
	public static void setGlideOption(Context context) {

		RequestQueue requestQueue = Volley.newRequestQueue(context);

		Glide.get(context).register(GlideUrl.class, InputStream.class, new VolleyUrlLoader.Factory(requestQueue));
		Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL);

	}

	/**
	 * Glide 메모리 정리
	 * 
	 * @param context
	 */
	public static void clearGlideMemory(Context context) {
		Glide.get(context).clearMemory();
	}

	/**
	 * Glide Trim 메모리 정리
	 * 
	 * @param context
	 * @param level
	 */
	public static void TrimGlideMemory(Context context, int level) {
		Glide.get(context).trimMemory(level);
	}

	public static class CircleTransform extends BitmapTransformation {
		public CircleTransform(Context context) {
			super(context);
		}

		@Override
		protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
			return circleCrop(pool, toTransform);
		}

		private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
			if (source == null)
				return null;

			int size = Math.min(source.getWidth(), source.getHeight());
			int x = (source.getWidth() - size) / 2;
			int y = (source.getHeight() - size) / 2;

			// TODO this could be acquired from the pool too
			Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

			Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
			if (result == null) {
				result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
			}

			Canvas canvas = new Canvas(result);
			Paint paint = new Paint();
			paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
			paint.setAntiAlias(true);
			float r = size / 2f;
			canvas.drawCircle(r, r, r, paint);
			return result;
		}

		@Override
		public String getId() {
			return getClass().getName();
		}
	}

}
