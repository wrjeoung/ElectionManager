package com.jsloves.election.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import support.BaseActivity;
import support.widget.NImageView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Nam on 15. 7. 22..
 */
public class PhotoViewDialogActivity extends BaseActivity {

    public final static String PARAM_IMAGE_URL = "param_image_url";

    private Context mContext;
    private String mImageUrl;

    NImageView mImage;
    PhotoViewAttacher mAttacher;

    @Override
    public int onGetContentViewResource() {
        mActivityType = ACTIVITY_DIALOG;
        return R.layout.activity_photo_dialog;
    }

    @Override
    public void onInit() {

        mContext = this;
        getWindow().getAttributes().width = mResolutionutils.convertWidth(680);
        getWindow().getAttributes().height = mResolutionutils.convertHeight(1024);

        setTitle("Photo");

        handleIntent(getIntent());

        buildComponents();

        showLoadingDlg(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingDlg();
                mAttacher = new PhotoViewAttacher(mImage);
                mAttacher.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                mAttacher.update();
            }
        }, 2000);
    }

    private void handleIntent(Intent getIntent) {
        if (getIntent != null) {
            mImageUrl = getIntent.getStringExtra(PARAM_IMAGE_URL);
        }
    }

    private void buildComponents() {
        mImage = (NImageView) findViewById(R.id.img_photo);

        if(!TextUtils.isEmpty(mImageUrl)){
//            mImage.setImageUrl(mImageUrl);
            Glide.with(mContext)
                    .load(mImageUrl)
                    .asBitmap()
                    .into(new BitmapImageViewTarget(mImage){
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                        }
                    });

        }

        Button btnClose = (Button) findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnRight = (Button) findViewById(R.id.btn_right);
        btnRight.setVisibility(View.GONE);
//        btnRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAttacher.setRotationTo(90f);
//            }
//        });
    }

    public static void callActivity(Context context, String imageUrl) {
        Intent intent = new Intent(context, PhotoViewDialogActivity.class);
        intent.putExtra(PARAM_IMAGE_URL, imageUrl);

        context.startActivity(intent);
    }
}
