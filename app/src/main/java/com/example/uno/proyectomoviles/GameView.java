package com.example.uno.proyectomoviles;

import android.content.Context;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable{

    //booleano para verificar si se esta jugando o no
    volatile boolean playing;

    //hilo del juego
    private Thread gameThread = null;

    //constructor
    public GameView(Context context){
        super(context);
    }

    @Override
    public void run() {
        while (playing){
            //actualiza la pantalla
            update();

            //dibuja la pantalla
            draw();

            //controla
            control();
        }
    }

    private void update(){

    }

    private void draw(){

    }

    private void control(){
        try {
            gameThread.sleep(17);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void pause(){
        //cuando el juego esta pausado la variable se cambia a falso
        playing = false;
        try {
            //deteniendo el hilo
            gameThread.join();
        }catch (InterruptedException e){

        }
    }

    public void resume(){
        //cuando se quita pausa se inicia el hilo otra vez
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();

    }
}
