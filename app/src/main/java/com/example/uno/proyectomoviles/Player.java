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

<<<<<<< HEAD
    //booleano para verificar si la nave esta acelerando
    private boolean boosting;

    //el valor de la gravedad que afecta al jugador
    private final int GRAVITY = -10;

    //para evitar que la nave salga de la pantalla
    private int maxY;
    private int minY;

    //limita la velocidad de la nave
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    //constructor
    public Player(Context context){
=======

        //constructor
    public Player(Context context, int screenX, int screenY){
>>>>>>> a5900e83d4877b83b8c1798f012cbadc1459fbe2
        x = 75;
        y = 50;
        speed = 1;

        // agarrando el bitmap de drawable resources
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
<<<<<<< HEAD
        //boost es falso al principio
        boosting = false;
    }



    //seteaando el estado de los booster
    public void setBoosting(){
        boosting = true;
    }

    public void stopBoosting(){
=======

       //calculando maxY
        maxY = screenY - bitmap.getHeight();
        //el menor valor de y es 0 que siempre será 0
        minY = 0;

        //setting the boosting value to false initially
>>>>>>> a5900e83d4877b83b8c1798f012cbadc1459fbe2
        boosting = false;
    }

    //metodo para actualizar las coordenadas del personaje
    public void update(){
        if (boosting){
            speed += 2;
        }else {
            speed -= 5;
        }
        //control de maxima y minima velocidad
        if (speed > MAX_SPEED){
            speed = MAX_SPEED;
        }
        if (speed < MIN_SPEED){
            speed = MIN_SPEED;
        }

        //aplicando gravedad
        y -= speed + GRAVITY;

        //control para que no se salga de la pantalla
        if (y < minY){
            y = minY;
        }
        if (y > maxY){
            y = maxY;
        }
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
