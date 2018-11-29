package com.example.emirhansarac.puzzlebuzzle;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private TextView username;
    private TextView score;

    private ImageButton new_game_button;
    private ImageButton quit_button;

    private String newString;
    private int p_;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        username = (TextView) findViewById(R.id.menu_name);
        score = (TextView) findViewById(R.id.menu_score);

        new_game_button = (ImageButton) findViewById(R.id.new_game_button);
        quit_button = (ImageButton) findViewById(R.id.quit_button);


        Bundle bundle = getIntent().getExtras();
        newString = "";
        String point;
        p_ = 0;

        if (bundle != null) {
            newString = bundle.getString("name");
            p_ = bundle.getInt("point");
        }
        point = ""+ p_;

        username.setText(newString);
        score.setText(point);

        quit_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );


        new_game_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuActivity.this, CreateGameActivity.class);
                        intent.putExtra("name", newString);
                        intent.putExtra("point", p_);
                        startActivity(intent);
                    }
                }
        );

    }
}
