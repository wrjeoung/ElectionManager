package com.jsloves.election.util;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by k on 2015-08-14.
 * GPS 정보 가져오기
 */
public class GpsInfo extends Service implements LocationListener {

    public static final String TAG = GpsInfo.class.getSimpleName();
    private final Context mContext;
    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;
    // 네트워크 사용유무
    boolean isNetworkEnabled = false;
    // GPS 상태값
    boolean isGetLocation = false;

    Location location;
    double lat;  // 위도
    double lon;  // 경도

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GpsInfo(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        Log.d(TAG,"getLocation()");
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // GPS 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d(TAG," isGPSEnabled : " + isGPSEnabled);
            Log.d(TAG," isNetworkEnabled : " + isNetworkEnabled);
            if(!isGPSEnabled && !isNetworkEnabled){
                Log.d(TAG,"GPS / Network 사용 가능하지 않음");
                // GPS 와 네트워크 사용이 가능하지 않을때 소스 구현
            }else {
                this.isGetLocation = true;
                // 네트워크 정보로 부터 위치값 가져오기
                if(isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            // 위도 경도 저장
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                            Log.d(TAG,"네트워크 사용");
                            Log.d(TAG,"lat : " + lat + " long : " + lon);
                        }
                    }
                }
                // GPS 정보로 부터 위치값 가져오기
                if(isGPSEnabled) {
                    if(location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES,this);

                        if(locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location !=  null) {
                                // 위도 경도 저장
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                Log.d(TAG,"GPS 사용");
                                Log.d(TAG,"lat : " + lat + " long : " + lon);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     *  GPS 종료
     */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsInfo.this);
        }
    }

    /**
     *  위도값을 가져옵니다.
     */
    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }

    /**
     *  경도값을 가져옵니다.
     */
    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }
        return lon;
    }

    /**
     *  GPS 나 WIFI 정보가 켜져있는지 확인합니다.
     */
    public boolean isGetLocation(){
        return this.isGetLocation;
    }

    /**
     *  GPS 정보를 가져오지 못했을때
     *  설정값으로 갈지 물어보는 alert 창
     */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. \n 설정창으로 가시겠습니까?");

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    // 위도와 경도 기반으로 주소를 리턴하는 메서드
    public String getAddress(double lat, double lon){
        String address = "";
        String address1 = "";
        // 위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        // 주소 목록을 담기 위한 HashMap
        List<Address> list = null;

        try {
            list = geocoder.getFromLocation(lat,lon, 1);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(list == null){
            Log.d(TAG,"주소 데이터 얻기 실패");
            return null;
        }

        if(list.size() > 0){
            Address addr = list.get(0);
            address = addr.getAdminArea() + " " + addr.getLocality() + " " + addr.getSubLocality() + " " + addr.getThoroughfare() + " " + addr.getFeatureName();
            address1 = addr.getUrl() + " " + addr.getExtras() + " " + addr.getSubLocality() + " " + addr.getSubThoroughfare() + " "
                      + addr.getSubAdminArea() + " " + addr.getCountryCode() + " " + addr.getCountryName() + " " + addr.getPhone() + " "
                      + addr.getPostalCode() + " " + addr.getPremises();

        }
        Log.d(TAG, "address : " + address);
        //Toast.makeText(mContext," address : " + address,Toast.LENGTH_SHORT).show();
        Log.d(TAG, "address1 : " + address1);
        //Toast.makeText(mContext," address1 : " + address1,Toast.LENGTH_SHORT).show();
        return address;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}