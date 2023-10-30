package com.example.lab7;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private float floatStartX = -1, floatStartY = -1, floatEndX = -1, floatEndY = -1;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint paint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = (ImageView) findViewById(R.id.imageView);
        setContentView(R.layout.activity_main);
    }

    private void drawPaintSketchImage(){
        if (bitmap == null){
            bitmap = Bitmap.createBitmap(imageView.getWidth(),
                    imageView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
        }
        canvas.drawLine(floatStartX,
                floatStartY-50,
                floatEndX,
                floatEndY-50,
                paint);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            floatStartX = event.getX();
            floatStartY = event.getY();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
            floatStartX = event.getX();
            floatStartY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            floatEndX = event.getX();
            floatEndY = event.getY();
            drawPaintSketchImage();
        }
        return true;
    }
}