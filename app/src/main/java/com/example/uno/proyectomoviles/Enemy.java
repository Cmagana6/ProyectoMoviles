package com.example.uno.proyectomoviles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Enemy {
    //bitmap para obtener la imagen del enemigo
    private Bitmap bitmap;

    //coordenadas
    private int x;
    private int y;

    //velocidad del enemigo
    private int speed = 1;

    //min y maximo de las coordenadas para mandener al enemigo en pantalla
    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    //creando objeto rectangular
    private Rect detectCollision;

    public Enemy(Context context, int screenX, int screenY){
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        //generando coordenadas aleatorias para poner al enemigo
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();
    }

    public void update(int playerSpeed){
        //reduce x para que se mueva de derecha a izquierda
        x -= playerSpeed;
        x -= speed;

        //si el enemigo llega al lado izquierdo de la pantalla
        if (x < minX - bitmap.getWidth()){
            //regresar al enemigo al lado derecho
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }

        //agregando lados para detectar colisiones
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    //setter en x para poder cambiarlo despues de colisionar
    public void setX(int x){
        this.x = x;
    }

    //getters
    public Rect getDetectCollision(){
        return detectCollision;
    }

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
