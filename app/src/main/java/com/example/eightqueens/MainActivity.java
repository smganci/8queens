package com.example.eightqueens;

import android.content.DialogInterface;
import android.graphics.Point;
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
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    protected boolean[][] bgrid;
    protected ImageButton[][] buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bgrid= new boolean[8][8];
        buttons= new ImageButton[8][8];
        ViewGroup linearLayout = (ViewGroup) findViewById(R.id.layout);
        GridLayout grid= (GridLayout) findViewById(R.id.grid);

        //determine cell size
        int displaywidth=getResources().getDisplayMetrics().widthPixels;
        int cell_size=displaywidth/8 ;

        for(int i=0; i< 8;i++){
            for(int j=0;j<8;j++){

                //create button
                ImageButton button= new ImageButton(getApplicationContext());
                buttons[i][j]=button;

                if(j%2==0 && i%2==0 ||j%2==1&&i%2==1){
                    button.setBackgroundResource(R.drawable.lp_tile);
                    final int i_point=i;
                    final int j_point=j;

                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v){

                                if(bgrid[i_point][j_point]){
                                    v.setBackgroundResource(R.drawable.lp_tile);
                                    bgrid[i_point][j_point]=false;
                                }else{
                                    if(check_place(i_point,j_point)){
                                        v.setBackgroundResource(R.drawable.lp_queen);
                                        bgrid[i_point][j_point]=true;
                                    }else{
                                        Toast.makeText(MainActivity.this, "That move is invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        }
                    });


                }else {
                    button.setBackgroundResource(R.drawable.dp_tile);
                    final int i_point=i;
                    final int j_point=j;

                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v){

                                if(bgrid[i_point][j_point]){
                                    v.setBackgroundResource(R.drawable.dp_tile);
                                    bgrid[i_point][j_point]=false;
                                }else{
                                    //if place is valid, play
                                    if(check_place(i_point,j_point)){
                                        v.setBackgroundResource(R.drawable.dp_queen);
                                        bgrid[i_point][j_point]=true;
                                    }else{
                                        Toast.makeText(MainActivity.this, "That move is invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        }
                    });

                }
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

//    protected void update_tile(int i, int j){
//        View tile= (View) findViewWithTag(""+i+"_"+j);
//    }

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

//
//        //check columns
//        for(int i=0; i<8;i++){
//            if(bgrid[i][y]){
//                return false;
//            }
//        }
//
//        //check rows
//        for(int i=0; i<8;i++){
//            if(bgrid[x][i]){
//                return false;
//            }
//        }
//
//        //check diagonal \
//        for(int i=x,j=y;i>=0 &&j>=0; i--, j-- ){
//            if(bgrid[i][j]){
//                return false;
//            }
//        }
//
//        for(int i=x,j=y;i>=0 &&j<8; i--, j++ ){
//            if(bgrid[i][j]){
//                return false;
//            }
//        }
//
//        for(int i=x,j=y;i<8 &&j<8; i++, j++ ){
//            if(bgrid[i][j]){
//                return false;
//            }
//        }
//        for(int i=x,j=y;i<8 &&j>=0; i++, j-- ){
//            if(bgrid[i][j]){
//                return false;
//            }
//        }
//        return true;
    }

    //scan tiles will ensure logical check
    protected void scan_tiles(){
        for(int i=0;i<bgrid.length;i++){
            for(int j=0;j<bgrid[0].length;j++){

            }
        }
    }

    protected void restart(View v){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){

                bgrid[i][j]=false;
                if(j%2==0 && i%2==0 ||j%2==1&&i%2==1) {
                    buttons[i][j].setBackgroundResource(R.drawable.lp_tile);
                }else{
                    buttons[i][j].setBackgroundResource(R.drawable.dp_tile);
                }

            }
        }
    }


}