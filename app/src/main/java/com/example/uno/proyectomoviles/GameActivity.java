package com.example.uno.proyectomoviles;

import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;

public class GameActivity  extends AppCompatActivity{
    //Declarando la vista del juego
    private GameView gameView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        //Inicializando el objeto gameView
        gameView = new GameView(this, size.x, size.y);

        //Agregandolo al contenido
        setContentView(gameView);
    }

    //Pausando el juego cuando la actividad esta pausada
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //corre el juego cuando la actividad se vuelve a comenzar
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
