package com.mpier.juvenaliaapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressWarnings("deprecation")
public class SelfieFragment extends Fragment {
    private static String TAG = SelfieFragment.class.getName();
    private Camera camera;
    private Camera.PictureCallback pictureCallback;
    private FrameLayout previewFrame;
    private CameraPreview cameraPreview;

    public SelfieFragment() {
        pictureCallback = new SelfiePictureCallback();
    }

    private static File getOutputImageFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "JuwenaliaPW");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("JuwenaliaPW", "failed to create directory");
                return null;
            }
        }

        // Create a media file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return new File(String.format("%s\\IMG_%s.jpg", mediaStorageDir.getPath(), timeStamp));
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setActionBarTitle(mainActivity.getString(R.string.selfie_activity_title));

        boolean initializationSuccessful = initializeCamera();

        if (!initializationSuccessful) {
            Toast.makeText(getActivity(), R.string.selfie_lack_of_front_camera, Toast.LENGTH_LONG).show();
            ImageButton button = (ImageButton) getView().findViewById(R.id.buttonCapture);
            button.setVisibility(View.INVISIBLE);
        }
        else {
            cameraPreview = new CameraPreview(getActivity(), camera);

            previewFrame = (FrameLayout) getView().findViewById(R.id.cameraPreview);
            previewFrame.addView(cameraPreview);

            ImageButton button = (ImageButton) getView().findViewById(R.id.buttonCapture);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    camera.takePicture(null, null, pictureCallback);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
        if (previewFrame != null) {
            previewFrame.removeView(cameraPreview);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private boolean initializeCamera() {
        boolean opened = false;

        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            int numberOfCameraFacingFront = -1;
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    numberOfCameraFacingFront = i;
                }
            }
            if (numberOfCameraFacingFront != -1) {
                camera = Camera.open(numberOfCameraFacingFront);
                opened = (camera != null);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to open Camera");
            e.printStackTrace();
        }

        return opened;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selfie, container, false);
    }

    private class SelfiePictureCallback implements Camera.PictureCallback {
        private Bitmap rotatedPhotoBitmap;

        @Override
        public void onPictureTaken(byte[] bitmapData, Camera camera) {
            Bitmap photoBitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);

            Matrix matrix = new Matrix();
            matrix.postRotate(-90);

            rotatedPhotoBitmap = Bitmap.createBitmap(photoBitmap, 0, 0, photoBitmap.getWidth(), photoBitmap.getHeight(), matrix, true);

            photoBitmap.recycle();

            final Canvas canvas = new Canvas(rotatedPhotoBitmap);

            AlertDialog.Builder builder = new AlertDialog.Builder(SelfieFragment.this.getActivity());
            builder.setTitle(R.string.selfie_input_text);
            final EditText input = new EditText(SelfieFragment.this.getActivity());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
            builder.setView(input);

            builder.setPositiveButton(R.string.selfie_input_text_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = input.getText().toString();

                    drawTextOnCanvas(canvas, text);

                    File outputFile = getOutputImageFile();
                    boolean saveSuccessful = savePhoto(outputFile);
                    if (saveSuccessful) {
                        sharePhoto(outputFile);
                    }
                }
            });
            builder.setNegativeButton(R.string.selfie_input_tekst_without, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    File outputFile = getOutputImageFile();
                    boolean saveSuccessful = savePhoto(outputFile);
                    if (saveSuccessful) {
                        sharePhoto(outputFile);
                    }
                }
            });

            builder.show();
        }

        private void drawTextOnCanvas(Canvas canvas, String text) {
            if (text != null && !text.isEmpty()) {
                Paint paint = new Paint();
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setColor(Color.BLACK);

                float densityMultiplier = getActivity().getResources().getDisplayMetrics().density;
                float scaledPx = 20 * densityMultiplier;

                paint.setTextSize(scaledPx);
                float textWidth = paint.measureText(text);

                float xPos = (canvas.getWidth() / 2) - textWidth / 2;
                float yPos = (canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2);

                Paint backgroundPaint = new Paint();
                backgroundPaint.setColor(Color.WHITE);
                backgroundPaint.setAlpha(100);

                canvas.drawRect(0, yPos - scaledPx - 5, rotatedPhotoBitmap.getWidth(), yPos + 5, backgroundPaint);


                canvas.drawText(text, xPos, yPos, paint);
            }
        }

        private boolean savePhoto(File outputFile) {
            if (outputFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions");
                return false;
            }

            try {
                FileOutputStream fos = new FileOutputStream(outputFile);

                rotatedPhotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
                return false;
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
                return false;
            }

            rotatedPhotoBitmap.recycle();

            Toast.makeText(getActivity(), getActivity().getString(R.string.selfie_photo_saved) + outputFile.getPath(), Toast.LENGTH_LONG).show();

            return true;
        }

        private void sharePhoto(File photoFile) {
            Fragment newFragment = new PhotoShareFragment();

            Bundle args = new Bundle();
            args.putString("photoFilePath", photoFile.getPath());
            newFragment.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_container, newFragment);
            transaction.commit();
        }
    }
}