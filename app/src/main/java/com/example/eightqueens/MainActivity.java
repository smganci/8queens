package com.example.eightqueens;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    protected boolean[][] bgrid;
    protected ImageButton[][] buttons;
    protected boolean invalid_move;
    protected boolean stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgrid= new boolean[8][8];
        buttons= new ImageButton[8][8];
        invalid_move=false;
        stop=false;
        stop=false;
        ViewGroup linearLayout = (ViewGroup) findViewById(R.id.layout);
        GridLayout grid= (GridLayout) findViewById(R.id.grid);

        //determine cell size
        int displaywidth=getResources().getDisplayMetrics().widthPixels;
        int cell_size=displaywidth/8 ;

        for(int i=0; i< 8;i++){
            for(int j=0;j<8;j++){

                //create button
                final ImageButton button= new ImageButton(getApplicationContext());
                buttons[i][j]=button;
                //set initial background based on row and column
                draw_tile(i,j);

                final int i_point=i;
                final int j_point=j;

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        check_and_update(i_point,j_point);
                    }
                });

//
//
//                if(j%2==0 && i%2==0 ||j%2==1&&i%2==1){
//                    button.setBackgroundResource(R.drawable.lp_tile);
//                    final int i_point=i;
//                    final int j_point=j;
//
//                    button.setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v){
//
//                            Toast.makeText(MainActivity.this, "TEST", Toast.LENGTH_SHORT);
//                                if(bgrid[i_point][j_point]){
//                                    v.setBackgroundResource(R.drawable.lp_tile);
//                                    bgrid[i_point][j_point]=false;
//                                }else{
//                                    if(check_place(i_point,j_point)){
//                                        v.setBackgroundResource(R.drawable.lp_queen);
//                                        bgrid[i_point][j_point]=true;
//                                    }else{
//                                        //if place is valid, play
//                                        if(check_place(i_point,j_point)){
//                                            v.setBackgroundResource(R.drawable.dp_queen);
//                                            bgrid[i_point][j_point]=true;
//                                        }else{
//                                            Toast.makeText(MainActivity.this, "INVALID", Toast.LENGTH_SHORT);
//                                        }
//                                    }
//                                }
//                        }
//                    });
//
//
//                }else {
//                    button.setBackgroundResource(R.drawable.dp_tile);
//                    final int i_point=i;
//                    final int j_point=j;
//
//                    button.setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v){
//
//                                if(bgrid[i_point][j_point]){
//                                    v.setBackgroundResource(R.drawable.dp_tile);
//                                    bgrid[i_point][j_point]=false;
//                                }else{
//                                    //if place is valid, play
//                                    if(check_place(i_point,j_point)){
//                                        v.setBackgroundResource(R.drawable.dp_queen);
//                                        bgrid[i_point][j_point]=true;
//                                    }else{
//                                        Toast.makeText(MainActivity.this, "INVALID", Toast.LENGTH_SHORT);
//                                    }
//                                }
//                        }
//                    });
//
//                }
                grid.addView((button));



                //set size of button
                button.setScaleType(ImageView.ScaleType.FIT_XY);
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.height=cell_size;
                layoutParams.width=cell_size;
                button.setLayoutParams(layoutParams);


                button.setTag("@+/"+i+"_"+j);
                //set button changing functionality

            }


        }


    }


    protected void draw_and_set_tile(int i, int j, boolean queen){
        bgrid[i][j]=queen;
        draw_tile(i,j);
    }


    protected void draw_tile(int i, int j){
        if(j%2==0 && i%2==0 ||j%2==1&&i%2==1) {
            if(bgrid[i][j]){
                buttons[i][j].setBackgroundResource(R.drawable.lp_queen);
            }else {
                buttons[i][j].setBackgroundResource(R.drawable.lp_tile);
            }
        }else{
            if(bgrid[i][j]){
                buttons[i][j].setBackgroundResource(R.drawable.dp_queen);
            }else {
                buttons[i][j].setBackgroundResource(R.drawable.dp_tile);
            }
        }

    }

    protected void draw_board(boolean[][]answer){
        this.bgrid=answer;

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                draw_tile(i,j);
            }
        }


    }

    //check place ensures that the position at x, y is a valid move by checking columns, rows and diagonals
    protected boolean check_place(int x, int y){

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(i==x){
                    if(bgrid[i][j]) {
                        return false;
                    }
                }else if(j==y){
                    if(bgrid[i][j]) {
                        return false;
                    }
                }else if(y-x==j-i){
                    //row-col matches diagonal going from top left to bottom right
                    if(bgrid[i][j]) {
                        return false;
                    }
                }else if(y+x==j+i){
                    //row+col matches diagonal going from bottom left up to top right
                    if(bgrid[i][j]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void check_and_update(int x, int y){
        if(this.stop){
            return;
        }
        if(bgrid[x][y]){
            TextView message= (TextView) findViewById(R.id.message_box);
            message.setText("You have unbselected a queen");
            draw_and_set_tile(x,y,false);

        }else if(check_place(x, y)){
            draw_and_set_tile(x,y,true);
            TextView message= (TextView) findViewById(R.id.message_box);
            if(this.count_queens()<8){
                int remaining=8-this.count_queens();
                message.setText("Good Move! Only "+ remaining+ " more Queens to place!");
            }else{
                message.setText("You Win!!! Press \"Restart\" to play again!");
                this.stop=true;
            }

        }else{
            //get warning box and update for invalid move
            TextView message= (TextView) findViewById(R.id.message_box);
            message.setText("Invalid Move! Try again.");
        }
    }


    protected void restart(View v){
        this.stop=false;
        this.draw_board(new boolean[8][8]);
    }

    protected int count_queens(){
        int sum=0;
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(bgrid[i][j]){
                    sum++;
                }
            }
        }
        return sum;
    }


}
