package com.example.whack_a_mole2;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private static final int WIN_SCORE = 30;
    private static final int MAX_MISS = 3;
    private final int GAME_DURACTION = 30;
    public static final int LOCATION_REQUEST = 101;

    private GridLayout gridGame;

    Cell[] cells;
    private Timer timer;
    private final int ROWS = 3;
    private final int COLUMNS = 3;

    private int score=0;
    private int miss=0;
    private int bombs=0;

    private long timeLeft;
    private CountDownTimer countDownTimer;

    private TextView textScore;
    private TextView textMiss;
    private TextView textViewCountDown;
    private String name;

    private Record record;
    private GoogleApiClient googleApiClient;
    Location playerLocation;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extars = getIntent().getExtras();
        name = extars.getString("PlayerName");

        getCurrentLocation();


        gridGame = createGridLayout(3,3);
        cells = new Cell[ROWS*COLUMNS];
        timer=new Timer();

        for (int i = 0; i < ROWS*COLUMNS; i++) {
            cells[i] = new Cell(this.getApplicationContext(),gridGame);
            cells[i].addCellToGrid();
            setMoleAndBombListeners(cells[i]);
        }

        RelativeLayout gameBoard = findViewById(R.id.game__layout);
        textMiss = findViewById(R.id.miss_txt);
        textScore = findViewById(R.id.score_txt);
        textViewCountDown = findViewById(R.id.time_txt);

        gameBoard.addView(gridGame);


        final Handler animationHandler = new Handler();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                animationHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final int index = (int) (Math.random() * (ROWS * COLUMNS));
                        final int bombOrMole = (int) (Math.random() * (4));

                        cells[index].showMoleOrBomb(bombOrMole);

                    }
                });
                }
            }, 200, 700);

            startCountDown();
    }




    private void getCurrentLocation() {
        //Checking if the location permission is granted
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Fetching location using FusedLOcationProviderAPI
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    playerLocation = location;
                    Log.d("location",""+ location.getLatitude() +"  " +  location.getLongitude());
                }
            }
        });



    }

    private void setMoleAndBombListeners(final Cell cell) {
        setMoleListener(cell.getMole());
        setBombListener(cell.getBomb(),cell.getBoom());
    }

    private void setMoleListener(ImageView mole) {

        mole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getAlpha() > 0.3){
                    score ++;
                    textScore.setText(" "+score);
                    ObjectAnimator rotate = ObjectAnimator.ofFloat(view,"rotation",0,360);
                    rotate.setDuration(500);
                    rotate.start();
                }
                else
                    miss ++;

                textMiss.setText(" "+miss);
                checkGameStatus();
            }
        });
    }

    private void setBombListener(final ImageView bomb, final ImageView boom) {
        bomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getAlpha() > 0.3){
                    if(score - 3 >= 0 )
                        score -= 3;
                    else
                        score = 0;
                    textScore.setText(" "+score);
                    bombs++;
                    boom.setVisibility(View.VISIBLE);
                    bomb.setVisibility(View.INVISIBLE);

                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(boom,"translationX",0,1);
                    scaleX.setDuration(800);

                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(boom,"translationY",0,1);
                    scaleY.setDuration(800);

                    AnimatorSet set = new AnimatorSet();
                    set.play(scaleX).with(scaleY);
                    set.start();

                    //vibrate
                    Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibe.vibrate(200);

                    set.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            boom.setVisibility(View.INVISIBLE);
                        }
                    });

                    checkGameStatus();

                }

            }
        });
    }

    private GridLayout createGridLayout(int rows, int columns) {
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(columns);
        gridLayout.setRowCount(rows);
        return gridLayout;
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(GAME_DURACTION * 1000, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateTextCountDown();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateTextCountDown();
                checkGameStatus();
            }

        }.start();

    }

    private void checkGameStatus() {
        if (score >= WIN_SCORE || timeLeft == 0 || miss == MAX_MISS) {
            timer.cancel();
            showStatusMessage();
            countDownTimer.cancel();
            saveRecord();
        }
    }

    private void showStatusMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "";
        if (this.score >= WIN_SCORE)
            message = this.name + " Win !";
        else if (this.score < WIN_SCORE || this.miss == 3)
            message = this.name + " Lose !";

        builder.setTitle("Status").setMessage(message);
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.show();
    }

    private void updateTextCountDown() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        if(timeLeft >10*1000)
            textViewCountDown.setText(String.format(Locale.getDefault(), "%2d:%2d", minutes, seconds));

        else {
            textViewCountDown.setText(String.format(Locale.getDefault(), "%2d", seconds));
            textViewCountDown.setTextColor(Color.RED);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this.textViewCountDown,"scaleX",0f,1f);
            scaleX.setDuration(800);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this.textViewCountDown,"scaleY",0f,1f);
            scaleY.setDuration(800);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(scaleX,scaleY);
            set.start();
        }
    }

    private void saveRecord(){
        if(playerLocation != null)
            this.record = new Record(name,(int)timeLeft,score,miss,bombs,playerLocation.getLatitude(),playerLocation.getLongitude());
        else
            this.record = new Record(name,(int)timeLeft,score,miss,bombs,0,0);

        databaseHelper =  new DatabaseHelper(this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                databaseHelper.addRecord(record);
                databaseHelper.keepOnly10Best();
                databaseHelper.close();
            }
        };

        Thread DBthread = new Thread(runnable);
        DBthread.start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(databaseHelper!=null)
            databaseHelper.close();
    }
}
