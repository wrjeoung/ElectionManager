package support;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jsloves.election.activity.R;


public class SsokLoadingDialog extends Dialog {
    private Context mContext;

    public SsokLoadingDialog(Context context) {
        super(context, R.style.AppTheme_TranslucentDialog);

        mContext = context;

        init(context);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void init(Context context) {
        setCanceledOnTouchOutside(false);
        SsokProgressView progressView = new SsokProgressView(context);

        addContentView(progressView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
