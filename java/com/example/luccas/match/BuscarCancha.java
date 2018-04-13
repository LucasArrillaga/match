package com.example.luccas.match;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuscarCancha extends AppCompatActivity {


    apiBd usdbh = new apiBd(this, "DBUsuario", null, 1);
    String servidor = "http://52.72.248.41/";
    String id_user;
    String ciudad;
    EditText txtBuscar;
    Button btnBuscar;

    String url;
    String location = "Montevideo";
    List<Address> addresses;
    List<LatLng> ll;

    private ArrayList<BusquedaCancha> datosBusqueda;
    private RecyclerView recView;
    private AdaptadorBusquedaCancha adaptador;

    private final String KEY_RECYCLER_STATE = "recycler_state";

    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cancha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtBuscar = (EditText) findViewById(R.id.txtBuscar);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);

        final Bundle bundle = this.getIntent().getExtras();
        id_user=bundle.getString("id_user");

        SQLiteDatabase db = usdbh.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT ciudad FROM usuario WHERE id_user="+id_user, null);

        if(db != null) {




            if (c.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                ciudad = c.getString(0);
                if(ciudad!=null) {
                    Log.d("Ciudad", ciudad);
                }
            }

            //Cerramos la base de datos
            db.close();
        }







        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBuscar.getWindowToken(), 0);
                recView.requestFocus();

                if(Geocoder.isPresent()){
                    try {
                        location=txtBuscar.getText().toString();
                        Geocoder gc = new Geocoder(BuscarCancha.this);
                        addresses= gc.getFromLocationName(location, 1); // get the found Address Objects

                        ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                        for(Address a : addresses){
                            if(a.hasLatitude() && a.hasLongitude()){
                                ll.add(new LatLng(a.getLatitude(), a.getLongitude()));

                            }

                        }
                    } catch (IOException e) {
                        // handle the exception
                    }
                }

                //Snackbar.make(v, String.valueOf(ll.get(0).latitude ) + "  " +  String.valueOf(ll.get(0).longitude ), Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();


                url =servidor+"buscar_por_localizacion.php?latitud="+String.valueOf(ll.get(0).latitude )+"&"+"longitud="+ String.valueOf(ll.get(0).longitude);
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

                                        datosBusqueda.clear();

                                        JSONArray jsonArray= null;

                                        // Obtener el array del objeto
                                        jsonArray = response.getJSONArray("respuestas");




                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            try {

                                                JSONObject objeto= jsonArray.getJSONObject(i);


                                                String id_complejo = objeto.getString("id_complejo");
                                                String nombre_complejo = objeto.getString("nombre_complejo");
                                                String precio = objeto.getString("precio");
                                                String url_foto = objeto.getString("foto_complejo");




                                                    datosBusqueda.add(new BusquedaCancha(id_complejo,nombre_complejo,precio,url_foto,5));





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







        datosBusqueda = new ArrayList<BusquedaCancha>();


        recView = (RecyclerView) findViewById(R.id.RVbuscar);
        recView.setHasFixedSize(true);
        adaptador = new AdaptadorBusquedaCancha(datosBusqueda);
        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recView.setAdapter(adaptador);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DemoRecView", "Pulsado el elemento " + datosBusqueda.get(recView.getChildPosition(v)).getId_cancha());

                Intent inte = new Intent(BuscarCancha.this, InfoCancha.class);
                //Creamos la información a pasar entre actividades
                Bundle bun = new Bundle();
                bun.putString("id_user", id_user);
                bun.putString("id_cancha", String.valueOf(datosBusqueda.get(recView.getChildPosition(v)).getId_cancha()));

                //Añadimos la información al intent
                inte.putExtras(bun);
                startActivity(inte);

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


    @Override
    protected void onPause()
    {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }


    @Override
    protected void onResume() {

        super.onResume();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recView.getLayoutManager().onRestoreInstanceState(listState);
        } else {


            url = servidor + "buscar_complejo.php?ciudad=" + ciudad;
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


            if (networkInfo != null && networkInfo.isConnected()) {
                // Operaciones http

                final JsonObjectRequest getRequest;
                getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {
                                // response
                                try {

                                    JSONArray jsonArray = null;

                                    // Obtener el array del objeto
                                    jsonArray = response.getJSONArray("respuestas");

                                    datosBusqueda.clear();

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        try {

                                            JSONObject objeto = jsonArray.getJSONObject(i);


                                            String id_complejo = objeto.getString("id_complejo");
                                            String nombre_complejo = objeto.getString("nombre_complejo");
                                            String precio = objeto.getString("precio");
                                            String url_foto = objeto.getString("foto_complejo");


                                            datosBusqueda.add(new BusquedaCancha(id_complejo, nombre_complejo, precio, url_foto, 5));


                                        } catch (JSONException e) {
                                            String TAG = "error";
                                            Log.e(TAG, "Error de parsing: " + e.getMessage());
                                        }


                                    }
                                    adaptador.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
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
    }

    @Override
    protected void onStop() {

        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBundleRecyclerViewState=null;
    }
}
