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
import com.example.eightqueens.Solutions;
import com.example.eightqueens.Pos;


public class MainActivity extends AppCompatActivity {

    //bgrid: 2d boolean array representing placement of queens
    protected boolean[][] bgrid;

    //buttons: 2d array of references to ImageButtons
    protected ImageButton[][] buttons;

    //invalid_move: true if invalid_tile is placed
    protected boolean invalid_move;

    //stop: true if game has been won
    protected boolean stop;

    //s: holds Solutions object (runs backtracking algo to generate all possible solutions)
    protected Solutions s;

    //current: holds current solution number (used in display of next or prev solution)
    protected int current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create and set view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize game variables
        this.bgrid= new boolean[8][8];
        this.buttons= new ImageButton[8][8];
        this.invalid_move=false;
        this.stop=false;
        this.current=0;
        this.s=new Solutions();


        //pull linear layout
        ViewGroup linearLayout = (ViewGroup) findViewById(R.id.layout);

        //pull grid layout
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

                //set variables as final to access within button onclick listener
                final int row=i;
                final int col=j;

                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        //call check_and_update method to determine validity of move and update board
                        check_and_update(row, col);
                    }
                });

                //add button to grid
                grid.addView((button));

                //set size of button
                button.setScaleType(ImageView.ScaleType.FIT_XY);
                ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                layoutParams.height=cell_size;
                layoutParams.width=cell_size;
                button.setLayoutParams(layoutParams);
            }
        }
    }

    /////////////////////////////////////////////////
    //GENERAL UTILITY
    /////////////////////////////////////////////////

    //name: set_message
    //action: takes in a string and sets the message box to that text
    protected void set_message(String s){
        TextView message= (TextView) findViewById(R.id.message_box);
        message.setText(s);

    }

    //name: draw_and_set_tile
    //action: takes in coordinate and boolean, sets bgrid at coordinate to boolean, draws that tile
    protected void draw_and_set_tile(int i, int j, boolean queen){
        bgrid[i][j]=queen;
        draw_tile(i,j);
    }


    //name: draw_tile
    //action: takes in coordinate, depending on bgrid boolean value and coordinate, sets button at coordinate backgroudn image
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

    //name: draw_board
    //action: takes ina new boolean array, sets that to bgrid, redraws every tile on the board
    protected void draw_board(boolean[][]answer){
        this.bgrid=answer;

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                draw_tile(i,j);
            }
        }


    }

    //name: check_and_update
    //action: checks validity of placing queen at x and y coordinate--either updates or displays message of invalid
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

    //name: count queens
    //action: returns a count of all queens stored in bgrid
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



    ///////////////////////////////////////////////////////////////
    //BUTTON HANDLERS
    ///////////////////////////////////////////////////////////////

    //name: restart
    //action: resets state variables and board
    protected void restart(View v){
        this.current=0;
        this.stop=false;
        this.draw_board(new boolean[8][8]);
        this.set_message("");
    }

    //name: next
    //action: updates board to next solution
    protected void next(View v){
        this.draw_board(s.get_grid_index(this.current));
        this.set_message("Solution #"+current);
        this.current++;
        if(this.current>=92){
            this.current=0;
        }
    }

    //name: prev
    //action: updates board to prev solution
    protected void prev(View v){
        this.draw_board(s.get_grid_index(this.current));
        this.set_message("Solution #"+current);
        this.current--;
        if(this.current<0){
            this.current=91;
        }

    }

    //name: give_up
    //action: finds the closest solution to the current given solution
    protected void give_up(View v){
        boolean [][] solution=new boolean[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                solution[i][j]=bgrid[i][j];
            }
        }
      //  solve(0,solution);
        draw_board(solution);
        TextView message= (TextView) findViewById(R.id.message_box);
        message.setText("You have given up");
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

}
