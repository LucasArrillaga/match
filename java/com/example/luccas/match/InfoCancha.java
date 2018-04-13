package com.example.luccas.match;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class InfoCancha extends AppCompatActivity {


    String id_complejo;
    String nomComplejo;
    String descCancha;
    float calificacion = 5;
    String id_user;

    private Button btnHide;
    private TextView TxtnomComplejo;
    private TextView txtPrecio;
    private TextView txtHorario;
    private TextView TxtdescCancha;
    private NetworkImageView ImgCancha;
    private RatingBar RBcalificacion;
    String url_imagen;
    Bitmap icon_fav;

    String servidor = "http://52.72.248.41/";
    String url;

    apiBd usdbh = new apiBd(this, "DBUsuario", null, 1);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_cancha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ImgCancha = (NetworkImageView) findViewById(R.id.imgCancha);
        btnHide = (Button)findViewById(R.id.btn_hide);

        TxtnomComplejo = (TextView)findViewById(R.id.nomComplejo);
        txtPrecio = (TextView)findViewById(R.id.Precio);
        TxtdescCancha = (TextView)findViewById(R.id.descCancha);
        RBcalificacion = (RatingBar) findViewById(R.id.estrellas);
        txtHorario = (TextView)findViewById(R.id.txtHorario);


        Bundle bundle = this.getIntent().getExtras();
        id_complejo=bundle.getString("id_cancha");
        id_user = bundle.getString("id_user");


        RBcalificacion.setRating(calificacion);

        url = servidor+"info_complejo.php?id_complejo="+id_complejo;


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            // Operaciones http

            final JsonObjectRequest getRequest;
            getRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                    new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            try {

                                Log.d("Response", response.getString("nombre_complejo")
                                        + " , " + response.getString("descripcion")
                                        + " , " + response.getString("hora_apertura")
                                        + " , " + response.getString("hora_cierre")
                                        + " , " + response.getString("foto_complejo")
                                        + " , " + response.getString("precio_min")
                                        + " , " + response.getString("precio_max"));

                                TxtnomComplejo.setText(response.getString("nombre_complejo"));
                                TxtdescCancha.setText(response.getString("descripcion"));
                                txtPrecio.setText("$" + response.getString("precio_min") + " - " + "$"+response.getString("precio_max"));
                                txtHorario.setText(response.getString("hora_apertura")+"hs"+ " a " + response.getString("hora_cierre")+"hs");
                                url_imagen = response.getString("foto_complejo");

                                // Petición el image loader
                                ImageLoader imageLoader = MatchSingleton.getInstance(getApplicationContext()).getImageLoader();
// Petición
                                ImgCancha.setImageUrl(servidor+url_imagen, imageLoader);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("Error.Response", error.getMessage());
                        }
                    }
            );
            // Añadir petición a la cola
            MatchSingleton.getInstance(getApplicationContext()).addToRequestQueue(getRequest);


        }




        btnHide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                SQLiteDatabase db = usdbh.getWritableDatabase();
                Cursor c = db.rawQuery("SELECT id_user FROM usuario", null);

                if (c.moveToFirst()) {
                    //Recorremos el cursor hasta que no haya más registros

                    String id_u = c.getString(0);

                    Intent inte = new Intent(InfoCancha.this, fh_reserva.class);

                    Bundle bun = new Bundle();
                    bun.putString("id_cancha", id_complejo);
                    bun.putString("id_user",id_user);

                    inte.putExtras(bun);
                    startActivity(inte);


                }

                else {

                    Intent inte = new Intent(InfoCancha.this, Inicio.class);

                    startActivity(inte);

                    finish();


                }


            }
        });


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
