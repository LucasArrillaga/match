package com.example.luccas.match;

import android.graphics.Bitmap;

/**
 * Created by luccas on 22/05/16.
 */
public class Cancha {

    private int id_cancha;
    private String nom_complejo;

    //private int precio_cancha;
    //private String desc_cancha;
    //private Bitmap foto_cancha;
    //private float calificacion_cancha;
    private double latitud_cancha;
    private double longitud_cancha;


    public Cancha(int id, String nombre_complej, double latitud, double longitud){

        id_cancha=id;
        nom_complejo=nombre_complej;
        latitud_cancha=latitud;
        longitud_cancha=longitud;


    }

    public int getId_cancha(){
        return id_cancha;
    }

    public  String getNombre_complejo(){
        return nom_complejo;
    }

    public double getLatitud_cancha(){
        return latitud_cancha;
    }

    public double getLongitud_cancha(){
        return longitud_cancha;
    }



}
