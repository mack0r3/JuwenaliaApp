package com.mpier.juvenaliaapp;

import android.os.AsyncTask;
import android.os.Bundle;
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

    private static final String PARAM_NAME = "name";

    private String name = "";

    public TelebimFragment() {}

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
        if(getArguments() != null) {
            name = getArguments().getString(PARAM_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        getActivity().setTitle(R.string.menu_telebim);

        inflatedView = inflater.inflate(R.layout.fragment_telebim, container, false);

        initializeSendMsgBtn();
        initializeMessageEditText();

        handleSendMsgBtnOnClickEvent();

        return inflatedView;
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
        sendMsgBtn = (FloatingActionButton)inflatedView.findViewById(R.id.buttonMsgSend);
    }

    private void initializeMessageEditText(){
        greetingsEditText = (EditText) inflatedView.findViewById(R.id.greetings);
    }

    private void handleSendMsgBtnOnClickEvent(){
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

    private void sendMessageIfNoErrors(EditText editText) throws EditTextException{
        if(editText.getText().toString().matches(""))
            throw new EditTextException("Wiadomość nie może być pusta.");
        else if(editText.getText().toString().length() > 200)
            throw new EditTextException("Wiadomość jest zbyt długa");
        else {
            sendMessage(editText.getText().toString());
            clearEditText(editText);
            makeToast("Wiadomość została wysłana!");
        }

    }

    private void sendMessage(String message){
        new SendMessage().execute(name, message);

    }

    private void clearEditText(EditText editText){
        editText.setText("");
    }

    private void makeToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}

class EditTextException extends Exception{

    public EditTextException(){

    }

    public EditTextException(String message){
        super(message);
    }
};


