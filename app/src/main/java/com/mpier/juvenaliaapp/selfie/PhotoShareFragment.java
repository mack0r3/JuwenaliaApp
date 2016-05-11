package com.mpier.juvenaliaapp.selfie;


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

import com.mpier.juvenaliaapp.R;

import java.io.File;

public class PhotoShareFragment extends Fragment {
    private static String TAG = PhotoShareFragment.class.getName();
    private boolean uiShown;
    private Uri photoUri;


    public PhotoShareFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();

        ImageView photoToShareView = (ImageView) getView().findViewById(R.id.photoToShareView);

        Bundle args = getArguments();
        File photoFile = new File(args.getString("photoFilePath"));
        photoUri = Uri.fromFile(photoFile);

        photoToShareView.setImageURI(photoUri);
        photoToShareView.setClickable(true);

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
        getActivity().setTitle(R.string.selfie_share_title);
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
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setPackage("com.facebook.katana");
                shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
                shareIntent.setType("image/*");

                startActivity(shareIntent);
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
