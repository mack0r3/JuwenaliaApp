package com.mpier.juvenaliaapp.selfie;


import android.content.ContentValues;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selfie, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().findViewById(R.id.cameraLoading).setVisibility(View.VISIBLE);
        new CameraInitializer().execute();
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
        if (cameraPreview != null) {
            cameraPreview.releaseBitmap();
        }
        if (previewFrame != null) {
            previewFrame.removeView(cameraPreview);

            previewFrame.setVisibility(View.GONE);
            getView().findViewById(R.id.buttonCapture).setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.selfie_menu, menu);
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

    private class CameraInitializer extends AsyncTask<Void, Void, Boolean> {
        private Bitmap logoBitmap;

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean initializationSuccessful = false;

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
                    initializationSuccessful = (camera != null);
                    logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_selfie);
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to open Camera");
                e.printStackTrace();
            }

            return initializationSuccessful;
        }

        @Override
        protected void onPostExecute(Boolean initializationSuccessful) {
            if (!initializationSuccessful) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container, new LackOfCameraFragment());
                fragmentTransaction.commit();
            }
            else {
                View view = getView();

                cameraPreview = new CameraPreview(getActivity(), camera, cameraId, logoBitmap);

                previewFrame = (FrameLayout) view.findViewById(R.id.cameraPreview);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
                previewFrame.addView(cameraPreview, params);

                ImageButton button = (ImageButton) view.findViewById(R.id.buttonCapture);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        camera.takePicture(null, null, pictureCallback);
                    }
                });

                view.findViewById(R.id.cameraLoading).setVisibility(View.GONE);
                previewFrame.setVisibility(View.VISIBLE);
                view.findViewById(R.id.buttonCapture).setVisibility(View.VISIBLE);
            }
        }
    }

    private class SelfiePictureCallback implements Camera.PictureCallback {
        private Bitmap photoBitmap;

        @Override
        public void onPictureTaken(byte[] bitmapData, Camera camera) {
            new SelfieSaver().execute(bitmapData);
        }
    }

    private class SelfieSaver extends AsyncTask<byte[], Void, Void> {
        private boolean saveSuccessful;
        private File outputFile;

        @Override
        protected Void doInBackground(byte[]... params) {
            byte[] bitmapData = params[0];

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap photoBitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length, options);

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

            drawLogoOnCanvas(canvas);

            outputFile = getOutputImageFile();
            saveSuccessful = savePhoto(photoBitmap, outputFile);

            photoBitmap.recycle();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (saveSuccessful) {
                SelfieFragment.this.getView().findViewById(R.id.cameraPreview).setVisibility(View.GONE);
                ImageView photoView = (ImageView) SelfieFragment.this.getView().findViewById(R.id.photoPreview);
                photoView.setVisibility(View.VISIBLE);
                photoView.setImageURI(Uri.fromFile(outputFile));

                addPhotoToGallery(outputFile);
                Toast.makeText(getActivity(), getActivity().getString(R.string.selfie_photo_saved), Toast.LENGTH_LONG).show();
                sharePhoto(outputFile);
            }
        }

        private File getOutputImageFile() {
            // To be safe, you should check that the SDCard is mounted
            // using Environment.getExternalStorageState() before doing this.

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "JuwenaliaPW");

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

        private void drawLogoOnCanvas(Canvas canvas) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo_selfie);
            float logoWidth = logo.getWidth();
            float logoHeight = logo.getHeight();
            float ratio = logoHeight / logoWidth;

            int logoWidthOnPhoto = canvas.getWidth() / 3;
            int logoHeightOnPhoto = (int) (logoWidthOnPhoto * ratio);

            Bitmap scaledLogo = Bitmap.createScaledBitmap(logo, logoWidthOnPhoto, logoHeightOnPhoto, true);
            logo.recycle();

            canvas.drawBitmap(scaledLogo, canvas.getWidth() - logoWidthOnPhoto, canvas.getHeight() - logoHeightOnPhoto, null);
            scaledLogo.recycle();
        }

        private boolean savePhoto(Bitmap photoBitmap, File outputFile) {
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

            return true;
        }

        private void addPhotoToGallery(File photoFile) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, photoFile.lastModified());
            values.put(MediaStore.Images.Media.DATA, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, photoFile.getPath());

            getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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