package com.example.uno.proyectomoviles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

<<<<<<< HEAD
public class GameView extends SurfaceView implements Runnable {
=======
import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable{
>>>>>>> a5900e83d4877b83b8c1798f012cbadc1459fbe2

    //booleano para verificar si se esta jugando o no
    volatile boolean playing;

    //hilo del juego
    private Thread gameThread = null;

    private Player player;

    //Estos objetos se utilizaran para dibujar la superficie
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;


    //Añadiendo lista de estrellas
    private ArrayList<Star> stars = new ArrayList<Star>();


    //constructor
<<<<<<< HEAD
    public GameView(Context context) {
=======
    public GameView(Context context, int screenX, int screenY){
>>>>>>> a5900e83d4877b83b8c1798f012cbadc1459fbe2

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

<<<<<<< HEAD
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_DOWN:

                break;
=======

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
        case MotionEvent.ACTION_UP:
        player.stopBoosting();
        break;
        case MotionEvent.ACTION_DOWN:
        player.setBoosting();
        break;
>>>>>>> a5900e83d4877b83b8c1798f012cbadc1459fbe2
        }
        return true;
    }
}
