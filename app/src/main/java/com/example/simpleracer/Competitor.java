package com.example.simpleracer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;

import java.util.Random;

public class Competitor {

    Context context;
    Bitmap bitmap;

    int x;
    int y;
    int maxX;
    int maxY;
    int speed;
    boolean crashed;
    boolean playerCrashed;

    int screenSizeX;
    int screenSizeY;

    Random random;
    int count=0;
    int xMovementDirection=0;

    int[] skins = {R.drawable.bugatti, R.drawable.greycar,
                    R.drawable.lambo, R.drawable.police, R.drawable.redcar,
                    R.drawable.scooter, R.drawable.truck, R.drawable.redsportcar};

    Rect collisionDetector;

    public  Competitor(Context context, int screenSizeX, int screenSizeY, int speed)
    {
        this.context=context;
        random = new Random();
        this.speed=random.nextInt(speed/2)+1;
        this.screenSizeX=screenSizeX;
        this.screenSizeY= screenSizeY;

        resetCompetitor();

    }

    public void resetCompetitor(){
        count++;
        crashed=false;
playerCrashed=false;
        if(count==2){
            count=0;
            if(random.nextBoolean()){
                xMovementDirection=1;
            }else {
                xMovementDirection=-1;
            }
        }else{
            xMovementDirection=0;
        }
        bitmap = BitmapFactory.decodeResource(context.getResources(), skins[random.nextInt(skins.length)]);

        maxX=screenSizeX-bitmap.getWidth();
        maxY = screenSizeY-bitmap.getHeight();

        y= (random.nextInt(100) + bitmap.getHeight())*-1;
        x = random.nextInt(maxX);

        collisionDetector= new Rect(x,y,x+bitmap.getWidth(),y+ bitmap.getHeight());
    }
    public void update (int playerSpeed){
        x+=xMovementDirection;
        y += playerSpeed - speed;

        if(y>maxY) {
        resetCompetitor();
        this.speed = random.nextInt(playerSpeed/2)+1;
        }

        if(x<0){
            x=0;
            xMovementDirection=0;
        }else if (x>maxX){
            x=maxX;
            xMovementDirection=0;
        }
        collisionDetector.top =y;
        collisionDetector.bottom = y+ bitmap.getHeight();
        collisionDetector.left=x;
        collisionDetector.right=x+bitmap.getWidth();
    }

    public void crashed() {
        xMovementDirection=0;
        crashed=true;
        speed=0;
    }
}
