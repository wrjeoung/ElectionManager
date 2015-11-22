package support.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsloves.election.activity.R;

public class PopupDialogAdapter extends BaseAdapter {

    private Context mContext = null;
    private CharSequence[] mCharSequence = null;

    public PopupDialogAdapter(Context oContext, CharSequence[] oCharSequence) {
        mContext = oContext;
        mCharSequence = oCharSequence;
    }

    @Override
    public int getCount() {
        return mCharSequence.length;
    }

    @Override
    public Object getItem(int arg0) {
        return mCharSequence[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View oView, ViewGroup oViewGroup) {
        if (oView == null) {
            oView = View.inflate(mContext, R.layout.popup_dialog_item, null);
        }

        LinearLayout oLinearLayout = (LinearLayout) oView
            .findViewById(R.id.layout_popup_dialog_item_container);
        TextView oTextView = (TextView) oView.findViewById(R.id.btn_popup_dialog_item_button);

        if (mCharSequence.length == 1) {
            oLinearLayout.setBackgroundResource(R.drawable.bg_btn_popup_more_btm);
        } else if (mCharSequence.length == 2) {
            if (position == 0) {
                oLinearLayout.setBackgroundResource(R.drawable.bg_btn_popup_more_top);
            } else {
                oLinearLayout.setBackgroundResource(R.drawable.bg_btn_popup_more_btm);
            }
        } else if (mCharSequence.length >= 3) {
            if (position == mCharSequence.length - 1) {
                oLinearLayout.setBackgroundResource(R.drawable.bg_btn_popup_more_btm);
            } else {
                oLinearLayout.setBackgroundResource(R.drawable.bg_btn_popup_more_top);
            }
        }

        oTextView.setText(mCharSequence[position]);

        return oView;
    }
}
