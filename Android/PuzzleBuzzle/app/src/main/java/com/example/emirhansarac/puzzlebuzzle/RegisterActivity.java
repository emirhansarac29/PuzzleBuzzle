package com.example.emirhansarac.puzzlebuzzle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class RegisterActivity extends AppCompatActivity {

    private class RegisterAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject message;
        private Socket socket;
        BufferedReader inFromServer;
        BufferedWriter outToServer;

        RegisterAsyncTask(JSONObject message_) {
            this.message = message_;
        }

        protected JSONObject doInBackground(Void... voids) {
            JSONObject ret = null;
            try {
                socket = new Socket(Helper.IP, Helper.PORT);
                outToServer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));
                inFromServer = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                outToServer.write(message.toString());
                outToServer.newLine();
                outToServer.flush();
                ret = new JSONObject(inFromServer.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ret;
        }

        protected void onPostExecute(JSONObject ret) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (inFromServer != null) {
                try {
                    inFromServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outToServer != null) {
                try {
                    outToServer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (ret.getString("success").equals("1")) {
                    register_success(ret);
                } else {
                    register_failed(ret);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private EditText username;
    private EditText password;
    private TextView backmenu;
    private Button register_button;
    private long mLastClickTime = 0;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.edit_text_username);
        password = (EditText) findViewById(R.id.edit_text_password);

        register_button = (Button) findViewById(R.id.button_register);
        backmenu = (TextView) findViewById(R.id.link_mainmenu);

        register_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        String un = username.getText().toString();
                        String pw = password.getText().toString();
                        if (un.length() < 6 || un.length() > 12 || pw.length() < 6 || pw.length() > 12) {
                            Toast.makeText(RegisterActivity.this, "Username or Password is invalid.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONObject message = new JSONObject();
                        try {
                            message.put("name", username.getText().toString());
                            message.put("password", password.getText().toString());
                            message.put("function", "register");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new RegisterAsyncTask(message).execute();
                    }
                }
        );

        backmenu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void register_success(JSONObject info) {
        Toast.makeText(RegisterActivity.this, "Account is created.",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void register_failed(JSONObject info) {
        Toast.makeText(RegisterActivity.this, "Username and password is not valid.",
                Toast.LENGTH_SHORT).show();
        username.setText("");
        password.setText("");
    }
}
