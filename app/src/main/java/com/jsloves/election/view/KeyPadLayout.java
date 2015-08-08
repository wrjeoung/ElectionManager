package com.jsloves.election.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.jsloves.election.activity.R;

public class KeyPadLayout extends LinearLayout
        implements View.OnClickListener
{
    public static final String TAG = KeyPadLayout.class.getSimpleName();
    public static final int KEYPAD_BACK = 11;
    public static final int KEYPAD_CANCEL = 10;
    private Context context;
    private keyPadListener keyPadListener;
    private ImageButton lockButtonBack;
    private Button lockButtonCancel;
    private ImageButton lockButtonNum0;
    private ImageButton lockButtonNum1;
    private ImageButton lockButtonNum2;
    private ImageButton lockButtonNum3;
    private ImageButton lockButtonNum4;
    private ImageButton lockButtonNum5;
    private ImageButton lockButtonNum6;
    private ImageButton lockButtonNum7;
    private ImageButton lockButtonNum8;
    private ImageButton lockButtonNum9;
    ViewGroup view9Button;

    public KeyPadLayout(Context paramContext)
    {
        super(paramContext);
        Log.d(TAG,"KeyPadLayout1()");
        this.context = paramContext;
        init();
    }

    public KeyPadLayout(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        setKeypadListener((keyPadListener)paramContext);
        Log.d(TAG, "KeyPadLayout2()");
        this.context = paramContext;
        init();
    }

    public KeyPadLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet);
        Log.d(TAG, "KeyPadLayout3()");
        this.context = paramContext;
        init();
    }

    public void init()
    {
        this.view9Button = ((ViewGroup)((LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_keypad_view, this, true));
        this.lockButtonNum1 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_1));
        this.lockButtonNum2 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_2));
        this.lockButtonNum3 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_3));
        this.lockButtonNum4 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_4));
        this.lockButtonNum5 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_5));
        this.lockButtonNum6 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_6));
        this.lockButtonNum7 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_7));
        this.lockButtonNum8 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_8));
        this.lockButtonNum9 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_9));
        this.lockButtonNum0 = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_0));
        this.lockButtonBack = ((ImageButton)this.view9Button.findViewById(R.id.passcode_btn_b));
        this.lockButtonCancel = ((Button)this.view9Button.findViewById(R.id.passcode_btn_c));
        this.lockButtonNum1.setOnClickListener(this);
        this.lockButtonNum2.setOnClickListener(this);
        this.lockButtonNum3.setOnClickListener(this);
        this.lockButtonNum4.setOnClickListener(this);
        this.lockButtonNum5.setOnClickListener(this);
        this.lockButtonNum6.setOnClickListener(this);
        this.lockButtonNum7.setOnClickListener(this);
        this.lockButtonNum8.setOnClickListener(this);
        this.lockButtonNum9.setOnClickListener(this);
        this.lockButtonNum0.setOnClickListener(this);
        this.lockButtonBack.setOnClickListener(this);
        this.lockButtonCancel.setOnClickListener(this);
    }

    public void onClick(View paramView)
    {
        this.keyPadListener.keypadClicked(paramView);
    }

    public void setKeypadListener(keyPadListener paramkeyPadListener)
    {
        Log.d(TAG,"setKeypadListener");
        this.keyPadListener = paramkeyPadListener;
    }

    public void setNoCancelButton(boolean paramBoolean)
    {
        if (paramBoolean)
        {
            this.lockButtonCancel.setBackgroundResource(R.drawable.lock_btn_empty_nor);
            this.lockButtonCancel.setOnClickListener(null);
            return;
        }
        this.lockButtonCancel.setBackgroundResource(R.drawable.keypad_num_cancel_selector);
        this.lockButtonCancel.setOnClickListener(this);
    }

    public void stopKeyEvent()
    {
        this.lockButtonNum1.setOnClickListener(null);
        this.lockButtonNum2.setOnClickListener(null);
        this.lockButtonNum3.setOnClickListener(null);
        this.lockButtonNum4.setOnClickListener(null);
        this.lockButtonNum5.setOnClickListener(null);
        this.lockButtonNum6.setOnClickListener(null);
        this.lockButtonNum7.setOnClickListener(null);
        this.lockButtonNum8.setOnClickListener(null);
        this.lockButtonNum9.setOnClickListener(null);
        this.lockButtonNum0.setOnClickListener(null);
        this.lockButtonBack.setOnClickListener(null);
        this.lockButtonCancel.setOnClickListener(null);
    }

    public static abstract interface keyPadListener
    {
        public abstract void keypadClicked(View paramView);
    }
}