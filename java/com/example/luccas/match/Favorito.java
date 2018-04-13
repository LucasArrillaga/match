package com.example.luccas.match;

import android.graphics.Bitmap;

/**
 * Created by luccas on 19/07/16.
 */
public class Favorito {

    private int id_cancha;
    private String nom_complejo;
    private String nom_cancha;
    private int precio_cancha;
    private Bitmap foto_cancha;
    private float calificacion_cancha;

    public Favorito (int id, String nombre_cancha, String nombre_complejo, int precio, Bitmap foto, float calificacion){

        id_cancha = id;
        nom_cancha = nombre_cancha;
        nom_complejo = nombre_complejo;
        precio_cancha = precio;
        foto_cancha = foto;
        calificacion_cancha = calificacion;

    }


    public int getId_cancha() {
        return id_cancha;
    }

    public String getNom_complejo() {
        return nom_complejo;
    }

    public String getNom_cancha() {
        return nom_cancha;
    }

    public int getPrecio_cancha() {
        return precio_cancha;
    }

    public Bitmap getFoto_cancha() {
        return foto_cancha;
    }

    public float getCalificacion_cancha() {
        return calificacion_cancha;
    }
}
