package support;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jsloves.election.activity.R;

import support.util.ResolutionUtils;

public class SsokProgressView extends LinearLayout {

    private Context mContext;
    private ResolutionUtils mResolutionUtil;
    private AnimationDrawable mAnimationDrawable;
    private ImageView mProgressImage;

    public SsokProgressView(Context context) {
        super(context);
        this.mContext = context;

        init();
    }

    public SsokProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResolutionUtil = new ResolutionUtils(mContext);
        View view = inflater.inflate(R.layout.widget_progress_view, this);
        mProgressImage = (ImageView) findViewById(R.id.progress_img);
        view.setLayoutParams(new LayoutParams(mResolutionUtil.convertWidth(83.75f), mResolutionUtil.convertHeight(86.25f)));

        mProgressImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimationDrawable = (AnimationDrawable) mProgressImage.getDrawable();
                mAnimationDrawable.start();
            }
        }, 50);
    }
}
