package com.example.lab4_2;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView text;
    SensorManager sensorManager;
    Sensor mAccelerometerSensor;
    Sensor mLightSensor;
    float mMaxValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMaxValue = mLightSensor.getMaximumRange();
    }
    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this, mAccelerometerSensor);
        sensorManager.unregisterListener(this, mLightSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            int prevWidth = (int)text.getTranslationX();
            int actualWidth = (int)(getResources().getDisplayMetrics().widthPixels / 2 - event.values[0] * 50);
            if (prevWidth / 10 != actualWidth / 10) {
                text.setX(actualWidth);
            }
            int prevHeight = (int)text.getTranslationY();
            int actualHeight = (int)(getResources().getDisplayMetrics().heightPixels / 2 + event.values[1] * 50);
            if (prevHeight / 10 != actualHeight / 10) {
                text.setY(actualHeight);
            }
        } else if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            WindowManager.LayoutParams layout = getWindow().getAttributes();
            layout.screenBrightness = (int)(255f* event.values[0] / mMaxValue);
            getWindow().setAttributes(layout);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}