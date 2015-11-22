package support;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsloves.election.activity.R;

import support.io.BaseRequest;
import support.io.model.BaseInterface;
import support.io.toolbox.SsokVolleyRequest;
import support.util.Builder;

public abstract class BaseFragment extends Fragment {

    private Activity mParentActivity;
    private SsokLoadingDialog mLoadingDialog = null;        // 로딩 Dialog

    // activity layout id (layou이 없을때는 0)
    public abstract int onGetContentViewResource();

    public abstract void onInit(View view);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        mParentActivity = getActivity();

        int nLayoutId = onGetContentViewResource();
        if (nLayoutId != 0)
            view = inflater.inflate(nLayoutId, container, false);

        onInit(view);
        return view;
    }

    public View currentView() {
        return getView();
    }

    /**
     * get loading dialog
     *
     * @param isCancel : 백버튼 활성화 여부
     * @return dialog
     */
    public SsokLoadingDialog getDialog(boolean isCancel) {
        if (this.mLoadingDialog == null) {
            mLoadingDialog = new SsokLoadingDialog(mParentActivity);
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
        if (!mParentActivity.isFinishing()) {
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
            this.mLoadingDialog.dismiss();
            this.mLoadingDialog = null;
        }
    }

    public void errorNoti(final Context context, final String msg) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Builder(context).setIcon(R.drawable.icon_alert)
                        .setMessage(msg)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
//                                mParentActivity.finish();
                            }
                        }).show();
            }
        }, 100);
    }

    public void requestApi(BaseInterface api, BaseRequest.OnRequestCallback cb){
        SsokVolleyRequest request = new SsokVolleyRequest(api);
        request.request(mParentActivity, cb);
    }
}