package com.example.simpleracer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {
    public int speed;
    public boolean crashed;
    Context context;
    int maxX;
    int maxY;
    int x;
    int y;
    Bitmap bitmap;

    Rect collisionDetection;

    public Player(Context context, int sizeX, int sizeY){
        this.context=context;

        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.lada);

        maxX=sizeX-bitmap.getWidth();
        maxY=sizeY-bitmap.getHeight();

        y=sizeY-200-bitmap.getHeight();
        x=sizeX/2 -bitmap.getWidth()/2;

        collisionDetection = new Rect(x, y, x+ bitmap.getWidth(), y+bitmap.getHeight());

    }

    public void update(int xMovement, int yMovement) {
        x+= xMovement;
        y+=yMovement;

        if(x<0){
            x=0;
        }else if (x>maxX){
            x=maxX;
        }

        if(y<0){
            y=0;
        }else if(y>maxY){
            y=maxY;
        }

        collisionDetection.left=x;
        collisionDetection.right=x+bitmap.getWidth();
        collisionDetection.top=y;
        collisionDetection.bottom=y +bitmap.getHeight();
    }
}
