package com.example.luccas.match;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class fh_reserva extends AppCompatActivity {

    public ArrayList<TipoCancha>ArrayTipoCancha = new ArrayList<>();
    private RecyclerView recViewTipo_cancha;
    AdaptadorTipoCancha adaptadorTipoCancha;

    public ArrayList<HoraLibre> ArrayHora = new ArrayList<HoraLibre>();
    private RecyclerView recView;
    AdaptadorHoraLibre adaptador;

    String id_usuario;
    String hora;
    String num_jugadores;
    String id_complejo;
    String url;
    String servidor = "http://52.72.248.41/";

    String fecha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fh_reserva);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = this.getIntent().getExtras();
        id_complejo=bundle.getString("id_cancha");
        num_jugadores = "5";


        id_usuario=bundle.getString("id_user");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // Operaciones http


                    url=servidor+"reserva.php";

                    final StringRequest postRequest;

                    postRequest = new StringRequest(Request.Method.POST,url,
                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response

                                    Log.d("Response", response);

                                    if(response.equals("ok")){
                                        Snackbar.make(view, "Se creo reserva", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                    }
                                    else{
                                        Snackbar.make(view, "Error", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }


                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.d("Error.Response", error.getMessage());
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //Converting Bitmap to String

                            //Creating parameters
                            Map<String,String> params = new Hashtable<String, String>();
                            //Adding parameters
                            params.put("id_complejo", id_complejo);
                            params.put("num_jugadores", num_jugadores);
                            params.put("id_usuario", id_usuario);
                            params.put("hf_reserva",fecha + " " + hora + ":00:00");

                            //returning parameters
                            return params;
                        }
                    };

                    MatchSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);

                } else {
                    Snackbar.make(view, "Usted no esta conectado a internet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });


        final long today = System.currentTimeMillis() - 1000;

        CalendarView calendario = (CalendarView) findViewById(R.id.calendarView);
        calendario.setMinDate(today);

        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy/MM/dd");
        fecha = simpleDate.format(today);




        ////////////////////////////////////////////////////////////////////////////////////////

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int dia = dayOfMonth;
                int mes = month+1;
                int año = year;

                fecha =String.valueOf(año)+"/"+String.valueOf(mes)+"/"+String.valueOf(dia);

                num_jugadores = "5";

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


                if (networkInfo != null && networkInfo.isConnected()) {

                    url = servidor+"reserva_hora_libre.php?id_complejo="+id_complejo+"&fecha="+fecha+"&numero_jugadores="+num_jugadores;

                    final JsonObjectRequest getRequest;

                    getRequest = new JsonObjectRequest(Request.Method.GET,url , null,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // response
                                    try {
                                        JSONArray jsonArray= null;

                                        // Obtener el array del objeto
                                        jsonArray = response.getJSONArray("respuestas");

                                        ArrayHora.clear();

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            try {

                                                JSONObject objeto= jsonArray.getJSONObject(i);


                                                ArrayHora.add(new HoraLibre(objeto.getString("hora")));



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

            }
        });




////////////////////////////////////////////////////////////////////////////////////////

        recViewTipo_cancha = (RecyclerView) findViewById(R.id.RVtipo_cancha);
        recViewTipo_cancha.setHasFixedSize(true);
        adaptadorTipoCancha=new AdaptadorTipoCancha(ArrayTipoCancha);


        adaptadorTipoCancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                num_jugadores = ArrayTipoCancha.get(recView.getChildAdapterPosition(v)).getNum_jugadores();
                Log.i("DemoRecView", "Num jugadores: " + num_jugadores);

                toolbar.setTitle("Futbol " + num_jugadores);

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {

                    url = servidor+"reserva_hora_libre.php?id_complejo="+id_complejo+"&fecha="+fecha+"&numero_jugadores="+num_jugadores;

                    final JsonObjectRequest getRequest;

                    getRequest = new JsonObjectRequest(Request.Method.GET,url , null,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // response
                                    try {
                                        JSONArray jsonArray= null;

                                        ArrayHora.clear();
                                        // Obtener el array del objeto
                                        jsonArray = response.getJSONArray("respuestas");

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            try {

                                                JSONObject objeto= jsonArray.getJSONObject(i);

                                                ArrayHora.add(new HoraLibre(objeto.getString("hora")));


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


            }
        });


        recViewTipo_cancha.setLayoutManager(new GridLayoutManager(this,3));
        recViewTipo_cancha.setAdapter(adaptadorTipoCancha);



        /////////////////////////////////////////////////////////////////////////




        recView = (RecyclerView) findViewById(R.id.RecView);
        recView.setHasFixedSize(true);
        adaptador = new AdaptadorHoraLibre(ArrayHora);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hora = ArrayHora.get(recView.getChildAdapterPosition(v)).getHora();
                Log.i("DemoRecView", "Pulsado el elemento " + hora);
                toolbar.setTitle("Futbol " + num_jugadores + " - " + hora + "hs");

            }
        });

        recView.setLayoutManager(new GridLayoutManager(this,5));
        recView.setAdapter(adaptador);



        /////////////////////////////////////////////////////////////////////////


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {


            url = servidor+"reserva_tipo_cancha.php?id_complejo="+id_complejo;
            final JsonObjectRequest getRequest;


            getRequest = new JsonObjectRequest(Request.Method.GET,url , null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            try {
                                JSONArray jsonArray= null;

                                // Obtener el array del objeto
                                jsonArray = response.getJSONArray("respuestas");

                                ArrayTipoCancha.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    try {

                                        JSONObject objeto= jsonArray.getJSONObject(i);

                                        ArrayTipoCancha.add(new TipoCancha(objeto.getString("numero_jugadores")));


                                    } catch (JSONException e) {
                                        String TAG = "error";
                                        Log.e(TAG, "Error de parsing: "+ e.getMessage());
                                    }


                                }
                                adaptadorTipoCancha.notifyDataSetChanged();



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


        ///////////////////////////////////////////////////////////////////////////////////////////

        if (networkInfo != null && networkInfo.isConnected()) {

            url = servidor+"reserva_hora_libre.php?id_complejo="+id_complejo+"&fecha="+fecha+"&numero_jugadores="+num_jugadores;

            final JsonObjectRequest getRequest;

            getRequest = new JsonObjectRequest(Request.Method.GET,url , null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            try {
                                JSONArray jsonArray= null;

                                // Obtener el array del objeto
                                jsonArray = response.getJSONArray("respuestas");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    try {

                                        JSONObject objeto= jsonArray.getJSONObject(i);

                                        ArrayHora.add(new HoraLibre(objeto.getString("hora")));


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
