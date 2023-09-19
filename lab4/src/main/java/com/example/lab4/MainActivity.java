package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView mAccelerometerX;
    TextView mAccelerometerY;
    TextView mAccelerometerZ;
    TextView mMagneticX;
    TextView mMagneticY;
    TextView mMagneticZ;
    TextView mProximity;
    TextView mLight;
    SensorManager sensorManager;
    Sensor mAccelerometerSensor;
    Sensor mProximitySensor;
    Sensor mMagneticSensor;
    Sensor mLightSensor;
    float mMaxValue;
    float mValue;
    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAccelerometerX = (TextView)findViewById(R.id.accelerometerX);
        mAccelerometerY = (TextView)findViewById(R.id.accelerometerY);
        mAccelerometerZ = (TextView)findViewById(R.id.accelerometerZ);
        mMagneticX = (TextView)findViewById(R.id.magneticX);
        mMagneticY = (TextView)findViewById(R.id.magneticY);
        mMagneticZ = (TextView)findViewById(R.id.magneticZ);
        mProximity = (TextView)findViewById(R.id.proximityVal);
        mLight = (TextView)findViewById(R.id.lightVal);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mProximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMaxValue = mLightSensor.getMaximumRange();
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            mAccelerometerX.setText(Float.toString(event.values[0]));
            mAccelerometerY.setText(Float.toString(event.values[1]));
            mAccelerometerZ.setText(Float.toString(event.values[2]));
        }
        if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            mMagneticX.setText(Float.toString(event.values[0]));
            mMagneticY.setText(Float.toString(event.values[1]));
            mMagneticZ.setText(Float.toString(event.values[2]));
        }
        if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            mProximity.setText(Float.toString(event.values[0]));
        }
        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            mLight.setText(Float.toString(event.values[0]));
            mValue =event.values[0];
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            layout.screenBrightness = (int)(255f* mValue / mMaxValue);
            getWindow().setAttributes(layout);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mMagneticSensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mProximitySensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mLightSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, mAccelerometerSensor);
        sensorManager.unregisterListener(this, mMagneticSensor);
        sensorManager.unregisterListener(this, mProximitySensor);
        sensorManager.unregisterListener(this, mLightSensor);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}