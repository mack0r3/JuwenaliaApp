package com.mpier.juvenaliaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class TelebimFragment extends Fragment {

    View inflatedView;
    FloatingActionButton sendMsgBtn;
    EditText greetingsEditText;
    TextView greetingsTextView;

    private static final String PARAM_NAME = "name";

    private String name = "";

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {

        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                name = profile.getFirstName();
                showMessagePanel();
            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
        }
    };

    public TelebimFragment() {
    }

    public static TelebimFragment newInstance(String name) {
        TelebimFragment telebimFragment = new TelebimFragment();
        Bundle b = new Bundle();
        b.putString(PARAM_NAME, name);
        telebimFragment.setArguments(b);
        return telebimFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        if (getArguments() != null) {
            name = getArguments().getString(PARAM_NAME);
        }

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    hideMessagePanel();
                }
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        getActivity().setTitle(R.string.menu_telebim);

        inflatedView = inflater.inflate(R.layout.fragment_telebim, container, false);
        greetingsTextView = (TextView) inflatedView.findViewById(R.id.greetingsTextView);

        if (isLoggedIn())
            showMessagePanel();
        else hideMessagePanel();

        initializeSendMsgBtn();
        initializeMessageEditText();

        handleSendMsgBtnOnClickEvent();

        return inflatedView;
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) inflatedView.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        if(profile != null) name = profile.getFirstName();
        displayName(profile);
    }

    private void displayName(Profile profile) {
        if (profile != null) {
            greetingsTextView.setText("Witaj " + profile.getFirstName() + "! Podziel się swoimi wrażeniami!");
        }
    }

    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    private void showMessagePanel() {
        greetingsTextView.setText("Witaj " + name + "! Podziel się swoimi wrażeniami!");
        greetingsTextView.setVisibility(View.VISIBLE);
        inflatedView.findViewById(R.id.msgPanel).setVisibility(View.VISIBLE);
        inflatedView.findViewById(R.id.buttonMsgSend).setVisibility(View.VISIBLE);
        inflatedView.findViewById(R.id.shareExperienceText).setVisibility(View.INVISIBLE);
    }

    private void hideMessagePanel() {
        inflatedView.findViewById(R.id.msgPanel).setVisibility(View.INVISIBLE);
        inflatedView.findViewById(R.id.buttonMsgSend).setVisibility(View.INVISIBLE);
        inflatedView.findViewById(R.id.shareExperienceText).setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        greetingsEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    new SendMessage().execute(name, greetingsEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private class SendMessage extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return postMsg(params[0], params[1]);
        }

        private boolean postMsg(String author, String msg) {
            try {
                URL url = new URL("http://jbosswildfly-juwenaliapw.rhcloud.com/rest/add");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(10000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put("author", author);
                dataMap.put("message", msg);
                String data = new GsonBuilder().create().toJson(dataMap, HashMap.class);
                OutputStream os = conn.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bw.write(data);
                bw.close();
                os.close();

                conn.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    private void initializeSendMsgBtn() {
        sendMsgBtn = (FloatingActionButton) inflatedView.findViewById(R.id.buttonMsgSend);
    }

    private void initializeMessageEditText() {
        greetingsEditText = (EditText) inflatedView.findViewById(R.id.greetings);
    }

    private void handleSendMsgBtnOnClickEvent() {
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendMessageIfNoErrors(greetingsEditText);
                } catch (EditTextException e) {
                    makeToast(e.getMessage());
                }
            }
        });
    }

    private void sendMessageIfNoErrors(EditText editText) throws EditTextException {
        if (editText.getText().toString().matches(""))
            throw new EditTextException("Wiadomość nie może być pusta.");
        else if (editText.getText().toString().length() > 200)
            throw new EditTextException("Wiadomość jest zbyt długa");
        else {
            sendMessage(editText.getText().toString());
            clearEditText(editText);
            makeToast("Wiadomość została wysłana!");
        }

    }

    private void sendMessage(String message) {
        new SendMessage().execute(name, message);

    }

    private void clearEditText(EditText editText) {
        editText.setText("");
    }

    private void makeToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}

class EditTextException extends Exception {

    public EditTextException() {

    }

    public EditTextException(String message) {
        super(message);
    }
};


