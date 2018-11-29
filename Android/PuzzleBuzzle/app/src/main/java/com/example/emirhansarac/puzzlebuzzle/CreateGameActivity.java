package com.example.emirhansarac.puzzlebuzzle;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGameActivity extends AppCompatActivity {


    private ImageView[] images;
    private Button goback;

    private String newString;
    private int p_;

    private long mLastClickTime = 0;

    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Bundle bundle = getIntent().getExtras();

        newString = "";
        String point;
        p_=0;

        if (bundle != null) {
            newString = bundle.getString("name");
            p_ = bundle.getInt("point");
        }

        point= ""+ p_;


        images = new ImageView[9];
        images[0] = (ImageView) findViewById(R.id.map_1);
        images[1] = (ImageView) findViewById(R.id.map_2);
        images[2] = (ImageView) findViewById(R.id.map_3);
        images[3] = (ImageView) findViewById(R.id.map_4);
        images[4] = (ImageView) findViewById(R.id.map_5);
        images[5] = (ImageView) findViewById(R.id.map_6);
        images[6] = (ImageView) findViewById(R.id.map_7);
        images[7] = (ImageView) findViewById(R.id.map_8);
        images[8] = (ImageView) findViewById(R.id.map_9);

        goback = (Button) findViewById(R.id.button2);

        images[0].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 1);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        images[1].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 2);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        images[2].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 3);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        images[3].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 4);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        images[4].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 5);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        images[5].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 6);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        images[6].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 7);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        images[7].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 8);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        images[8].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, LobbyActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("map", 9);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

        goback.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        Intent intent = new Intent(CreateGameActivity.this, MenuActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );
    }
}
