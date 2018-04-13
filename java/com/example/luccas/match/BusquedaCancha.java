package com.example.luccas.match;

import android.graphics.Bitmap;

/**
 * Created by luccas on 02/02/17.
 */

public class BusquedaCancha {

    
    private String nom_complejo;
    private String precio_cancha;
    private String foto_cancha;
    


    public BusquedaCancha(String nombre_complejo, String precio, String foto){

        nom_complejo = nombre_complejo;
        precio_cancha = precio;
        foto_cancha = foto;
       

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

  

}
