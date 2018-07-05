package com.example.uno.proyectomoviles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Boom {

    //objeto bitmap
    private Bitmap bitmap;

    //coordinando variables
    private int x;
    private int y;

    //constructor
    public Boom(Context context){
        //obteniendo la imagen boom
        bitmap = BitmapFactory.decodeResource
                (context.getResources(), R.drawable.boom);

        //seteando el coordinador afuera de la pantalla
        //asi no la mostrara en pantalla
        //solo sera visible en la fraccion de segundo
        //despues de la colision

        x = -150;
        y = -150;
    }



    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}


