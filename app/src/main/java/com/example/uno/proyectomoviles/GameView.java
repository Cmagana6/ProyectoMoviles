package com.example.uno.proyectomoviles;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {


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

    //screenX holder
    int screenX;

    //para contar el numero de perdidas
    int countMisses;

    //indicador que el enemigo ha entrado la pantalla de juego
    boolean flag;

    //indicador para Game Over
    private boolean isGameOver;

    //holder de puntajes
    int score;

    //holder de puntajes altos
    int highScore[] = new int [4];

    //Shared Preferences para almacenar los puntajes altos
    SharedPreferences sharedPreferences;


    //constructor

    public GameView(Context context, int screenX, int screenY) {

        super(context);

        //Inicializando el player
        player = new Player(context, screenX, screenY);

        //Inicializando los objetos para dibujar
        surfaceHolder = getHolder();
        paint = new Paint();

        //añadiendo estrellas
        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        //iniciando objeto Boom
        boom = new Boom(context);

        //iniciando el objeto de la clase Friend
        friend = new Friend(context, screenX, screenY);

        this.screenX = screenX;
        countMisses = 0;
        isGameOver = false;

        //seteando el puntaje en 0 inicialmente
        score = 0;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME", Context.MODE_PRIVATE);

        //iniciando el arreglo de puntajes altos con los valores previos
        highScore[0] = sharedPreferences.getInt("score1", 0);
        highScore[1] = sharedPreferences.getInt("score2", 0);
        highScore[2] = sharedPreferences.getInt("score3", 0);
        highScore[3] = sharedPreferences.getInt("score4", 0);


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

        //incrementando el puntaje cuando pasa el tiempo
        score++;

        //actualiza la posicion del jugador
        player.update();

        boom.setX(-250);
        boom.setY(-250);


        for (Star s : stars) {
            s.update(player.getSpeed());
        }

        //seteando la bandera como verdadera cuando el enemigo entra la pantalla
        if (enemies.getX() == screenX) {
            flag = true;
        }

        enemies.update(player.getSpeed());

        //si la colision ocurre con Player
        if (Rect.intersects(player.getDetectCollision(), enemies.getDetectCollision())) {

            //mostrando el boom en la ubicacion
            boom.setX(enemies.getX());
            boom.setY(enemies.getY());

            //moviendo al enemigo afuera del borde izquierdo
            enemies.setX(-200);
        } //la condicion cuando el player pierde al enemigo
        else {
            //si el enemigo apenas entró
            if (flag) {

                //si la coordenada X de player es mayor que la coordenada x de enemies
                if (player.getDetectCollision().exactCenterX() & gt;=enemies.getDetectCollision().exactCenterX()){

                    //incrementando countMisses
                    countMisses++;


                    //seteando la bandera a  false asi solo se ejecuta cuando el enemigo entra en la pantalla
                    flag = false;
                    //si no Misses es igual a 3, entonces es  game over.
                    if (countMisses == 3) {

                        //seteando playing false para parar el juego.
                        playing = false;
                        isGameOver = true;

                        //Asignando los puntajes al arreglo de puntajes
                        for (int i = 0; i&lt;4;i++){
                            if (highScore[i] & lt;score){

                                final int finalI = i;
                                highScore[i] = score;
                                break;
                            }
                        }

                        //almacenando los puntajes a traves de Shared Preferences
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        for (int i = 0; i & lt; 4 ;i++){
                            int j = i + 1;
                            e.putInt("score" + j, highScore[i]);
                        }
                        e.apply();
                    }
                }
            }
        }

        //actualizando las coordenadas de la nave friend
        friend.update(player.getSpeed());
        //checkeando colision entre player y friend
        if(Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())){

            //mostrando el boom en la collision
            boom.setX(friend.getX());
            boom.setY(friend.getY());
            //seteando playing false para parar el juego
            playing = false;
            //setting the isGameOver true as the game is over
            isGameOver = true;

            //Assigning the scores to the highscore integer array
            for(int i=0;i&lt;4;i++){
                if(highScore[i]&lt;score){

                    final int finalI = i;
                    highScore[i] = score;
                    break;
                }
            }

            //storing the scores through shared Preferences
            SharedPreferences.Editor e = sharedPreferences.edit();
            for(int i=0;i&lt;4;i++){
                int j = i+1;
                e.putInt("score"+j,highScore[i]);
            }
            e.apply();
        }
    }


        private void draw () {
            //Checkea que la superficie sea valida
            if (surfaceHolder.getSurface().isValid()) {
                //Lockea el canvas
                canvas = surfaceHolder.lockCanvas();
                //Dibujando un color de fondo para el canvas
                canvas.drawColor(Color.BLACK);
                //asignando el color de las estrellas
                paint.setColor(Color.WHITE);

                //dibujando las estrellas
                for (Star s : stars) {
                    paint.setStrokeWidth(s.getStarWidth());
                    canvas.drawPoint(s.getX(), s.getY(), paint);
                }

                //dibujando el puntaje en la pantalla de juego
                paint.setTextSize(30);
                canvas.drawText("Score:"+score,100,50,paint);

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

                if (isGameOver) {
                    paint.setTextSize(150);
                    paint.setTextAlign(Paint.Align.CENTER);

                    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                    canvas.drawText("Game Over", canvas.getWidth() / 2, yPos, paint);
                }

                //Desbloqueando el canvas
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        private void control () {
            try {
                gameThread.sleep(17);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void pause () {
            //cuando el juego esta pausado la variable se cambia a falso
            playing = false;
            try {
                //deteniendo el hilo
                gameThread.join();
            } catch (InterruptedException e) {

            }
        }

        public void resume () {
            //cuando se quita pausa se inicia el hilo otra vez
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();

        }


        @Override
        public boolean onTouchEvent (MotionEvent motionEvent){
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
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