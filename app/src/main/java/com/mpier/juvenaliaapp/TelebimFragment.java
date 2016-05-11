package com.mpier.juvenaliaapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    EditText nameEditText;

    //private static final String PARAM_NAME = "name";

    //private String name = "";

    public TelebimFragment() {}

    public static TelebimFragment newInstance(String name) {
        TelebimFragment telebimFragment = new TelebimFragment();
        //Bundle b = new Bundle();
        //b.putString(PARAM_NAME, name);
        //telebimFragment.setArguments(b);
        return telebimFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        //if(getArguments() != null) {
        //name = getArguments().getString(PARAM_NAME);
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        getActivity().setTitle(R.string.menu_telebim);

        inflatedView = inflater.inflate(R.layout.fragment_telebim, container, false);

        initializeSendMsgBtn();
        initializeEditTexts();

        handleSendMsgBtnOnClickEvent();

        return inflatedView;
    }

    private void initializeSendMsgBtn() {
        sendMsgBtn = (FloatingActionButton) inflatedView.findViewById(R.id.buttonMsgSend);
    }

    private void initializeEditTexts() {
        greetingsEditText = (EditText) inflatedView.findViewById(R.id.greetings);
        nameEditText = (EditText) inflatedView.findViewById(R.id.name);
    }

    private void handleSendMsgBtnOnClickEvent(){
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendMessageIfNoErrors();
                } catch (EditTextException e) {
                    makeToast(e.getMessage());
                }
            }
        });
    }

    private void sendMessageIfNoErrors() throws EditTextException {
        if (greetingsEditText.getText().toString().matches(""))
            throw new EditTextException("Wiadomość nie może być pusta.");
        else if (greetingsEditText.getText().toString().length() > 200)
            throw new EditTextException("Wiadomość jest zbyt długa");
        else if (nameEditText.getText().toString().length() > 20)
            throw new EditTextException("Imię jest za długie");
        else if (nameEditText.getText().toString().length() < 3)
            throw new EditTextException("Imię jest za krótkie");
        else {
            sendMessage(nameEditText.getText().toString(), greetingsEditText.getText().toString());
            clearEditText(greetingsEditText);
            clearEditText(nameEditText);
        }

    }

    private void sendMessage(String name, String message) {
        new SendMessage().execute(name, message);

    }

    private void clearEditText(EditText editText){
        editText.setText("");
    }

    private void makeToast(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                makeToast("Wiadomość została wysłana!");
            } else {
                makeToast("Nie można wysłać wiadomości");
            }
        }
    }

}

class EditTextException extends Exception{

    public EditTextException(){

    }

    public EditTextException(String message){
        super(message);
    }
};


