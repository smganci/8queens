package com.example.eightqueens;

import android.widget.ImageButton;

public class Tile {

    private ImageButton button;
    private R.drawable tile_img;
    private R.drawable queen_img;
    private boolean on;

    public Tile(R.drawable t, R.drawable q){
        this.tile_img=t;
        this.queen_img=q;
        on=false;

    }

    public ImageButton getButton(){
        return button;
    }

}
