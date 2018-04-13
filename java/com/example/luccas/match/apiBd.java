package com.example.luccas.match;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luccas on 30/11/16.
 */

public class apiBd  extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Usuario (id_user INTEGER primary key, ciudad VARCHAR(30), ultima_localizacion VARCHAR(100))";

    public apiBd(Context contexto, String nombre,
                 SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente
        // la opción de eliminar la tabla anterior y crearla de nuevo
        // vacía con el nuevo formato.
        // Sin embargo lo normal será que haya que migrar datos de la
        // tabla antigua a la nueva, por lo que este método debería
        // ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Usuario");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }

}
