package com.jsloves.election.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;

import com.jsloves.election.activity.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utility {

    /**
     * action을 사용할 수 있는지 체크
     * @param context
     * @param action
     * @return
     */
    public static boolean isIntentAvailable(Context context, String action){
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent( action);
        List<ResolveInfo> list = packageManager.queryIntentActivities( intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 현재시간 구하기 ( yyyyMMdd_HHmmss )
     * @return
     */
    public static String getCurrentTimeFileName(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

        long currentTime = System.currentTimeMillis();
        return sdf.format(new Date(currentTime));
    }

    /**
     * DCIM폴더 밑에 앱명 폴더 생성
     * @param context
     * @return
     */
    public static String getDCIMDir(Context context){
        String targetDirPath = null;
        try{
            //check sdcard mount state
            String str = Environment.getExternalStorageState();

            if (str.equals(Environment.MEDIA_MOUNTED)) {
                String newDirName = context.getString(R.string.app_name);
                targetDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/DCIM/" + newDirName;

                File file = new File(targetDirPath);
                if(!file.exists()){
                    file.mkdirs();
                }else{
                }
            }else{
            }
        }catch(Exception e){
        }

        return targetDirPath;
    }

    /**
     * 원형 bitma으로 변환
     * @param bitmap
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);

        int size = (bitmap.getWidth() / 2);

        canvas.drawCircle(size, size, size, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
