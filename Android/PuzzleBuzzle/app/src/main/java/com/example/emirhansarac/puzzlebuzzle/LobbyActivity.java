package com.example.emirhansarac.puzzlebuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class LobbyActivity extends AppCompatActivity {

    private ProgressBar progress;
    private Button quit_button;

    private String newString;
    private int p_;
    private int map_;
    private int id_ = -1;
    private Semaphore lock = new Semaphore(0);
    private long mLastClickTime = 0;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        quit_button = (Button) findViewById(R.id.quit_button);
        progress = (ProgressBar) findViewById(R.id.waiting_bar);


        Bundle bundle = getIntent().getExtras();

        newString = "";
        String point;
        map_ = 0;
        p_ = 0;

        if (bundle != null) {
            newString = bundle.getString("name");
            p_ = bundle.getInt("point");
            map_ = bundle.getInt("map");
        }

        point = "" + p_;


        Thread waiting = new Thread() {

            private JSONObject message;
            private Socket socket;
            BufferedReader inFromServer;
            BufferedWriter outToServer;

            @Override
            public void run() {
                JSONObject ret;
                message = new JSONObject();

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
                    message.put("name", newString);
                    message.put("point", p_);
                    message.put("map", map_);
                    message.put("function", "waiting");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    outToServer.write(message.toString());
                    outToServer.newLine();
                    outToServer.flush();


                    ret = new JSONObject(inFromServer.readLine());
                    id_ = ret.getInt("id");
                    lock.release();

                    ret = new JSONObject(inFromServer.readLine());
                    while (ret.getInt("success") == 0) {
                        ret = new JSONObject(inFromServer.readLine());
                    }

                    Intent intent = new Intent(LobbyActivity.this, GameOnActivity.class);
                    intent.putExtra("name", newString);
                    intent.putExtra("point", p_);
                    intent.putExtra("map", map_);
                    intent.putExtra("id", ret.getInt("id"));
                    startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        waiting.start();


        //progress.setVisibility(View.GONE);

        quit_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        try {
                            lock.acquire();
                        } catch (InterruptedException e) {
                            return;
                        }

                        Thread waiting = new Thread() {

                            private JSONObject message;
                            private Socket socket;
                            BufferedReader inFromServer;
                            BufferedWriter outToServer;

                            @Override
                            public void run() {
                                JSONObject ret;
                                message = new JSONObject();

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
                                    message.put("id", id_);
                                    message.put("function", "delete");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    outToServer.write(message.toString());
                                    outToServer.newLine();
                                    outToServer.flush();
                                    lock.release();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        waiting.start();

                        try {
                            lock.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LobbyActivity.this, CreateGameActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("point", p_);
                        progress.setVisibility(View.GONE);
                        startActivity(intent);
                    }
                }
        );

    }
}
