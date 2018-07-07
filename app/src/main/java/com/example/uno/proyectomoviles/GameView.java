package com.example.uno.proyectomoviles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
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

    int screenX;
    //context para usar en onTouchEvent para navegar entre GameActivity a MainActivity
    Context context;

    int score;

    int highScore[] = new int[4];

    //para guardar High Scores
    SharedPreferences sharedPreferences;

    int countMises;
    //indica si un enemigo entro a la pantalla de juego
    boolean flag;

    //indica Game Over
    private boolean isGameOver;

    //Estos objetos se utilizaran para dibujar la superficie
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private Enemy enemies;

    private Friend friend;


    //Añadiendo lista de estrellas
    private ArrayList<Star> stars = new ArrayList<Star>();

    //definiendo el objeto boom
    private Boom boom;

    //objetos para configurar la musica y sonidos de fondo
    static MediaPlayer gameOnsound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;

    //constructor

    public GameView(Context context, int screenX, int screenY){

        super(context);

        //Inicializando el player
        player = new Player(context, screenX, screenY);

        //Inicializando los objetos para dibujar
        surfaceHolder = getHolder();
        paint = new Paint();

        this.context = context;

        //añadiendo estrellas
        int starNums = 100;
        for (int i = 0; i < starNums; i++){
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        enemies = new Enemy(context, screenX, screenY);

        //iniciando objeto Boom
        boom = new Boom(context);

        //iniciando el objeto de la clase Friend
        friend = new Friend(context, screenX, screenY);

        score = 0;

        countMises = 0;

        this.screenX = screenX;

        isGameOver = false;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //se inicializan los valores del arreglo de puntaje
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);
        highScore[3] = sharedPreferences.getInt("score4", 0);

        //inicializan los valores de los sonidos
        gameOnsound = MediaPlayer.create(context, R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context, R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context, R.raw.gameover);

        //inicia la musica que va a sonar en el juego
        gameOnsound.start();
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
        //aumenta el puntaje entre mas pasa el tiempo
        score++;

        //actualiza la posicion del jugador
        player.update();

        boom.setX(-250);
        boom.setY(-250);


        for (Star s : stars){
            s.update(player.getSpeed());
        }
        //pone la vandera en true cuando los enemigo aparecen en pantalla
        if (enemies.getX() == screenX){
            flag = true;
        }


        enemies.update(player.getSpeed());
            //si la colision ocurre con Player
            if(Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())){

                //mostrando el boom en la ubicacion
                boom.setX(enemies.getX());
                boom.setY(enemies.getY());

                //inicializa el sonido de colision entre el jugador y el enemigo
                killedEnemysound.start();

                //moviendo al enemigo afuera del borde izquierdo
               enemies.setX(-200);
            }else {//cuando faya el jugador
                //si el enemigo acaba de entrar a la pantalla
                if (flag){
                    //si la coordenadas en x son iguales a las coordenadas en y del enemigo
                    if (player.getDetectCollision().exactCenterX() >= enemies.getDetectCollision().exactCenterX()){
                        countMises++;
                        //se pone la bandera en falso para que el else solo se ejecute cuando el enemigo este en pantalla
                        flag = false;

                        //si se falla 3 veces se pierde el juego
                        if (countMises == 3){
                            //se ponen las banderas en su valor correspondiente
                            playing = false;
                            isGameOver = true;

                            gameOnsound.stop();
                            gameOversound.start();

                            //asigna el puntaje al arreglo
                            for (int i=0; i<4; i++){
                                if (highScore[i]<score){
                                    final int finalI = i;
                                    highScore[i] = score;
                                    break;
                                }
                            }
                            //guarda el puntaje por shared preferences
                            SharedPreferences.Editor e = sharedPreferences.edit();

                            for (int i=0; i<4; i++){
                                int j = i+1;
                                e.putInt("puntaje"+j, highScore[i]);
                            }
                            e.apply();
                        }
                    }
                }
            }

          friend.update(player.getSpeed());
            //revisa si hay collision
        if (Rect.intersects(player.getDetectCollision(), friend.getDetectCollision())){
            // muestra explosion en la colision
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            //se detiene el juego
            playing = false;
            isGameOver = true;

            gameOnsound.stop();
            gameOversound.start();

            //asignando el puntaje al arreglo
            for (int i=0;i<4;i++){
                if (highScore[i]<score){
                    final int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }

            //guardando el puntaje
            SharedPreferences.Editor e = sharedPreferences.edit();

            for (int i=0;i<4;i++){
                int j = i=1;
                e.putInt("puntaje"+j,highScore[i]);
            }
            e.apply();
        }

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
            paint.setTextSize(20);

            //dibujando las estrellas
            for(Star s : stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }
            //Dibujando el jugador
            canvas.drawBitmap(player.getBitmap(),
                    player.getX(), player.getY(), paint);

            canvas.drawBitmap(enemies.getBitmap(), enemies.getX(), enemies.getY(), paint);

            //dibuja el puntaje en la pantalla de juego
            paint.setTextSize(30);
            canvas.drawText("Puntaje"+score,100,50,paint);

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

            //pone game over cuando se pierde
            if (isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent())/2));
                canvas.drawText("GAME OVER", canvas.getWidth() / 2, yPos, paint);
            }

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

    //Para la musica al salir
    public static void stopMusic(){
        gameOnsound.stop();
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
        //si se acabo el juego un tap a la pantalla nos regresa a la pantalla de inicio
        if (isGameOver){
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context, MainActivity.class));
            }
        }
        return true;
    }
}
