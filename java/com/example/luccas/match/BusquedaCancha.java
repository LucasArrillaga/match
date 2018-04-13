package com.example.luccas.match;

import android.graphics.Bitmap;

/**
 * Created by luccas on 02/02/17.
 */

public class BusquedaCancha {

    
    private String nom_complejo;
    private String precio_cancha;
    private String foto_cancha;
    private float calificacion_cancha;


    public BusquedaCancha(String nombre_complejo, String precio, String foto, float calificacion){

        nom_complejo = nombre_complejo;
        precio_cancha = precio;
        foto_cancha = foto;
        calificacion_cancha = calificacion;

    }

    public String getId_cancha() {
        return id_cancha;
    }

    public String getNom_complejo() {
        return nom_complejo;
    }

    public String getPrecio_cancha() {
        return precio_cancha;
    }

    public String getFoto_cancha() {
        return foto_cancha;
    }

    public float getCalificacion_cancha() {
        return calificacion_cancha;
    }

}
