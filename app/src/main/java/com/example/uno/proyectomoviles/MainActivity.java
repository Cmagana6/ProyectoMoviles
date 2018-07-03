package com.example.uno.proyectomoviles;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton buttonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Seteando la orientacion a Landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Obteniendo el boton
        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);

        //Agregando un listener al boton
        buttonPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //Iniciando game activity
        startActivity(new Intent(this, GameActivity.class));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
