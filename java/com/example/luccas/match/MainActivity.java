package com.example.luccas.match;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    apiBd usdbh = new apiBd(this, "DBUsuario", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SQLiteDatabase db = usdbh.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT id_user FROM usuario", null);

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros

            String id_u = c.getString(0);



            Intent inte = new Intent(MainActivity.this, MapaCanchas.class);
            //inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Creamos la información a pasar entre actividades
            Bundle bun = new Bundle();
            bun.putString("id_user", id_u);

            //Añadimos la información al intent
            inte.putExtras(bun);

            //Iniciamos la nueva actividad

            startActivity(inte);

            finish();




        }

        else {


            String id_u = "1";



            Intent inte = new Intent(MainActivity.this, MapaCanchas.class);
            //inte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //Creamos la información a pasar entre actividades
            Bundle bun = new Bundle();
            bun.putString("id_user", id_u);

            //Añadimos la información al intent
            inte.putExtras(bun);

            //Iniciamos la nueva actividad

            startActivity(inte);

            finish();


        }

    }


}
