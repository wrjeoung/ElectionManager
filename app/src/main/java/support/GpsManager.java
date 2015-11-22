package support;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by Nam on 15. 7. 26..
 */
public class GpsManager implements LocationListener {
    private static int LOCATION_USE_TIME = 1 * 1000 * 60;   // 1분

    public interface OnLocation {
        void onLocation(double lat, double lng);
    }

    private Context mContext;

    private LocationManager mLocationManager;
    private String mProvider;

    private OnLocation mLocationListener;

    public GpsManager(Context context){
        mContext = context;

        mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

        /** 현재 사용가능한 위치 정보 장치 검색*/
        mProvider = getProvider();

//        /**마지막으로  조회했던 위치 얻기*/
//        Location location = mLocationManager.getLastKnownLocation(mProvider);
//
//        if(!isFindUseLocation(location)) {
//            Log.e("nam", "사용가능한 위치정보가 없거나 오래됐다");
//            startUpdateLocation();
//        }
//        else {
//            onLocationChanged(location);
//        }
    }

    public void setOnLocationListener(OnLocation listener){
        mLocationListener = listener;
    }

    private String getProvider(){
        String provider = getBestProvider();

        // 최적의 값이 없거나, 해당 장치가 사용가능한 상태가 아니라면,
        //모든 장치 리스트에서 사용가능한 항목 얻기
        if(provider == null || !mLocationManager.isProviderEnabled(provider)){
            // 모든 장치 목록
            List<String> list = mLocationManager.getAllProviders();

            for(int i = 0; i < list.size(); i++){
                //장치 이름 하나 얻기
                String temp = list.get(i);

                //사용 가능 여부 검사
                if(mLocationManager.isProviderEnabled(temp)){
                    provider = temp;
                    break;
                }
            }
        }// (end if)위치정보 검색 끝

        Log.e("nam", "provider : " + provider);
        return provider;
    }

    private String getBestProvider(){
        Criteria criteria = new Criteria();
        // 정확도
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 전원 소비량
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        // 고도, 높이 값을 얻어 올지를 결정
        criteria.setAltitudeRequired(false);
        // provider 기본 정보(방위, 방향)
        criteria.setBearingRequired(false);
        // 속도
        criteria.setSpeedRequired(false);
        // 위치 정보를 얻어 오는데 들어가는 금전적 비용
        criteria.setCostAllowed(true);

        return mLocationManager.getBestProvider(criteria, true);
    }

    private boolean isFindUseLocation(Location location){
        if(location == null)
            return false;

        long currentTime = System.currentTimeMillis();
        long locationTime = location.getTime();

        // location 시간이 LOCATION_USE_TIME이후 라면... false
        if(currentTime > locationTime + LOCATION_USE_TIME)
            return false;

        return true;
    }

    public void startUpdateLocation(){
        Location location = mLocationManager.getLastKnownLocation(mProvider);

        if(!isFindUseLocation(location)) {
            mLocationManager.requestLocationUpdates(mProvider, 100, 0, this);
        } else {
            onLocationChanged(location);
        }
    }

    public void stopUpdateLocation(){
        mLocationManager.removeUpdates(this);
    }

    /** 위도와 경도 기반으로 주소를 리턴하는 메서드*/
    public String getAddress(double lat, double lng){
        String address = null;

        //위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);

        List<Address> list = null;

        try{
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch(Exception e){
            e.printStackTrace();
        }

        if(list == null){
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return null;
        }

        if(list.size() > 0){
            Address addr = list.get(0);
            address = addr.getCountryName() + " "
//                    + addr.getPostalCode() + " "
                    + addr.getLocality() + " "
                    + addr.getThoroughfare() + " "
                    + addr.getFeatureName();
        }

        Log.e("nam", "address : " + address);
        return address;
    }

    @Override
    public void onLocationChanged(Location location) {
        // 위도, 경도
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Log.e("nam", "위도 : " + Double.toString(lat));
        Log.e("nam", "경도 : " + Double.toString(lng));

        stopUpdateLocation();

        if(mLocationListener != null)
            mLocationListener.onLocation(lat, lng);
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
