package support.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsloves.election.activity.R;

/**
 * Custom alert dialog
 * 
 */
public class Builder {
    /**
     * 느낌표 아이콘
     */
    public final static int ICON_INFO = R.drawable.icon_alert;

    private final Context mContext;
    private ResolutionUtils mResolutionUtils;
    private OnClickListener mPositiveButtonlistener;
    private OnClickListener mNegativeButtonlistener;
    private CharSequence mTitle;
    private CharSequence mMessage;
    private View mView;
    private OnClickListener mOnItemClickListener;
    private CharSequence mPositiveText;
    private CharSequence mNegativeText;
    private int mPositiveIconResId = 0;
    private int mNegativeIconResId = 0;
    private CharSequence[] mItems;
    private int mFrameColor;
    private int mIcon;
    private boolean mStarIcon;

    private boolean mCancel = true;
    private boolean mCancelTouchOutside = true;

    private static boolean mIsShowStatus =false;

    /**
     * 생성자
     * 
     * @param context
     *            콘텍스트
     */
    public Builder(Context context) {
        mContext = context;

        mResolutionUtils = new ResolutionUtils(context);
    }

    /**
     * 타이틀을 설정 한다
     * 
     * @param title
     *            설정할 타이틀
     * @return 빌더 자신
     */
    public Builder setTitle(CharSequence title) {
    	mTitle = title;
        return this;
    }

    public Builder setStarIcon(boolean toogle) {
    	mStarIcon = toogle;
        return this;
    }

    /**
     * 메세지를 설정한다
     * 
     * @param message
     *            설정할 메세지
     * @return 빌더 자신
     */
    public Builder setMessage(CharSequence message) {
        if (mView != null) {
        	mView = null;
        }

        mMessage = message;
        return this;
    }

    /**
     * 커스텀한 얼럿 다이얼로그를 띄우기 위한 함수
     * 
     * @param view
     *            다이얼로그내에 포함 시킬 뷰
     * @return 빌더 자신
     */
    public Builder setView(View view) {
        if (!TextUtils.isEmpty(mMessage))
        	mMessage = null;

        mView = view;
        return this;
    }

    /**
     * 커스텀한 얼럿 다이얼로그를 띄우기 위한 함수
     * 
     * @param _resourceID
     *            뷰의 리소스 ID
     * @return
     */
    public Builder setView(int _resourceID) {
        View v = View.inflate(mContext, _resourceID, null);
        return setView(v);
    }

    /**
     * 확인 버튼 리스너를 등록 한다
     * 
     * @param _text
     *            확인버튼 텍스트
     * @param _listener
     *            눌렀을 경우 호출할 리스너
     * @return 빌더 자신
     */
    public Builder setPositiveButton(CharSequence _text, final OnClickListener _listener) {
        setPositiveButton(_text, _listener, 0);
        return this;
    }

    public Builder setPositiveButton(CharSequence _text, final OnClickListener _listener, int _iconResId) {
    	mPositiveText = _text;
        mPositiveButtonlistener = _listener;
        mPositiveIconResId = _iconResId;
        return this;
    }

    /**
     * 취소 버튼 리스너를 등록 한다
     * 
     * @param _text
     *            취소 버튼 텍스트
     * @param _listener
     *            눌렀을 경우 호출할 리스너
     * @return 빌더 자신
     */
    public Builder setNegativeButton(CharSequence _text, final OnClickListener _listener) {
        setNegativeButton(_text, _listener, 0);
        return this;
    }

    public Builder setNegativeButton(CharSequence _text, final OnClickListener _listener, int _iconResId) {
    	mNegativeText = _text;
        mNegativeButtonlistener = _listener;
        mNegativeIconResId = _iconResId;
        return this;
    }

    /**
     * 다이얼로그 스트링 리스트 타입
     * 
     * @param _items
     *            리스트 아이템
     * @param _listener
     *            아이템을 클릭할 경우 호출할 리스트
     * @return 빌더 자신
     */
    public Builder setItems(CharSequence[] _items, final OnClickListener _listener) {
        if (!TextUtils.isEmpty(mMessage))
        	mMessage = null;

        mItems = _items;
        mOnItemClickListener = _listener;
        return this;
    }

    /**
     * 백버튼을 눌렀을 경우 켄슬 가능 하게 할지 말지 설정
     * 
     * @see
     * @param _cancel
     *            켄슬 가능 여부
     * @return 빌더 자신
     */
    public Builder setCancelable(boolean _cancel) {
    	mCancel = _cancel;
        return this;
    }

    public Builder setCanceledOnTouchOutside(boolean _cancel){
    	mCancelTouchOutside = _cancel;
        return this;
    }

    /**
     * 태두리 칼라 설정
     * 
     * @param _color
     *            설정할 칼라값
     * @return 빌더 자신
     */
    public Builder setFrameColor(int _color) {

    	mFrameColor = _color;
        return this;
    }

    /**
     * 아이콘 설정 타이틀과 같이 사용할 수 있다
     * 
     * @param _icon
     *            아이콘 리소스 ID
     * @return 빌더 자신
     */
    public Builder setIcon(int _icon) {
    	mIcon = _icon;
        return this;
    }

    /**
     * 다이알로그 생성
     * 
     * @return 생성된 다이알로그
     */
    public Dialog create() {
        final Dialog dlg = new Dialog(mContext, R.style.AppTheme_Builder);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.builder_custom_dialog);
        dlg.setOnDismissListener(new OnDismissListener(){

            @Override
            public void onDismiss(DialogInterface dialog) {
            	mIsShowStatus = false;
            }});

        if (mStarIcon == true) {
            ((LinearLayout) dlg.findViewById(R.id.layout_custom_dialog_fan_container))
            .setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(mTitle)) {
            TextView title = (TextView) dlg.findViewById(R.id.title);
            title.setVisibility(View.VISIBLE);
            title.setText(mTitle);

        }

        if (!TextUtils.isEmpty(mMessage)) {
            TextView message = (TextView) dlg.findViewById(R.id.message);
            message.setVisibility(View.VISIBLE);
            message.setText(mMessage);
        }

        if (mIcon != 0) {
            ImageView icon = (ImageView) dlg.findViewById(R.id.icon);
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(mIcon);
        } else if (mItems != null && mItems.length > 0) {
            ListView listView = (ListView) dlg.findViewById(R.id.list);
            ((LinearLayout) dlg.findViewById(R.id.content_frame))
            .setBackgroundResource(R.drawable.bg_popup_txtarea_grey);
            // ListAdapter adapter = new ArrayAdapter<CharSequence>(m_context,
            // R.layout.drop_down_list_item, R.id.text1, m_items);
            PopupDialogAdapter adapter = new PopupDialogAdapter(mContext, mItems);
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> _listView, final View _view, int _position,
                    final long _id) {
                    if (mOnItemClickListener != null) {
                    	mOnItemClickListener.onClick(dlg, _position);
                    }

                    dlg.dismiss();
                }
            });

            listView.setAdapter(adapter);
            listView.setVisibility(View.VISIBLE);
            dlg.findViewById(R.id.contentsView).setVisibility(View.VISIBLE);
        } else if (mView != null) {
            LinearLayout viewGroup = (LinearLayout) dlg.findViewById(R.id.contentsView);
            viewGroup.setVisibility(View.VISIBLE);
            viewGroup.addView(mView);

            viewGroup.setBackgroundColor(mFrameColor);
        }

        if (!TextUtils.isEmpty(mPositiveText)) {
            LinearLayout positiveContainer = (LinearLayout)dlg.findViewById(R.id.positive_container);
            positiveContainer.setVisibility(View.VISIBLE);
            TextView tvPositivie = (TextView) dlg.findViewById(R.id.tvPositive);
            tvPositivie.setText(mPositiveText);

            positiveContainer.setBackgroundResource(R.drawable.bg_popup_right_confirm);
//            if (m_NegativeText == null || m_NegativeText.length() == 0) {
//                positiveContainer.setBackgroundResource(R.drawable.bg_popup_confirm);
//            } else {
//                positiveContainer.setBackgroundResource(R.drawable.bg_popup_right_confirm);
//            }

            positiveContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View _v) {
                    if (mPositiveButtonlistener != null) {
                    	mPositiveButtonlistener.onClick(dlg, R.id.ivPositive);
                    }
                    dlg.dismiss();
                }
            });

            // Positive Button Icon
            if(mPositiveIconResId != 0) {
                ImageView ivPositivie = (ImageView) dlg.findViewById(R.id.ivPositive);
                ivPositivie.setImageResource(mPositiveIconResId);
                ivPositivie.setVisibility(View.VISIBLE);
            }
        }

        if (!TextUtils.isEmpty(mNegativeText)) {
            LinearLayout negativeContainer = (LinearLayout)dlg.findViewById(R.id.negative_container);
            negativeContainer.setVisibility(View.VISIBLE);
            TextView tvNegative = (TextView) dlg.findViewById(R.id.tvNegative);
            tvNegative.setText(mNegativeText);

            negativeContainer.setBackgroundResource(R.drawable.bg_popup_left_cancel);
//            if (m_PositiveText == null || m_PositiveText.length() == 0) {
//                negativeContainer.setBackgroundResource(R.drawable.bg_popup_confirm);
//            } else {
//                negativeContainer.setBackgroundResource(R.drawable.bg_popup_left_cancel);
//            }

            negativeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View _v) {
                    if (mNegativeButtonlistener != null) {
                    	mNegativeButtonlistener.onClick(dlg, R.id.ivNegative);
                    }
                    dlg.dismiss();
                }
            });

            // Negative Button Icon
            if(mNegativeIconResId != 0) {
                ImageView ivPositivie = (ImageView) dlg.findViewById(R.id.ivPositive);
                ivPositivie.setImageResource(mNegativeIconResId);
                ivPositivie.setVisibility(View.VISIBLE);
            }
        }

        // if(m_NegativeText!=null&&m_NegativeText.length()>0&&m_PositiveText!=null&&m_PositiveText.length()>0)
        // {
        // dlg.findViewById(R.id.buttonLine).setVisibility(View.VISIBLE);
        // } else {
        // dlg.findViewById(R.id.buttonLine).setVisibility(View.GONE);
        // }

        if(TextUtils.isEmpty(mTitle) && TextUtils.isEmpty(mMessage) && mIcon == 0){
            ((LinearLayout) dlg.findViewById(R.id.content_frame)).setVisibility(View.GONE);
            ((LinearLayout) dlg.findViewById(R.id.layout_button)).setLayoutParams(
                    new RelativeLayout.LayoutParams(mResolutionUtils.convertWidth(400), mResolutionUtils.convertHeight(200)));

        }

        dlg.setCancelable(mCancel);
        dlg.setCanceledOnTouchOutside(mCancelTouchOutside);

        return dlg;
    }

    /**
     * 다이얼로그 생성및 화면 표시
     */
    public void show() {
        create().show();
        mIsShowStatus = true;
    }

    public static boolean isShowStatus(){
        return 	mIsShowStatus;
    }

    public static void setShowStatus(boolean isShow) {
    	mIsShowStatus = isShow;
    }
}
