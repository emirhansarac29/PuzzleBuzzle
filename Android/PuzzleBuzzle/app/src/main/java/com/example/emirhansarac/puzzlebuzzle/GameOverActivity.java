package com.example.emirhansarac.puzzlebuzzle;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class GameOverActivity extends AppCompatActivity {

    private class UpdateDBAsyncTask extends AsyncTask<Void, Void, JSONObject> {

        private JSONObject message;
        private Socket socket;
        BufferedReader inFromServer;
        BufferedWriter outToServer;

        UpdateDBAsyncTask(JSONObject message_) {
            this.message = message_;
        }

        protected JSONObject doInBackground(Void... voids) {
            JSONObject ret = new JSONObject();
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
                message.put("function", "updateScore");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                outToServer.write(message.toString());
                outToServer.newLine();
                outToServer.flush();
             //   ret = new JSONObject(inFromServer.readLine());
            } catch (IOException e) {
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
        }

    }

    String my_name;
    String enemy_name;
    int my_score;
    int enemy_score;
    int collected;
    int id;
    private long mLastClickTime = 0;
    ImageView winner;
    ImageView loser;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Bundle bundle = getIntent().getExtras();

        my_name = "";
        enemy_name = "";
        my_score = 0;
        enemy_score = 0;
        collected = 0;
        id = 0;
        if (bundle != null) {
            my_name = bundle.getString("name");
            enemy_name = bundle.getString("op_name");
            my_score = bundle.getInt("score");
            enemy_score = bundle.getInt("op_score");
            collected = bundle.getInt("point");
            id = bundle.getInt("id");
        }

        winner = findViewById(R.id.winner);
        loser = findViewById(R.id.loser);

        if (my_score == enemy_score) {
            winner.setImageResource(R.drawable.draw);
            loser.setImageResource(R.drawable.draw);
        } else if (my_score > enemy_score) {
            loser.setEnabled(false);
            loser.setVisibility(View.GONE);
        } else {
            winner.setEnabled(false);
            winner.setVisibility(View.GONE);
        }

        winner.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(GameOverActivity.this, MenuActivity.class);
                        intent.putExtra("name", my_name);
                        intent.putExtra("point", collected + my_score);
                        startActivity(intent);
                    }
                }
        );

        loser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(GameOverActivity.this, MenuActivity.class);
                        intent.putExtra("name", my_name);
                        intent.putExtra("point", collected + my_score);
                        startActivity(intent);
                    }
                }
        );

        JSONObject my = new JSONObject();

        try {
            my.put("name", my_name);
            my.put("point", collected + my_score);
            my.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new UpdateDBAsyncTask(my).execute();
    }
}
