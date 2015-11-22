package support.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 화면 픽셀 대응 유틸
 * 픽셀 단위로 계산 (Default 1280 * 720)
 */
public class ResolutionUtils {
	private Context mContext;
	private DisplayMetrics dm;
	
	private static final float DEFAULT_WIDTH = 720.0F;
	private static final float DEFAULT_HEIGHT = 1280.0F;
	
    private static final float DEFAULT_SCREEN_RATIO = 1.6667f;
    private static float DEVIATION_RATION = 0.07f;      //편차

	private int mWidth;
	private int mHeight;
	Display mDisplay;
	public ResolutionUtils(Context context){
		this.mContext = context;
		if(mContext instanceof Activity)
		    mDisplay = ((Activity) mContext).getWindowManager().getDefaultDisplay();
		
		dm = new DisplayMetrics();
		if(mContext instanceof Activity)
		    ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		mHeight = dm.heightPixels;
		mWidth = context.getResources().getDisplayMetrics().widthPixels;
		mHeight = context.getResources().getDisplayMetrics().heightPixels;
	}

	public DisplayMetrics getDisplayMetrics(){
		return dm;
	}
	
	public int convertHeight(float pixel) {
		float ratio = mHeight / DEFAULT_HEIGHT;
		return (int)(pixel * ratio);
	}
	
	public int convertWidth(float pixel) {
		float ratio = mWidth / DEFAULT_WIDTH;
		return (int)(pixel * ratio);
	}

	public float convertFontPixel(float pixel) {
		float fontPixel;
		float fontRatio = mHeight / DEFAULT_HEIGHT;	// 세로를 기준으로 늘어나야할 비율
		float tmpRatio = mHeight / (mWidth / 1.0f);
		float pixRatio = pixel * fontRatio;
		try {
			float deviceScreenRatio = Float.parseFloat(String.format("%.4f", tmpRatio));
			float gapRatio = Math.abs(DEFAULT_SCREEN_RATIO - deviceScreenRatio);
			
			if (deviceScreenRatio > DEFAULT_SCREEN_RATIO) {
				deviceScreenRatio = deviceScreenRatio / DEFAULT_SCREEN_RATIO;
				//fontPixel = ((pixRatio / deviceScreenRatio) / dm.scaledDensity);
				fontPixel = (pixRatio / dm.scaledDensity);
			} else if (deviceScreenRatio < DEFAULT_SCREEN_RATIO) {
				if(gapRatio > DEVIATION_RATION) {
					fontPixel = ((pixRatio / 1.2f) / dm.scaledDensity);
				} else {
					fontPixel = (pixRatio / dm.scaledDensity);
				}
			    fontPixel = (pixRatio / dm.scaledDensity);
			} else {
				fontPixel = (pixRatio / dm.scaledDensity);
			}
		} catch (Exception e) {
			fontPixel = (pixRatio / dm.scaledDensity);
		}
		return fontPixel;
	}
	
	public float scaledFontPixel(float pixel) {
		float yScale;
		if (Build.VERSION.SDK_INT <= 10) {
			yScale = mHeight / (float) DEFAULT_HEIGHT;
		} else {
			Point size = new Point();
			if (mDisplay != null) {
			    mDisplay.getSize(size);
			}
			yScale = (float) size.y / (float) DEFAULT_HEIGHT;
		}

//		Qlog.e("", "yScale: " + yScale);
		return pixel * yScale;
	}
	
	public float convertPixelHeight(float pixel) {
		return pixel / (DEFAULT_HEIGHT / dm.heightPixels);
	}

	public int convertPixelHeight(int pixel) {
		return (int) (pixel / (DEFAULT_HEIGHT / dm.heightPixels));
	}
	
	public int getDisplayWidth(){		
		return dm.widthPixels;
	}
	
	public int getOrientation(){
		Display ds = ((Activity)mContext).getWindowManager().getDefaultDisplay();
		return ds.getOrientation();
	}
	
	public boolean textViewSizeLonger(TextView textView, int percent){
		DisplayMetrics dm = getDisplayMetrics();
		int widthForText = (dm.widthPixels * percent) / 100;
		
		if(getRealLengthOfText(textView) < widthForText){
			return false;
		}
		
		return true;
	}

	public TextView adjustTitleTextSize(TextView textView, int percent){
		DisplayMetrics dm = getDisplayMetrics();
		int widthForText = (dm.widthPixels * percent) / 100;
		
		textView.setTextSize(convertFontPixel(20.0f));
		
		if(getRealLengthOfText(textView) < widthForText){
			return textView;
		}
		
		int songTextSize = (int)textView.getTextSize();
		
		while(getRealLengthOfText(textView) > widthForText){
			textView.setTextSize(--songTextSize);
			int size = (int)(textView.getTextSize() / dm.scaledDensity / 1.1);
			textView.setTextSize(size);
		}
		
		return textView;
	}
	
	private int getRealLengthOfText(TextView textview) {
		Paint paint = textview.getPaint();
		String str1 = textview.getText().toString();

		float[] widths = new float[str1.length()];
		int nLength = paint.getTextWidths(str1, 0, str1.length(), widths);
		int realLength = 0;

		for (int i = 0;i < widths.length;i++) {
			realLength += widths[i];
		}
		return realLength;
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
        for (int i = 0;i < listAdapter.getCount();i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        if(params.height < 0)
            params.height = 0;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
