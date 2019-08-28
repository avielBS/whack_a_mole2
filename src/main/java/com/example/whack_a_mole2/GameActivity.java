package com.example.whack_a_mole2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    private GridLayout gridGame;

    Cell[] cells;
    private Timer timer;
    private final int ROWS = 3;
    private final int COLUMNS = 3;

    private int score=0;
    private  int miss=0;

    private TextView textScore;
    private TextView textMiss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gridGame = createGridLayout(3,3);
        cells = new Cell[9];
        timer=new Timer();

        for (int i = 0; i < 9; i++) {
            cells[i] = new Cell(this.getApplicationContext(),gridGame);
            cells[i].addCellToGrid();
            setMoleAndBombListeners(cells[i]);
        }

        RelativeLayout gameBoard = findViewById(R.id.game__layout);
        textMiss = findViewById(R.id.miss_txt);
        textScore = findViewById(R.id.score_txt);

        gameBoard.addView(gridGame);


        final Handler animationHandler = new Handler();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                animationHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final int index = (int) (Math.random() * (ROWS * COLUMNS));
                        final int bombOrMole = (int) (Math.random() * (2));

                        cells[index].showMoleOrBomb(bombOrMole);

                    }
                });
                }
            }, 200, 2000);


    }

    private void setMoleAndBombListeners(Cell cell) {
        setMoleListener(cell.getMole());
        setBombListener(cell.getBomb());
    }

    private void setMoleListener(ImageView mole) {

        mole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getAlpha() > 0.3){
                    score ++;
                    textScore.setText(""+score);
                }
                else
                    miss ++;
                    textMiss.setText(""+miss);
            }
        });
    }

    private void setBombListener(ImageView bomb) {
        bomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getAlpha() > 0.3){
                    score -= 3;
                    miss += 1;
                    textScore.setText(""+score);
                    textMiss.setText(""+miss);
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






}
