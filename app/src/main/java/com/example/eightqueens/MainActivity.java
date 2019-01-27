package com.example.eightqueens;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
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
import com.example.eightqueens.Options;
import com.example.eightqueens.Pos;


public class MainActivity extends AppCompatActivity {

    protected boolean[][] bgrid;
    protected ImageButton[][] buttons;
    protected boolean invalid_move;
    protected boolean stop;
    protected Pos[] options;
    //idea
    //list of pairs that you push and pop

    //idea:
    //hard code the 92 different options and have selector iterate through list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize game variables
        bgrid= new boolean[8][8];
        buttons= new ImageButton[8][8];
        invalid_move=false;
        stop=false;
        stop=false;

        //initialize options array
        ;

        //pull linear layout
        ViewGroup linearLayout = (ViewGroup) findViewById(R.id.layout);

        //pull grid layout
        GridLayout grid= (GridLayout) findViewById(R.id.grid);

        //pull message box
        TextView message= (TextView) findViewById(R.id.message_box);

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

                final int row=i;
                final int col=j;

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        check_and_update(row, col);
                    }
                });

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

    protected boolean check_array(boolean[]b){
        for(int i=0;i<b.length;i++){
            if(b[i]){
                return true;
            }
        }
        return false;
    }

    protected void give_up(View v){
        boolean [][] solution=new boolean[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                solution[i][j]=bgrid[i][j];
            }
        }
        solve(0,solution);
        draw_board(solution);
        TextView message= (TextView) findViewById(R.id.message_box);
        message.setText("You have given up");
    }


    protected boolean solve(int row, boolean[][]grid){
        if(row>=grid.length){
            return true;
        }


        for(int i=0;i<grid.length;i++){

            //check if valid and then return solve
            if(solve_check(row, i,grid)){
                grid[row][i]=true;

                boolean temp=solve(row+1,grid);
                if(temp){
                    return true;
                }else{
                    grid[row][i]=false;
                }
            }
        }

        if(check_array(grid[row])){
            return solve(row+1, grid);
        }


        return false;
    }

    protected boolean solve_check(int x, int y, boolean[][] grid){

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(i==x){
                    if(grid[i][j]) {
                        return false;
                    }
                }else if(j==y){
                    if(grid[i][j]) {
                        return false;
                    }
                }else if(y-x==j-i){
                    //row-col matches diagonal going from top left to bottom right
                    if(grid[i][j]) {
                        return false;
                    }
                }else if(y+x==j+i){
                    //row+col matches diagonal going from bottom left up to top right
                    if(grid[i][j]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }



    protected void xout(int x, int y){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(!bgrid[i][j]){
                    if(i==x){
                        buttons[i][j].setBackgroundResource(R.color.cran);
                    }else if(j==y){
                        buttons[i][j].setBackgroundResource(R.color.cran);
                    }else if(y-x==j-i){
                        buttons[i][j].setBackgroundResource(R.color.cran);
                    }else if(y+x==j+i){
                        buttons[i][j].setBackgroundResource(R.color.cran);
                    }
                }

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
            message.setText("You have unselected a queen.");
            draw_and_set_tile(x,y,false);

        }else if(check_place(x, y)){
            draw_and_set_tile(x,y,true);
           // xout(x, y);
            TextView message= (TextView) findViewById(R.id.message_box);
            if(this.count_queens()<8){
                int remaining=8-this.count_queens();
                message.setText("Good move! Only "+ remaining+ " more Queens to place!");
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
        TextView message= (TextView) findViewById(R.id.message_box);
        message.setText("");
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
