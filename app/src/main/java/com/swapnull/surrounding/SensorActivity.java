package com.swapnull.surrounding;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;


public class SensorActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;

    private Sensor mAcc;
    private Sensor mLight;
    private Sensor mGRAVITY;
    private Sensor mMAGNETIC;

    private TextView tvLight;
    private TextView tvACCELEROMETER;
    private TextView tvPressure;
    private TextView tvMAGNETIC;

    float[] accelerometerValues=new float[3];
    float[] magneticFieldValues=new float[3];
    float[] values=new float[3];
    float[] rotate=new float[9];
    private int orgs=0;
    private String pointKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00933B")));

        setContentView(R.layout.activity_sensor);


        tvLight = (TextView) findViewById(R.id.tvLight);
        tvACCELEROMETER = (TextView) findViewById(R.id.tvTemperature);
        tvPressure = (TextView) findViewById(R.id.tvPressure);
        tvMAGNETIC = (TextView) findViewById(R.id.tvReliableHumidity);


        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mMAGNETIC=mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(mAcc != null) {
            mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_UI);
        }

        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if(mLight != null) {
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_UI);
        }

        mGRAVITY = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        mMAGNETIC = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);//micro-Tesla (uT)

        if(mMAGNETIC != null) {
            mSensorManager.registerListener(this, mMAGNETIC, SensorManager.SENSOR_DELAY_UI);
        }

    }


    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
//        float millibars_of_pressure = event.values[0];
        // Do something with this sensor data.

        //Log.d(tag, "SENSOR "+ event.sensor);

        if(event.sensor == mLight)
        {
            tvLight.setText(String.format("%.2f", event.values[0]) +" lx");
        }
        else if(event.sensor == mAcc)
        {
            accelerometerValues=event.values;
            tvACCELEROMETER.setText(String.format("%.2f", event.values[0]) + " m/s^2");
        }
        else if(event.sensor == mMAGNETIC)
        {
            magneticFieldValues=event.values;
            tvMAGNETIC.setText(String.format("%.2f", event.values[0]) + " uT" );
        }

        SensorManager.getRotationMatrix(rotate,null,accelerometerValues,magneticFieldValues);
        SensorManager.getOrientation(rotate,values);

        values[0]=(float) ((values[0])*180/Math.PI);
        orgs=(int)values[0];
        pointKey=toEast(orgs);
        tvPressure.setText(pointKey);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAcc, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMAGNETIC, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private static String toEast(int num) {
        if(num<45&&num>=-45)return "NORTH";
        else if(num>=45&&num<135)return "EAST";
        else if(num>=135&&num<=180)return "SOUTH";
        else if(num>=-180&&num<-135)return "SOUTH";
        else if(num>=-135&&num<-45)return"WEST";
        else return "NULL";
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.sensor, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
