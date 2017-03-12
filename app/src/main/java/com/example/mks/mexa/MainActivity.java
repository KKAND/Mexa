package com.example.mks.mexa;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.LocationSource;
//import com.amap.api.maps.MapView;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;

import static com.example.mks.mexa.R.id.map;

public class MainActivity extends Activity implements LocationSource, AMapLocationListener {
    private MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption mapLocationClientOption;
    LocationManager locationManager;
    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapView mapView = (MapView) findViewById(map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        //初始化地图控制器对象
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        //setUpMap();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        button = (Button)findViewById(R.id.button);
        BTL l = new BTL();
        button.setOnClickListener(l);
        button2 = (Button)findViewById(R.id.button2);
        BTL2 l2 = new BTL2();
        button2.setOnClickListener(l2);



        System.out.println("测试1");
        aMap.setLocationSource(MainActivity.this);
        System.out.println("测试2");
        aMap.setMyLocationEnabled(true);
        System.out.println("测试3");
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        System.out.println("测试4");

    }

    /*private void setUpMap() {
        // 自定义系统定位小蓝点
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.sandroid));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);

    }*/

    class BTL implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        }
    }
    class BTL2 implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
        System.out.println("销毁地图");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        //mapView.onResume();
        //暂时注释掉这句话，经过测试，执行到这里就会闪退，暂时没有找到问题的原因
        System.out.println("resume3");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
        System.out.println("暂停");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
        System.out.println("保存状态");
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        System.out.println("激活定位");
        if (mapLocationClient == null) {
            //初始化定位
            System.out.println("初始化");
            mapLocationClient = new AMapLocationClient(this);
            //参数
            System.out.println("参数");
            mapLocationClientOption = new AMapLocationClientOption();
            //定位回调监听
            System.out.println("监听");
            mapLocationClient.setLocationListener(this);
            //高精度
            System.out.println("高精度");
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            System.out.println("设置定位");
            mapLocationClient.setLocationOption(mapLocationClientOption);
            System.out.println("开始定位");
            mapLocationClient.startLocation();
            System.out.println("测试经纬度GPS");


        }
    }

    @Override
    public void deactivate() {
        System.out.println("显示deactiv....");
        mListener = null;
        if (mapLocationClient != null){
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        System.out.println("显示监听类");
        mListener.onLocationChanged(aMapLocation);
        System.out.println("已经显示小蓝点");
        if (mListener != null && aMapLocation != null){
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0){
                mListener.onLocationChanged(aMapLocation);
                LatLng mLocalLatlng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocalLatlng, 18));
                System.out.println("显示小蓝点");
            }
            else {
                String errText = "定位失败，" + aMapLocation.getErrorCode() + ":" + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }

    }
}
