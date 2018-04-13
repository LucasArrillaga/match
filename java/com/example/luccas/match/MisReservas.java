package com.example.luccas.match;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MisReservas extends AppCompatActivity {


    private ArrayList<Reserva> datosReserva;
    private RecyclerView recView;
    private AdaptadorReserva adaptador;

    String servidor = "http://52.72.248.41/";
    String id_user;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final Bundle bundle = this.getIntent().getExtras();
        id_user=bundle.getString("id_user");

        final RequestQueue queue = Volley.newRequestQueue(this);

        url =servidor+"mis_reservas.php?id_usuario="+id_user;



        datosReserva = new ArrayList<Reserva>();


        recView = (RecyclerView) findViewById(R.id.RVreservas);
        recView.setHasFixedSize(true);
        adaptador = new AdaptadorReserva(datosReserva);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recView.setAdapter(adaptador);




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


    @Override
    protected void onResume() {


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

                                JSONArray jsonArray= null;

                                // Obtener el array del objeto
                                jsonArray = response.getJSONArray("respuestas");

                                datosReserva.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    try {

                                        JSONObject objeto= jsonArray.getJSONObject(i);



                                        String nombre_complejo = objeto.getString("nombre_complejo");
                                        String hf_reserva = objeto.getString("hf_reserva");
                                        String nombre_cancha = objeto.getString("nombre_cancha");
                                        String url_foto = objeto.getString("foto_complejo");



                                        datosReserva.add(new Reserva(nombre_complejo,nombre_cancha,hf_reserva,url_foto));


                                    } catch (JSONException e) {
                                        String TAG = "error";
                                        Log.e(TAG, "Error de parsing: "+ e.getMessage());
                                    }


                                }
                                adaptador.notifyDataSetChanged();

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

        super.onResume();
    }
}
