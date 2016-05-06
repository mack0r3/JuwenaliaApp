package com.mpier.juvenaliaapp.selfie;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mpier.juvenaliaapp.R;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("deprecation")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = CameraPreview.class.getName();

    private final Activity activity;

    private final Camera camera;
    private final int cameraId;
    private final List<Camera.Size> supportedPreviewSizes;

    private Camera.Size previewSize;

    private SurfaceHolder surfaceHolder;

    private final Bitmap logoBitmap;

    public CameraPreview(Activity activity, Camera camera, int cameraId) {
        super(activity);
        this.activity = activity;
        this.camera = camera;
        this.cameraId = cameraId;

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
        
        logoBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.logo_selfie);

        setWillNotDraw(false);
    }

    private void setCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
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

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        if (supportedPreviewSizes != null) {
            previewSize = getOptimalPreviewSize(supportedPreviewSizes, width, height);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            float ratio;
            if (previewSize.height >= previewSize.width) {
                ratio = (float) previewSize.height / (float) previewSize.width;
            } else {
                ratio = (float) previewSize.width / (float) previewSize.height;
            }
            setMeasuredDimension((int) (height * ratio), height);
        }
        else {
            float ratio;
            if (previewSize.height >= previewSize.width) {
                ratio = (float) previewSize.height / (float) previewSize.width;
            } else {
                ratio = (float) previewSize.width / (float) previewSize.height;
            }
            setMeasuredDimension(width, (int)(width * ratio));
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null)
            return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.height / size.width;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;

            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int previewWidth = getWidth();
        int previewHeight = getHeight();
        float ratio = (float)logoBitmap.getHeight() / logoBitmap.getWidth();

        int logoWidth = previewWidth / 3;
        int logoHeight = (int) (logoWidth * ratio);

        Rect rect = new Rect(previewWidth - logoWidth, previewHeight - logoHeight, previewWidth, previewHeight);
        canvas.drawBitmap(logoBitmap, null, rect, null);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(previewSize.width, previewSize.height);
            camera.setParameters(parameters);
            setCameraDisplayOrientation();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
