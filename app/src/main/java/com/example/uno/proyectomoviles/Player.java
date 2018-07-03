package com.example.uno.proyectomoviles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Player {

    //bitmap para jalar al personaje de imagenes
    private Bitmap bitmap;

    //coordenadas
    private int x;
    private int y;

    //velocidad del personaje (movimiento)
    private int speed = 0;

    //constructor
    public Player(Context context){
        x = 75;
        y = 50;
        speed = 1;

        // agarrando el bitmap de drawable resources
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
    }

    //metodo para actualizar las coordenadas del personaje
    public void update(){
        //actualizando coordenadas x
        x++;
    }

    // getters

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
