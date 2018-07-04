package com.example.uno.proyectomoviles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable{


    //booleano para verificar si se esta jugando o no
    volatile boolean playing;

    //hilo del juego
    private Thread gameThread = null;

    private Player player;

    //Estos objetos se utilizaran para dibujar la superficie
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Friend friend;


    //Añadiendo lista de estrellas
    private ArrayList<Star> stars = new ArrayList<Star>();

    //definiendo el objeto boom
    private Boom boom;


    //constructor

    public GameView(Context context, int screenX, int screenY){

        super(context);

        //Inicializando el player
        player = new Player(context, screenX, screenY);

        //Inicializando los objetos para dibujar
        surfaceHolder = getHolder();
        paint = new Paint();

        //añadiendo estrellas
        int starNums = 100;
        for (int i = 0; i < starNums; i++){
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        //iniciando objeto Boom
        boom = new Boom(context);

        //iniciando el objeto de la clase Friend
        friend = new Friend(context, screenX, screenY);


    }

    @Override
    public void run() {
        while (playing) {
            //actualiza la pantalla
            update();

            //dibuja la pantalla
            draw();

            //controla
            control();
        }
    }

    private void update() {
        //actualiza la posicion del jugador
        player.update();

        boom.setX(-250);
        boom.setY(-250);


        for (Star s : stars){
            s.update(player.getSpeed());
        }


        enemies.update(player.getSpeed());

            //si la colision ocurre con Player
            if(Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())){

                //mostrando el boom en la ubicacion
                boom.setX(enemies.getX());
                boom.setY(enemies.getY());

                //moviendo al enemigo afuera del borde izquierdo
               enemies.setX(-200);
            }

          friend.update(player.getSpeed());

    }

    private void draw() {
        //Checkea que la superficie sea valida
        if (surfaceHolder.getSurface().isValid()) {
            //Lockea el canvas
            canvas = surfaceHolder.lockCanvas();
            //Dibujando un color de fondo para el canvas
            canvas.drawColor(Color.BLACK);
            //asignando el color de las estrellas
            paint.setColor(Color.WHITE);

            //dibujando las estrellas
            for(Star s : stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }
            //Dibujando el jugador
            canvas.drawBitmap(player.getBitmap(),
                    player.getX(), player.getY(), paint);


            //dibujando imagen boom
            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            //dibujando imagen friend
            canvas.drawBitmap(
                    friend.getBitmap(),
                    friend.getX(),
                    friend.getY(),
                    paint
            );

            //Desbloqueando el canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //cuando el juego esta pausado la variable se cambia a falso
        playing = false;
        try {
            //deteniendo el hilo
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume() {
        //cuando se quita pausa se inicia el hilo otra vez
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();

    }



    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
        case MotionEvent.ACTION_UP:
        player.stopBoosting();
        break;
        case MotionEvent.ACTION_DOWN:
        player.setBoosting();
        break;

        }
        return true;
    }
}
