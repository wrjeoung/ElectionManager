package support.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jsloves.election.activity.R;

public class NToast {
    public static final int LONG = Toast.LENGTH_LONG;
    public static final int SHORT = Toast.LENGTH_SHORT;

	public static void show(Context context, String text, int duration){
		show(context, text, duration, Gravity.NO_GRAVITY);
	}
	
	public static void show(Context context, String text, int duration, int gravity) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.util_ntoast_layout, null);

        TextView textV = (TextView) layout.findViewById(R.id.txt_toast);
        textV.setText(text);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        if(gravity != Gravity.NO_GRAVITY)
        	toast.setGravity(gravity, 0, 0);

        toast.setView(layout);
        toast.show();
    }
}
