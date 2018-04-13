package com.example.luccas.match;

import android.graphics.Bitmap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luccas on 03/07/16.
 */
public class Reserva  {





    private String url_foto;
    private String nom_complejo;
    private String nom_cancha;

    private String fechahora;



    public Reserva( String nombre_complejo, String nombre_cancha,String fh, String foto ){


        nom_complejo = nombre_complejo;
        nom_cancha = nombre_cancha;
        url_foto = foto;
        fechahora = fh;


    }



    public String getNom_complejo(){
        return nom_complejo;
    }
    
    public String getNom_cancha(){
        return nom_cancha;
    }



    public String getFecha() {
        return fechahora;
    }

    public String getFoto_cancha(){
        return url_foto;
    }

}
