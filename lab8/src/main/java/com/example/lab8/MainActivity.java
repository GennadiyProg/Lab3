package com.example.lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    SurfaceView sv;
    SurfaceHolder holder;
    HolderCallback holderCallback;
    Camera camera;
    Button button;

    final int CAMERA_ID = 0;
    final boolean FULL_SCREEN = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sv = findViewById(R.id.surfaceView);
        button = findViewById(R.id.button);
        button.setOnClickListener(v-> takePhoto(v));
        holder = sv.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holderCallback = new HolderCallback();
        holder.addCallback(holderCallback);
    }

    public void takePhoto(View view) {
        camera.takePicture(null, null, (data, camera) -> {
            final ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "photo"+ Math.random() +".jpg");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

            final ContentResolver resolver = getApplicationContext().getContentResolver();
            Uri uri = null;
            try {
                final Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                uri = resolver.insert(contentUri, values);
                if (uri == null)
                    throw new IOException("Failed to create new MediaStore record.");
                try (final OutputStream stream = resolver.openOutputStream(uri)) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);
                    if (stream == null)
                        throw new IOException("Failed to open output stream.");
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream))
                        throw new IOException("Failed to save bitmap.");
                }
            } catch (IOException e) {
                if (uri != null) {
                    resolver.delete(uri, null, null);
                }
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        camera = camera.open(CAMERA_ID);
        setPreviewSize(FULL_SCREEN);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(camera!=null)
            camera.release();
        camera = null;
    }
    class HolderCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            camera.stopPreview();
            setCameraDisplayOrientation(CAMERA_ID);
            try{
                camera.setPreviewDisplay((holder));
                camera.startPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

        }
    }
    public void setPreviewSize(boolean fullScreen){
        Display display = getWindowManager().getDefaultDisplay();
        boolean widthIsMax = display.getWidth() > display.getHeight();

        Camera.Size size = camera.getParameters().getPreviewSize();

        RectF rectDisplay = new RectF();
        RectF rectPreview = new RectF();

        rectDisplay.set(0,0,display.getWidth(),display.getHeight());

        if(widthIsMax){
            rectPreview.set(0,0, size.width,size.height);
        }else{
            rectPreview.set(0,0,size.height,size.width);
        }

        Matrix matrix = new Matrix();

        if(!fullScreen){
            matrix.setRectToRect(rectPreview,rectDisplay,Matrix.ScaleToFit.START);
        }else{
            matrix.setRectToRect(rectDisplay,rectPreview,Matrix.ScaleToFit.START);
            matrix.invert(matrix);
        }

        matrix.mapRect(rectPreview);

        sv.getLayoutParams().height = (int) (rectPreview.bottom);
        sv.getLayoutParams().width = (int) (rectPreview.right);
    }

    void setCameraDisplayOrientation(int cameraId){
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,info);

        if(info.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
            result = ((360 - degrees) + info.orientation);
        }else if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = ((360 - degrees) - info.orientation);
            result += 360;
        }

        result = result % 360;
        camera.setDisplayOrientation(result);
    }

}