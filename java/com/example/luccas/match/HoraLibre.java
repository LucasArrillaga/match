package com.example.luccas.match;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by luccas on 02/06/16.
 */
public class HoraLibre {

    DateFormat formatoHora = new SimpleDateFormat("HH");


    private Date hora = new Date();

    public HoraLibre (String horaStr) {


        try {
             hora = formatoHora.parse(horaStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getHora() {
        return formatoHora.format(hora);
    }


}
