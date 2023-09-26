package com.example.lab6;

import static java.lang.Math.PI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawView(this));
    }

    class DrawView extends View {
        Paint p;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
        }

        @Override
        public void onDraw(Canvas canvas){
            canvas.drawARGB(80, 102, 204, 255);
            p.setColor(Color.BLUE);
            p.setStrokeWidth(15);
            drawPolygon(canvas, 5, 800, 1050);
            drawPolygon(canvas, 6, 800, 50);
        }

        public void drawPolygon(Canvas canvas, int countOfSides, float Xstart, float Ystart){
            int size = 500;
            for (int i = 1; i <= countOfSides; i++){
                double newX = size * Math.cos(i * 2 * PI / countOfSides);
                double newY = size * Math.sin(i * 2 * PI / countOfSides);
                canvas.drawLine(Xstart, Ystart, (float) (Xstart + newX), (float) (Ystart + newY),p);
                Xstart = (float) (Xstart + newX);
                Ystart = (float) (Ystart + newY);
            }
        }
    }
}