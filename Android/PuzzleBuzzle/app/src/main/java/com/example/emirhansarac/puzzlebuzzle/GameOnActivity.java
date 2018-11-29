package com.example.emirhansarac.puzzlebuzzle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import static java.lang.Math.abs;

public class GameOnActivity extends AppCompatActivity {

    ArrayList<Bitmap> pieces;
    ImageView[][] image_pieces;
    ImageView[] hint_pieces;

    ArrayList<Integer> pieces_not_found = new ArrayList<Integer>();

    ImageView imageView;
    Button refresh;

    int lastClick = -1;
    int[] hint_hold = new int[4];

    private String newString;
    private int p_;
    private int map_;
    private int game_ID;

    private TextView name_me;
    private TextView name_op;
    private TextView score_me;
    private TextView score_op;

    Semaphore end = new Semaphore(0);

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_on);

        Bundle bundle = getIntent().getExtras();

        newString = "";
        String point;
        map_ = 0;
        p_ = 0;
        game_ID = 0;
        if (bundle != null) {
            newString = bundle.getString("name");
            p_ = bundle.getInt("point");
            map_ = bundle.getInt("map");
            game_ID = bundle.getInt("id");
        }
        point = "" + p_;

        refresh = findViewById(R.id.refresh_button);
        imageView = findViewById(R.id.real_image);

        name_me = findViewById(R.id.player_me);
        name_op = findViewById(R.id.player_opponent);
        score_me = findViewById(R.id.me_score);
        score_op = findViewById(R.id.opponent_score);

        name_me.setText(newString);

        initialize_image_pieces();
        initialize_initial_image(map_);

        /*
        hint_pieces[0].setImageBitmap(pieces.get(0));
        hint_pieces[1].setImageBitmap(pieces.get(1));
        hint_pieces[2].setImageBitmap(pieces.get(2));
        hint_pieces[3].setImageBitmap(pieces.get(3));
        */

        imageView.post(new Runnable() {
            @Override
            public void run() {
                pieces = splitImage();
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        image_pieces[i][j].setImageBitmap(pieces.get(4 * i + j));
                        image_pieces[i][j].setAlpha((float) 0.2);

                    }
                }
                imageView.setVisibility(View.GONE);
                refresh_hints();
            }
        });

        refresh.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh_hints();
                    }
                }
        );

        hint_pieces[0].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick != -1)
                            hint_pieces[lastClick].setAlpha((float) 1);
                        lastClick = 0;
                        hint_pieces[0].setAlpha((float) 0.5);
                    }
                }
        );

        hint_pieces[1].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick != -1)
                            hint_pieces[lastClick].setAlpha((float) 1);
                        lastClick = 1;
                        hint_pieces[1].setAlpha((float) 0.5);
                    }
                }
        );

        hint_pieces[2].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick != -1)
                            hint_pieces[lastClick].setAlpha((float) 1);
                        lastClick = 2;
                        hint_pieces[2].setAlpha((float) 0.5);
                    }
                }
        );

        hint_pieces[3].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick != -1)
                            hint_pieces[lastClick].setAlpha((float) 1);
                        lastClick = 3;
                        hint_pieces[3].setAlpha((float) 0.5);
                    }
                }
        );

        image_pieces[0][0].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 0) {
                            boolean can = callInsertion(0);
                            //  right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }

                    }
                }
        );

        image_pieces[0][1].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 1) {
                            boolean can = callInsertion(1);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[0][2].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 2) {
                            boolean can = callInsertion(2);
                            // right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[0][3].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 3) {
                            boolean can = callInsertion(3);
                            //      right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[1][0].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 4) {
                            boolean can = callInsertion(4);
                            //   right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[1][1].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 5) {
                            boolean can = callInsertion(5);
                            //   right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[1][2].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 6) {
                            boolean can = callInsertion(6);
                            //  right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[1][3].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 7) {
                            boolean can = callInsertion(7);
                            //      right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[2][0].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 8) {
                            boolean can = callInsertion(8);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[2][1].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 9) {
                            boolean can = callInsertion(9);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[2][2].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 10) {
                            boolean can = callInsertion(10);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[2][3].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 11) {
                            boolean can = callInsertion(11);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[3][0].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 12) {
                            boolean can = callInsertion(12);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[3][1].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 13) {
                            boolean can = callInsertion(13);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[3][2].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 14) {
                            boolean can = callInsertion(14);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        image_pieces[3][3].setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastClick == -1) {

                        } else if (hint_hold[lastClick] == 15) {
                            boolean can = callInsertion(15);
                            //right_guess(hint_hold[lastClick]);
                        } else {
                            wrong_guess(lastClick);
                        }
                    }
                }
        );

        listen_opponent_moves();
    }

    private boolean callInsertion(int hint) {
        final int hint_ = hint;
        new Thread() {
            private Socket socket;
            private BufferedReader inFromServer;
            private BufferedWriter outToServer;
            JSONObject message = null;


            @Override
            public void run() {
                JSONObject ret = null;
                try {
                    socket = new Socket(Helper.IP, Helper.PORT);
                    inFromServer = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    outToServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ret = new JSONObject();
                try {
                    ret.put("name", newString);
                    ret.put("id", game_ID);
                    ret.put("hint", hint_);
                    ret.put("function", "putpiece");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    outToServer.write(ret.toString());
                    outToServer.newLine();
                    outToServer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    ret = new JSONObject(inFromServer.readLine());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if (ret.getString("success").equals("1")) {
                        insertPiece(hint_);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                shutDownConnections();
            }

            public void insertPiece(int p_n) {
                final int pn = p_n;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        right_guess(pn);
                        int k = Integer.parseInt(score_me.getText().toString()) + 1;
                        score_me.setText("" + k);
                        if (pieces_not_found.size() == 0) {
                            Intent intent = new Intent(GameOnActivity.this, GameOverActivity.class);
                            intent.putExtra("name", newString);
                            intent.putExtra("point", p_);
                            intent.putExtra("op_name", name_op.getText().toString());
                            intent.putExtra("op_score", Integer.parseInt(score_op.getText().toString()));
                            intent.putExtra("score", Integer.parseInt(score_me.getText().toString()));
                            intent.putExtra("id", game_ID);
                            startActivity(intent);
                        }

                    }
                });

            }

            protected void shutDownConnections() {
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

        }.start();
        return true;
    }

    private void listen_opponent_moves() {

        new Thread() {
            private Socket socket;
            private BufferedReader inFromServer;
            private BufferedWriter outToServer;
            JSONObject message = null;

            @Override
            public void run() {
                JSONObject ret = null;
                try {
                    socket = new Socket(Helper.IP, Helper.PORT);
                    inFromServer = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    outToServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ret = new JSONObject();
                try {
                    ret.put("name", newString);
                    ret.put("id", game_ID);
                    ret.put("function", "setConnection");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    outToServer.write(ret.toString());
                    outToServer.newLine();
                    outToServer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    ret = new JSONObject(inFromServer.readLine());
                    final String en = ret.getString("enemy");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name_op.setText(en);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if (!ret.getString("success").equals("1"))
                        return;

                    while (pieces_not_found.size() != 0) {
                        try {
                            String f = inFromServer.readLine();
                            if (f != null && f != "") {
                                ret = new JSONObject(f);
                                pieceInserted(ret);
                                end.acquire();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                shutDownConnections();

                Intent intent = new Intent(GameOnActivity.this, GameOverActivity.class);
                intent.putExtra("name", newString);
                intent.putExtra("point", p_);
                intent.putExtra("op_name", name_op.getText().toString());
                intent.putExtra("op_score", Integer.parseInt(score_op.getText().toString()));
                intent.putExtra("score", Integer.parseInt(score_me.getText().toString()));
                intent.putExtra("id", game_ID);
                startActivity(intent);

            }

            private void pieceInserted(JSONObject ret) {
                message = ret;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int hint = 0;
                        try {
                            hint = message.getInt("hint");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int pos = pieces_not_found.indexOf(hint);
                        pieces_not_found.remove(pos);
                        image_pieces[(hint / 4)][(hint % 4)].setAlpha((float) 1);
                        image_pieces[(hint / 4)][(hint % 4)].setEnabled(false);

                        end.release();

                        if (pieces_not_found.size() < 4) {
                            hint_pieces[pieces_not_found.size()].setImageResource(R.drawable.ic_dashboard_black_24dp);
                            hint_pieces[pieces_not_found.size()].setEnabled(false);
                        }
                        score_op.setText("" + (Integer.parseInt(score_op.getText().toString()) + 1));
                        refresh_hints();
                        //puan artır.

                    }
                });
            }


            protected void shutDownConnections() {
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

        }.start();
    }

    /*
        private void callx() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    score_me.setText(""+12);
                }
            });
        }
        */
    private ArrayList<Bitmap> splitImage() {
        int piecesNumber = 12;
        int rows = 4;
        int cols = 4;

        ImageView imageView = findViewById(R.id.real_image);
        ArrayList<Bitmap> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the with and height of the pieces
        int pieceWidth = croppedImageWidth / cols;
        int pieceHeight = croppedImageHeight / rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                pieces.add(Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight));
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }

    private int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public void initialize_image_pieces() {
        image_pieces = new ImageView[4][4];
        image_pieces[0][0] = findViewById(R.id.image1_1);
        image_pieces[0][1] = findViewById(R.id.image1_2);
        image_pieces[0][2] = findViewById(R.id.image1_3);
        image_pieces[0][3] = findViewById(R.id.image1_4);
        image_pieces[1][0] = findViewById(R.id.image2_1);
        image_pieces[1][1] = findViewById(R.id.image2_2);
        image_pieces[1][2] = findViewById(R.id.image2_3);
        image_pieces[1][3] = findViewById(R.id.image2_4);
        image_pieces[2][0] = findViewById(R.id.image3_1);
        image_pieces[2][1] = findViewById(R.id.image3_2);
        image_pieces[2][2] = findViewById(R.id.image3_3);
        image_pieces[2][3] = findViewById(R.id.image3_4);
        image_pieces[3][0] = findViewById(R.id.image4_1);
        image_pieces[3][1] = findViewById(R.id.image4_2);
        image_pieces[3][2] = findViewById(R.id.image4_3);
        image_pieces[3][3] = findViewById(R.id.image4_4);

        hint_pieces = new ImageView[4];
        hint_pieces[0] = findViewById(R.id.hint1);
        hint_pieces[1] = findViewById(R.id.hint2);
        hint_pieces[2] = findViewById(R.id.hint3);
        hint_pieces[3] = findViewById(R.id.hint4);

        for (int i = 0; i < 16; i++) {
            pieces_not_found.add(i);
        }

        for (int i = 0; i < 4; i++)
            hint_hold[i] = -1;
    }

    public void initialize_initial_image(int map) {
        if (map == 1)
            imageView.setImageResource(R.drawable.map_1);
        else if (map == 2)
            imageView.setImageResource(R.drawable.map_2);
        else if (map == 3)
            imageView.setImageResource(R.drawable.map_3);
        else if (map == 4)
            imageView.setImageResource(R.drawable.map_4);
        else if (map == 5)
            imageView.setImageResource(R.drawable.map_5);
        else if (map == 6)
            imageView.setImageResource(R.drawable.map_6);
        else if (map == 7)
            imageView.setImageResource(R.drawable.map_7);
        else if (map == 8)
            imageView.setImageResource(R.drawable.map_8);
        else if (map == 9)
            imageView.setImageResource(R.drawable.map_9);
    }

    public void refresh_hints() {
        if (lastClick != -1) {
            hint_pieces[lastClick].setAlpha((float) 1);
            lastClick = -1;
            refresh_hints();
        }

        int h_n = pieces_not_found.size();
        if (h_n >= 4)
            h_n = 4;
        ArrayList<Integer> t = new ArrayList<Integer>();
        for (int i = 0; i < h_n; i++) {
            Random r = new Random();
            int p = r.nextInt(pieces_not_found.size());
            if (t.contains(p))
                i--;
            else {
                t.add(p);
                hint_pieces[i].setImageBitmap(pieces.get(pieces_not_found.get(p)));
                hint_hold[i] = pieces_not_found.get(p);
            }
        }
    }

    public void wrong_guess(int hint) {
        if (hint != -1) {
            hint_pieces[lastClick].setAlpha((float) 1);
            lastClick = -1;
            refresh_hints();
        }
    }

    public void right_guess(int hint) {
        int pos = pieces_not_found.indexOf(hint);
        pieces_not_found.remove(pos);
        image_pieces[(hint / 4)][(hint % 4)].setAlpha((float) 1);
        image_pieces[(hint / 4)][(hint % 4)].setEnabled(false);
        wrong_guess(hint);
        if (pieces_not_found.size() < 4) {
            hint_pieces[pieces_not_found.size()].setImageResource(R.drawable.ic_dashboard_black_24dp);
            hint_pieces[pieces_not_found.size()].setEnabled(false);
        }
    }

}