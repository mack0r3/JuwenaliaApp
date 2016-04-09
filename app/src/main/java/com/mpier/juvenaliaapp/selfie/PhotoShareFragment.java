package com.mpier.juvenaliaapp.selfie;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.mpier.juvenaliaapp.MainActivity;
import com.mpier.juvenaliaapp.R;

import java.io.File;

public class PhotoShareFragment extends Fragment implements View.OnClickListener {
    private static String TAG = PhotoShareFragment.class.getName();
    private Uri photoUri;
    private boolean uiShown;


    public PhotoShareFragment() {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        FacebookSdk.sdkInitialize(activity);
    }

    @Override
    public void onClick(View v) {
        if (uiShown) {
            hideSystemUI();
            uiShown = false;
        }
        else {
            showSystemUI();
            uiShown = true;
        }
    }

    private void hideSystemUI() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void showSystemUI() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        final MainActivity mainActivity = (MainActivity) getActivity();
        //mainActivity.setActionBarTitle(mainActivity.getString(R.string.selfie_share_activity_title));


        ImageView photoToShareView = (ImageView) getView().findViewById(R.id.photoToShareView);

        Bundle args = getArguments();
        File photoFile = new File(args.getString("photoFilePath"));
        photoUri = Uri.fromFile(photoFile);

        photoToShareView.setImageURI(photoUri);
        photoToShareView.setClickable(true);
        photoToShareView.setOnClickListener(this);

        uiShown = true;

        View decorView = getActivity().getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            mainActivity.getSupportActionBar().show();

                            View shareButtonsView = getView().findViewById(R.id.shareButtonsLayout);
                            shareButtonsView.setVisibility(View.VISIBLE);
                            shareButtonsView.bringToFront();
                        } else {
                            mainActivity.getSupportActionBar().hide();

                            getView().findViewById(R.id.shareButtonsLayout).setVisibility(View.INVISIBLE);
                        }
                    }
                });


        ImageButton fullscreen = (ImageButton) getView().findViewById(R.id.fullscreenImage);
        fullscreen.setOnClickListener(this);

        ImageButton facebookShareButton = (ImageButton) getView().findViewById(R.id.facebookShareButton);
        facebookShareButton.setOnClickListener(new FacebookShareHandler());

        ImageButton instagramShareButton = (ImageButton) getView().findViewById(R.id.instagramShareButton);
        instagramShareButton.setOnClickListener(new InstagramShareHandler());

        ImageButton customShareButton = (ImageButton) getView().findViewById(R.id.customShareButton);
        customShareButton.setOnClickListener(new CustomShareHandler());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_share, container, false);
    }

    private boolean appInstalled(String packageName) {
        PackageManager pm = getActivity().getPackageManager();
        boolean appInstalled;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    private class FacebookShareHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (appInstalled("com.facebook.katana")) {
                SharePhoto photo = new SharePhoto.Builder().setImageUrl(photoUri).build();

                final SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(
                        photo).build();

                ShareDialog.show(PhotoShareFragment.this, content);
            } else {
                Toast.makeText(getActivity(), R.string.selfie_share_facebook_app_not_installed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class InstagramShareHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (appInstalled("com.instagram.android")) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.instagram.android");
                shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
                shareIntent.setType("image/*");

                startActivity(shareIntent);
            } else {
                Toast.makeText(getActivity(), R.string.selfie_share_instagram_app_not_installed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CustomShareHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
            shareIntent.setType("image/*");

            startActivity(Intent.createChooser(shareIntent, getActivity().getString(R.string.selfie_share_choose_app)));
        }
    }
}
