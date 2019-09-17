package com.example.whack_a_mole2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Cell {

    private ImageView mole;
    private ImageView hole;
    private ImageView bomb;
    private ImageView boom;


    private GridLayout gameGrid;
    private RelativeLayout cell;
    private LinearLayout linearLayout;


    public Cell(Context context, GridLayout gridLayout ) {
        this.mole = new ImageView(context);
        mole.setImageResource(R.drawable.small_mole);
        mole.setTranslationY(70);
        mole.setAlpha(0f);

        this.hole = new ImageView(context);
        hole.setImageResource(R.drawable.small_hole);

        this.bomb = new ImageView(context);
        bomb.setImageResource(R.drawable.bomb);
        bomb.setTranslationY(70);
        bomb.setVisibility(View.INVISIBLE);

        this.boom = new ImageView(context);
        boom.setImageResource(R.drawable.boom);
        boom.setTranslationY(70);
        boom.setVisibility(View.INVISIBLE);


        this.gameGrid = gridLayout;
        this.cell = new RelativeLayout(context);
        this.linearLayout = new LinearLayout(context);
        this.linearLayout.setOrientation(LinearLayout.VERTICAL);


        linearLayout.addView(mole);
        linearLayout.addView(hole);
        cell.addView(bomb);
        cell.addView(boom);

        cell.addView(linearLayout);
    }


    public ImageView getBomb(){
        return this.bomb;
    }

    public ImageView getMole(){
        return this.mole;
    }


    public void addCellToGrid(){
        this.gameGrid.addView(cell);
    }

    public void showMoleOrBomb(final int moleOrBomb){
        ImageView view ;

        if(moleOrBomb == Util.BOMB) {
            view = this.bomb;
        }else{
            view = this.mole;
        }

        view.setVisibility(View.VISIBLE);
        float buttom=50,top=-10;
        ObjectAnimator jump = ObjectAnimator.ofFloat(view, "translationY",   buttom,top,top,buttom);
        jump.setDuration(2000);

        ObjectAnimator show = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f, 1f, 0f);
        show.setDuration(2000);

        //setAnimator
        AnimatorSet set = new AnimatorSet();
        set.play(show).with(jump);
        set.start();

        final ImageView finalView = view;
        jump.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(moleOrBomb == Util.BOMB){
                    finalView.setVisibility(View.INVISIBLE);
                    mole.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationEnd(animation);
                if(moleOrBomb == Util.BOMB) {
                    finalView.setVisibility(View.INVISIBLE);
                    mole.setVisibility(View.VISIBLE);
                }
            }

        });

    }


    public ImageView getBoom() {
        return this.boom;
    }
}
