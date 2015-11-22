package support;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.jsloves.election.activity.R;

import support.io.BaseRequest;
import support.io.model.BaseInterface;
import support.io.toolbox.SsokVolleyRequest;
import support.util.Builder;
import support.util.ResolutionUtils;

public abstract class BaseActivity extends AppCompatActivity {
    public static String ACTIVITY_DEFAULT = "activity_default";
    public static String ACTIVITY_DIALOG = "activity_dialog";

    private SsokLoadingDialog mLoadingDialog = null;        // 로딩 Dialog

    // activity layout id (layou이 없을때는 0)
    public abstract int onGetContentViewResource();

    // onCreat대신 사용
    public abstract void onInit();

    protected String mActivityType = ACTIVITY_DEFAULT;      // activity type : onGetContentViewresource()에서 설정
    protected ResolutionUtils mResolutionutils;             // siz관련 util

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResolutionutils = new ResolutionUtils(this);

        int layoutId = onGetContentViewResource();

        if (layoutId != 0)
            setContentView(layoutId);

        onInit();
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mActivityType.equals(ACTIVITY_DEFAULT))
            overridePendingTransition(R.anim.start_enter, R.anim.start_exit);

        super.setContentView(layoutResID);
    }

    @Override
    public void finish() {
        super.finish();

        if (mActivityType.equals(ACTIVITY_DEFAULT))
            overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }

    /**
     * get loading dialog
     *
     * @param isCancel : 백버튼 활성화 여부
     * @return dialog
     */
    public SsokLoadingDialog getDialog(boolean isCancel) {
        if (this.mLoadingDialog == null) {
            mLoadingDialog = new SsokLoadingDialog(this);
            mLoadingDialog.setCancelable(isCancel);
        }
        return mLoadingDialog;
    }

    /**
     * show loading dialog
     *
     * @param isCancel : 백버튼 활성화 여부
     */
    protected void showLoadingDlg(boolean isCancel) {
        if (!this.isFinishing()) {
            if (this.mLoadingDialog == null) {
                mLoadingDialog = getDialog(isCancel);
            }
            this.mLoadingDialog.show();
        }

    }

    /**
     * hide loading dialog
     */
    protected void hideLoadingDlg() {
        if (this.mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void errorNoti(final Context context, final String msg, final boolean isFinish) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Builder(context).setIcon(R.drawable.icon_alert)
                        .setMessage(msg)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(isFinish)
                                    finish();
                            }
                        }).show();
            }
        }, 100);
    }

    public void requestApi(BaseInterface api, BaseRequest.OnRequestCallback cb) {
        SsokVolleyRequest request = new SsokVolleyRequest(api);
        request.request(this, cb);
    }
}