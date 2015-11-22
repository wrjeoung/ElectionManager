package support.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import support.Config;
import support.util.ResolutionUtils;

/**
 * @desc 
 * 
 *
 * @author
 * @date
 *
 */
public class NImageView extends GlideImageView{
	private ResolutionUtils mResoulutionUtils;

	public NImageView(Context context) {
		super(context);

		mResoulutionUtils = new ResolutionUtils(context);
	}
	
	public NImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mResoulutionUtils = new ResolutionUtils(context);
	}
	
	public NImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		mResoulutionUtils = new ResolutionUtils(context);
	}
	
	/** 
	 * @desc 
	 * @see
	 */
	@Override
	public void setImageUrl(String url) {
		setImageResource(Config.IMAGE_DEFAULT_PLACEHOLDER_ERROR_IMAGE);
		super.setImageUrl(url);
	}

	public void setImagePath(String path){
		int targetW = getWidth(); // ImageView 의 가로 사이즈 구하기
		int targetH = getHeight(); // ImageView 의 세로 사이즈 구하기

		if(targetW == 0)
			targetW = mResoulutionUtils.convertWidth(200);
		if(targetH == 0)
			targetH = mResoulutionUtils.convertHeight(200);

		// Bitmap 정보를 가져온다.
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmOptions);
		int photoW = bmOptions.outWidth; // 사진의 가로 사이즈 구하기
		int photoH = bmOptions.outHeight; // 사진의 세로 사이즈 구하기

		// 사진을 줄이기 위한 비율 구하기
		int scaleFactor = Math.min( photoW/targetW, photoH/targetH);

		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
		setImageBitmap(bitmap);
	}
}
