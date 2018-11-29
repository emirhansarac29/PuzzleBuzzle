package com.example.emirhansarac.puzzlebuzzle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
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

public class LoginActivity extends AppCompatActivity {

    private class LoginAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject message;
        private Socket socket;
        BufferedReader inFromServer;
        BufferedWriter outToServer;

        LoginAsyncTask(JSONObject message_) {
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
                    login_success(ret);
                } else {
                    login_failed(ret);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private EditText username;
    private EditText password;
    private TextView signup_button;
    private Button login_button;
    private long mLastClickTime = 0;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = (EditText) findViewById(R.id.edit_text_username);
        password = (EditText) findViewById(R.id.edit_text_password);

        login_button = (Button) findViewById(R.id.button_login);
        signup_button = (TextView) findViewById(R.id.link_signup);

        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        JSONObject message = new JSONObject();
                        try {
                            message.put("name", username.getText().toString());
                            message.put("password", password.getText().toString());
                            message.put("function", "log_in");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        new LoginAsyncTask(message).execute();
                    }
                }
        );

        signup_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }

    public void login_success(JSONObject info) {
        Toast.makeText(LoginActivity.this, "Username and password is correct",
                Toast.LENGTH_SHORT).show();

        String name_ = "";
        int point_ = 0;
        try {
            name_ = info.getString("name");
            point_ = info.getInt("point");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra("name", name_);
        intent.putExtra("point", point_);
        startActivity(intent);
    }

    public void login_failed(JSONObject info) {
        Toast.makeText(LoginActivity.this, "Username and password is NOT correct",
                Toast.LENGTH_SHORT).show();
        username.setText("");
        password.setText("");
    }

}
