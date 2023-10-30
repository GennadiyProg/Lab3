package com.example.lab7_2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    private List<Float> floatStartX, floatStartY, floatEndX, floatEndY;

    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatStartX = Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        floatStartY = Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        floatEndX = Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        floatEndY = Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        imageView = findViewById(R.id.imageView);
    }

    private void drawPaintSketchImage(int index){
        if (bitmap == null){
            bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
        }
        canvas.drawLine(floatStartX.get(index), floatStartY.get(index) - 50, floatEndX.get(index), floatEndY.get(index) - 50, paint);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        if (event.getAction() == MotionEvent.ACTION_POINTER_DOWN){
            for (int i = 0; i < event.getPointerCount(); i++) {
                floatStartX.set(index, event.getX(index));
                floatStartY.set(index, event.getY(index));
            }
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            floatStartX.set(index, event.getX(index));
            floatStartY.set(index, event.getY(index));
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            for (int i = 0; i < event.getPointerCount(); i++) {
                floatEndX.set(i, event.getX(i));
                floatEndY.set(i, event.getY(i));
                drawPaintSketchImage(event.getPointerId(i));
                floatStartX.set(i, event.getX(i));
                floatStartY.set(i, event.getY(i));
            }
        }
        if (event.getAction() == MotionEvent.ACTION_POINTER_UP){
            for (int i = 0; i < event.getPointerCount(); i++) {
                floatEndX.set(index, event.getX(index));
                floatEndY.set(index, event.getY(index));
                drawPaintSketchImage(event.getPointerId(index));
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            floatEndX.set(index, event.getX(index));
            floatEndY.set(index, event.getY(index));
            drawPaintSketchImage(event.getPointerId(index));
        }
        return super.onTouchEvent(event);
    }
}