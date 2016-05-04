package com.mpier.juvenaliaapp.selfie;


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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mpier.juvenaliaapp.MainActivity;
import com.mpier.juvenaliaapp.R;

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
    private int cameraId;
    private Camera.PictureCallback pictureCallback;
    private FrameLayout previewFrame;
    private CameraPreview cameraPreview;

    public SelfieFragment() {
        pictureCallback = new SelfiePictureCallback();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    private static File getOutputImageFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "JuwenaliaPW");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return new File(String.format("%s/IMG_%s.jpg", mediaStorageDir.getPath(), timeStamp));
    }

    @Override
    public void onResume() {
        super.onResume();

        MainActivity mainActivity = (MainActivity) getActivity();
        //mainActivity.getSupportActionBar().hide();
        //mainActivity.setActionBarTitle(mainActivity.getString(R.string.selfie_activity_title));

        boolean initializationSuccessful = initializeCamera();

        if (!initializationSuccessful) {
            Toast.makeText(getActivity(), R.string.selfie_lack_of_front_camera, Toast.LENGTH_LONG).show();
            ImageButton button = (ImageButton) getView().findViewById(R.id.buttonCapture);
            button.setVisibility(View.INVISIBLE);
        } else {
            cameraPreview = new CameraPreview(getActivity(), camera, cameraId);

            previewFrame = (FrameLayout) getView().findViewById(R.id.cameraPreview);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            previewFrame.addView(cameraPreview, params);

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

        //MainActivity mainActivity = (MainActivity) getActivity();
        //mainActivity.getSupportActionBar().show();

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
            int idOfCameraFacingFront = -1;
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    idOfCameraFacingFront = i;
                }
            }
            if (idOfCameraFacingFront != -1) {
                camera = Camera.open(idOfCameraFacingFront);
                cameraId = idOfCameraFacingFront;
                opened = (camera != null);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to open Camera");
            e.printStackTrace();
        }

        return opened;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_selfie_info: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_AlertDialog);
                builder.setMessage(getContext().getString(R.string.selfie_info_message))
                        .setTitle(getContext().getString(R.string.selfie_info_title))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.selfie_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selfie, container, false);
    }

    private int getCameraDisplayOrientation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
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
        return result;
    }

    private class SelfiePictureCallback implements Camera.PictureCallback {
        private Bitmap photoBitmap;

        @Override
        public void onPictureTaken(byte[] bitmapData, Camera camera) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            photoBitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length, options);

            Matrix matrix = new Matrix();
            int rotation = getCameraDisplayOrientation();
            if (rotation != 0) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    rotation += 180;
                }
                matrix.postRotate(rotation);
            }
            matrix.postScale(-1, 1);
            Bitmap rotated = Bitmap.createBitmap(photoBitmap, 0, 0, photoBitmap.getWidth(), photoBitmap.getHeight(), matrix, true);
            photoBitmap.recycle();
            photoBitmap = rotated;


            final Canvas canvas = new Canvas(photoBitmap);

            AlertDialog.Builder builder = new AlertDialog.Builder(SelfieFragment.this.getActivity(), R.style.AppTheme_AlertDialog);
            builder.setTitle(R.string.selfie_input_title);
            builder.setMessage(getActivity().getString(R.string.selfie_input_message));

            final EditText input = new EditText(builder.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});

            builder.setView(input);

            builder.setPositiveButton(R.string.selfie_input_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = input.getText().toString();

                    drawTextOnCanvas(canvas, text);

                    drawLogoOnCanvas(canvas);

                    File outputFile = getOutputImageFile();
                    boolean saveSuccessful = savePhoto(outputFile);
                    if (saveSuccessful) {
                        sharePhoto(outputFile);
                    }
                }
            });
            builder.setNegativeButton(R.string.selfie_input_without, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                    drawLogoOnCanvas(canvas);

                    File outputFile = getOutputImageFile();
                    boolean saveSuccessful = savePhoto(outputFile);
                    if (saveSuccessful) {
                        sharePhoto(outputFile);
                    }
                }
            });

            AlertDialog textInputDialog = builder.create();
            textInputDialog.setCancelable(false);
            textInputDialog.setCanceledOnTouchOutside(false);
            textInputDialog.show();
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

                canvas.drawRect(0, yPos - 1.25f * scaledPx, photoBitmap.getWidth(), yPos + 0.25f * scaledPx, backgroundPaint);

                canvas.drawText(text, xPos, yPos, paint);
            }
        }

        private void drawLogoOnCanvas(Canvas canvas) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.jacek_selfie);
            int logoWidth = logo.getWidth();
            int logoHeight = logo.getHeight();
            float ratio = logoHeight / logoWidth;

            int logoWidthOnPhoto = canvas.getWidth() / 4;
            int logoHeightOnPhoto = (int) (logoWidthOnPhoto * ratio);

            Bitmap scaledLogo = Bitmap.createScaledBitmap(logo, logoWidthOnPhoto, logoHeightOnPhoto, true);
            logo.recycle();

            canvas.drawBitmap(scaledLogo, canvas.getWidth() - logoWidthOnPhoto, canvas.getHeight() - logoHeightOnPhoto, null);
            scaledLogo.recycle();
        }

        private boolean savePhoto(File outputFile) {
            if (outputFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions");
                return false;
            }

            try {
                FileOutputStream fos = new FileOutputStream(outputFile);

                photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
                return false;
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
                return false;
            }

            photoBitmap.recycle();

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