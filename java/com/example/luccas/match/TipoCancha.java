package com.example.luccas.match;

/**
 * Created by luccas on 16/11/16.
 */

public class TipoCancha  {

    private String num_jugadores;


    public TipoCancha(String numero_jugadores){

        num_jugadores = numero_jugadores;

    }

    public String getNum_jugadores() {
        return num_jugadores;
    }

    public void setNum_jugadores(String num_jugadores) {
        this.num_jugadores = num_jugadores;
    }
}
