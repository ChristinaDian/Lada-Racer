package com.example.simpleracer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class PowerUp {

    int x;
    int y;
    int maxX;
    int maxY;
    Context context;
    int speed;
    Bitmap bitmap;
    Rect collisionDetection;
    Random random;

    int screenSizeX;
    int screenSizeY;

    public PowerUp(Context context, int screenSizeX, int screenSizeY, int playerSpeed)
    {
        this.context=context;

        this.speed=playerSpeed;
        this.screenSizeX=screenSizeX;
        this.screenSizeY= screenSizeY;
        resetPowerUp();

    }
    public void update (int playerSpeed){
        y += playerSpeed;

        collisionDetection.top =y;
        collisionDetection.bottom = y+ bitmap.getHeight();
        collisionDetection.left=x;
        collisionDetection.right=x+bitmap.getWidth();
    }

    public void resetPowerUp(){

        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.wrench);
        maxX=screenSizeX-bitmap.getWidth();
        maxY = screenSizeY-bitmap.getHeight();

        y= 0;
        x = random.nextInt(maxX);

        collisionDetection= new Rect(x,y,x+bitmap.getWidth(),y+ bitmap.getHeight());
    }
}
