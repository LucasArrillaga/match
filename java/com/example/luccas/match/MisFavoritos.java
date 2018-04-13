package com.example.luccas.match;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MisFavoritos extends AppCompatActivity {

    private ArrayList<Favorito> datosFavorito;
    private RecyclerView recView;
    private AdaptadorFavorito adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_favoritos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        datosFavorito = new ArrayList<Favorito>();


        Bitmap imagen_cancha = BitmapFactory.decodeResource(getResources(), R.drawable.cancha);
        Bitmap imagen_cancha2 = BitmapFactory.decodeResource(getResources(), R.drawable.cancha2);

        datosFavorito.add(new Favorito(1,"Cancha 1","Las Gemelas",1000,imagen_cancha,4));


        recView = (RecyclerView) findViewById(R.id.RVfavorito);
        recView.setHasFixedSize(true);
        adaptador = new AdaptadorFavorito(datosFavorito);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recView.setAdapter(adaptador);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
