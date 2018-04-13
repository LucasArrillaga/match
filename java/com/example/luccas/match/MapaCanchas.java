package com.example.luccas.match;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.NavigationView;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapaCanchas extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraChangeListener {


    public HashMap<String, Marker> MarkersHash = new HashMap<String, Marker>();


    private static final int LOCATION = 0;

    private View mLayout;


    int id_cancha;
    String id_user;
    String servidor = "http://52.72.248.41/";
    String url;
    String url2;
    String latitud = null;
    String longitud = null;
    LatLng ubicacion;
    boolean bandera;
    boolean bandera2;
    String location = "Uruguay";
    List<Address> addresses;
    List<LatLng> ll;

    TextView txtNom;
    TextView txtEmail;
    NetworkImageView ImgUser;
    Button btnBuscar;

    apiBd usdbh = new apiBd(this, "DBUsuario", null, 1);

    private GoogleMap mMap;
    private HashMap<String, Cancha> Hashcanchas = new HashMap<String, Cancha>();
    private Bitmap imagen;

    private LocationManager locManager;
    private LocationListener locListener;
    Location loc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_canchas);

        if(latitud==null) {
            bandera2 = true;
        }

        mLayout = findViewById(R.id.Rlayout);

        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView.getMenu().findItem(R.id.menu_opcion_2).setVisible(false);

        final Bundle bundle = this.getIntent().getExtras();
        id_user=bundle.getString("id_user");


        verifyPermission();

        btnBuscar = (Button)findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        // Operaciones http

                        url = servidor+"mapa_canchas.php?latitud="+latitud+"&longitud="+longitud;

                        if(latitud != null){

                            final JsonObjectRequest getRequest;
                            mMap.clear();

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

                                                        Hashcanchas.put(objeto.getString("id_complejo"),new Cancha(Integer.parseInt(objeto.getString("id_complejo")),objeto.getString("nombre_complejo"),objeto.getDouble("latitud_complejo"),objeto.getDouble("longitud_complejo")));




                                                    } catch (JSONException e) {
                                                        String TAG = "error";
                                                        Log.e(TAG, "Error de parsing: "+ e.getMessage());
                                                    }


                                                }

                                                for(String key : Hashcanchas.keySet()){

                                                    int ii = 0;

                                                    //int precio = Hashcanchas.get(key).getPrecio_cancha();
                                                    LatLng ubicacion = new LatLng(Hashcanchas.get(key).getLatitud_cancha(), Hashcanchas.get(key).getLongitud_cancha());

                                                    Marker marker= mMap.addMarker(new MarkerOptions()

                                                            .anchor(0.0f, 1.0f)
                                                            .title(Hashcanchas.get(key).getNombre_complejo())
                                                            .position(ubicacion)
                                                            .icon(BitmapDescriptorFactory.fromBitmap(imagen))
                                                            .draggable(false)
                                                            .visible(true)
                                                            .flat(true)

                                                    );
                                                    MarkersHash.put(Integer.toString(Hashcanchas.get(key).getId_cancha()),marker);


                                                    if(ii<1) {
                                                        marker.showInfoWindow();

                                                        //CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(ubicacion, 12F);
                                                        //mMap.moveCamera(camUpd1);
                                                        id_cancha=Hashcanchas.get(key).getId_cancha();


                                                    }



                                                    ii++;
                                                }

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
                    bandera = false;

            }
        });


        SQLiteDatabase db = usdbh.getWritableDatabase();


        if(db != null)
        {


            Cursor c = db.rawQuery("SELECT id_user FROM usuario", null);

            if (c.moveToFirst()) {

                navView.getMenu().findItem(R.id.menu_opcion_2).setVisible(false);

            }

            else {

                navView.getMenu().findItem(R.id.menu_opcion_2).setVisible(true);
                navView.getMenu().findItem(R.id.menu_opcion_1).setVisible(false);


            }

            c.close();
        }

        else{
            navView.getMenu().findItem(R.id.menu_opcion_2).setVisible(true);
        }



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMap=mapFragment.getMap();

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);





            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling




                return;
            }else{


                if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(loc!=null){
                        latitud = String.valueOf(loc.getLatitude());
                        longitud = String.valueOf(loc.getLongitude());
                         ubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
                        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(ubicacion, 12F);
                        mMap.moveCamera(camUpd1);
                    }else
                        Toast.makeText(MapaCanchas.this, "Encienda el GPS para ver las canchas cercanas ", Toast.LENGTH_SHORT).show();

                }else{

                    loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if(loc!=null){
                        latitud = String.valueOf(loc.getLatitude());
                        longitud = String.valueOf(loc.getLongitude());
                         ubicacion = new LatLng(loc.getLatitude(),loc.getLongitude());
                        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(ubicacion, 12F);
                        mMap.moveCamera(camUpd1);
                    }else
                        Toast.makeText(MapaCanchas.this, "Encienda el GPS para ver las canchas cercanas", Toast.LENGTH_SHORT).show();


                }


            }


            //Nos registramos para recibir actualizaciones de la posición
            locListener = new LocationListener() {
                public void onLocationChanged(Location location) {

                    Log.i("LocAndroid", "Estado Proveedor:" + location);
                    latitud = String.valueOf(location.getLatitude());
                    longitud = String.valueOf(location.getLongitude());

                    if(bandera){

                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                        if (networkInfo != null && networkInfo.isConnected()) {
                            // Operaciones http

                            url = servidor+"mapa_canchas.php?latitud="+latitud+"&longitud="+longitud;

                            if(latitud != null){

                                final JsonObjectRequest getRequest;
                                mMap.clear();

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

                                                            Hashcanchas.put(objeto.getString("id_complejo"),new Cancha(Integer.parseInt(objeto.getString("id_complejo")),objeto.getString("nombre_complejo"),objeto.getDouble("latitud_complejo"),objeto.getDouble("longitud_complejo")));




                                                        } catch (JSONException e) {
                                                            String TAG = "error";
                                                            Log.e(TAG, "Error de parsing: "+ e.getMessage());
                                                        }


                                                    }

                                                    for(String key : Hashcanchas.keySet()){

                                                        int ii = 0;

                                                        //int precio = Hashcanchas.get(key).getPrecio_cancha();
                                                        LatLng ubicacion = new LatLng(Hashcanchas.get(key).getLatitud_cancha(), Hashcanchas.get(key).getLongitud_cancha());

                                                        Marker marker= mMap.addMarker(new MarkerOptions()

                                                                .anchor(0.0f, 1.0f)
                                                                .title(Hashcanchas.get(key).getNombre_complejo())
                                                                .position(ubicacion)
                                                                .icon(BitmapDescriptorFactory.fromBitmap(imagen))
                                                                .draggable(false)
                                                                .visible(true)
                                                                .flat(true)

                                                        );
                                                        MarkersHash.put(Integer.toString(Hashcanchas.get(key).getId_cancha()),marker);


                                                        if(ii<1) {
                                                            marker.showInfoWindow();

                                                            CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(ubicacion, 12F);
                                                            mMap.moveCamera(camUpd1);
                                                            id_cancha=Hashcanchas.get(key).getId_cancha();


                                                        }



                                                        ii++;
                                                    }

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
                        bandera = false;
                    }

                }

                public void onProviderDisabled(String provider) {
                    Log.i("LocAndroid", "Proveedor caido");
                    bandera = false;
                    btnBuscar.setVisibility(View.VISIBLE);
                    Toast.makeText(MapaCanchas.this, "Encienda el GPS para ver las canchas cercanas", Toast.LENGTH_SHORT).show();

                }

                public void onProviderEnabled(String provider) {


                    Log.i("LocAndroid", "Proveedor Habilitado");
                    bandera=true;
                    btnBuscar.setVisibility(View.INVISIBLE);

                }

                public void onStatusChanged(String provider, int status, Bundle extras) {

                    bandera = true;

                }
            };

            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locListener);
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locListener);




        mMap.setMyLocationEnabled(true);



        imagen = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_action_view_carousel);



        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Operaciones http

            url = servidor+"mapa_canchas.php?latitud="+latitud+"&longitud="+longitud;

            if(latitud != null){

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

                                            Hashcanchas.put(objeto.getString("id_complejo"),new Cancha(Integer.parseInt(objeto.getString("id_complejo")),objeto.getString("nombre_complejo"),objeto.getDouble("latitud_complejo"),objeto.getDouble("longitud_complejo")));




                                        } catch (JSONException e) {
                                            String TAG = "error";
                                            Log.e(TAG, "Error de parsing: "+ e.getMessage());
                                        }


                                    }

                                    for(String key : Hashcanchas.keySet()){

                                        int ii = 0;

                                        //int precio = Hashcanchas.get(key).getPrecio_cancha();
                                        LatLng ubicacion = new LatLng(Hashcanchas.get(key).getLatitud_cancha(), Hashcanchas.get(key).getLongitud_cancha());

                                        Marker marker= mMap.addMarker(new MarkerOptions()

                                                .anchor(0.0f, 1.0f)
                                                .title(Hashcanchas.get(key).getNombre_complejo())
                                                .position(ubicacion)
                                                .icon(BitmapDescriptorFactory.fromBitmap(imagen))
                                                .draggable(false)
                                                .visible(true)
                                                .flat(true)

                                        );
                                        MarkersHash.put(Integer.toString(Hashcanchas.get(key).getId_cancha()),marker);


                                        if(ii<1) {
                                            marker.showInfoWindow();

                                            CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(ubicacion, 12F);
                                            mMap.moveCamera(camUpd1);
                                            id_cancha=Hashcanchas.get(key).getId_cancha();


                                        }



                                        ii++;
                                    }

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




        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {



                        switch (menuItem.getItemId()) {
                            case R.id.menu_seccion_1: {
                                Log.i("NavigationView", "Pulsada 1");
                                Bundle bun = new Bundle();
                                bun.putString("id_user", id_user);
                                Intent inte = new Intent(MapaCanchas.this, BuscarCancha.class);
                                inte.putExtras(bun);
                                startActivity(inte);
                                break;

                            }



                            case R.id.menu_seccion_2: {
                                Log.i("NavigationView", "Pulsada 2");
                                Bundle bun = new Bundle();
                                bun.putString("id_user", id_user);
                                Intent inte = new Intent(MapaCanchas.this, MisReservas.class);
                                inte.putExtras(bun);
                                startActivity(inte);
                                break;
                            }



                            case R.id.menu_seccion_4: {
                                Bundle bun = new Bundle();
                                bun.putString("id_user", id_user);
                                Intent inte = new Intent(MapaCanchas.this, MiPerfil.class);
                                inte.putExtras(bun);
                                startActivity(inte);
                                break;
                            }

                            case R.id.menu_opcion_1: {
                                Log.i("NavigationView", "Pulsada opción 1");


                                SQLiteDatabase db = usdbh.getWritableDatabase();


                                if(db != null)
                                {


                                    Cursor c = db.rawQuery("SELECT id_user FROM usuario", null);

                                    if (c.moveToFirst()) {
                                        //Recorremos el cursor hasta que no haya más registros

                                        String id_u = c.getString(0);


                                        //Insertamos los datos en la tabla Usuarios
                                        db.execSQL("DELETE FROM Usuario");

                                        c.close();
                                        //Cerramos la base de datos
                                        db.close();

                                        Intent inte = new Intent(MapaCanchas.this, Inicio.class);
                                        startActivity(inte);
                                        finish();




                                    }

                                    else {

                                        Intent inte = new Intent(MapaCanchas.this, Inicio.class);
                                        startActivity(inte);
                                        finish();

                                    }





                                }

                                else{
                                    Intent inte = new Intent(MapaCanchas.this, Inicio.class);
                                    startActivity(inte);
                                    finish();
                                }


                                break;
                            }
                            case R.id.menu_opcion_2: {
                                Log.i("NavigationView", "Pulsada opción 2");
                                Intent inte = new Intent(MapaCanchas.this, Inicio.class);
                                startActivity(inte);
                                finish();
                                break;
                            }

                        }


                        drawerLayout.closeDrawers();

                        return true;
                    }


                });



    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraChangeListener(this);




        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
    @Override
    public void onInfoWindowClick(Marker marker) {

        for (String key : MarkersHash.keySet()) {

            if(MarkersHash.get(key).equals(marker)) {


                id_cancha=Hashcanchas.get(key).getId_cancha();

                Intent inte = new Intent(MapaCanchas.this, InfoCancha.class);

                Bundle bun = new Bundle();
                bun.putString("id_cancha", String.valueOf(id_cancha));
                bun.putString("id_user",id_user);

                inte.putExtras(bun);
                startActivity(inte);


            }

        }

    }
});


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // if marker source is clicked
                marker.showInfoWindow();

                return true; ///// return onclick
            }
        });




    }

    @Override
    public void onMapClick(LatLng point) {

        //Toast.makeText(MapaCanchas.this, point.toString(), Toast.LENGTH_SHORT).show();

        if(bandera){

        }else{
            btnBuscar.setVisibility(View.VISIBLE);
            latitud = String.valueOf(point.latitude);
            longitud = String.valueOf(point.longitude);
        }

    }

    @Override
    public void onMapLongClick(LatLng point) {


    }

    @Override
    public void onCameraChange(final CameraPosition position) {


        if(bandera){

        }else{

            btnBuscar.setVisibility(View.VISIBLE);
            latitud=String.valueOf(position.target.latitude);
            longitud= String.valueOf(position.target.longitude);

            //Toast.makeText(MapaCanchas.this, latitud + " " + longitud, Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            //...
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
            url2 = servidor+"mi_perfil.php?id_usuario="+id_user;

            final JsonObjectRequest getRequest;
            getRequest = new JsonObjectRequest(Request.Method.GET,url2,null,
                    new Response.Listener<JSONObject>(){


                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            try {

                                txtNom = (TextView) findViewById(R.id.Txtnom_user);
                                txtEmail = (TextView) findViewById(R.id.TxtEmail);
                                ImgUser = (NetworkImageView) findViewById(R.id.ImgMiperf);
                                if(txtNom!= null) {

                                    Log.d("Response", response.getString("nombre_usuario") + " , " + response.getString("email") + " , " + response.getString("nombre_ciudad"));
                                    txtNom.setText(response.getString("nombre_usuario"));
                                    txtEmail.setText(response.getString("email"));
                                    location = response.getString("nombre_ciudad");
                                    ImageLoader imageLoader = MatchSingleton.getInstance(getApplicationContext()).getImageLoader();
// Petición
                                    if(id_user.equals("1")){

                                        ImgUser.setImageUrl("https://s3.amazonaws.com/match-contentdelivery-mobilehub-711091535/icono_persona.png", imageLoader);

                                    }
                                    else{
                                       // Toast.makeText(MapaCanchas.this, id_user, Toast.LENGTH_SHORT).show();//

                                        ImgUser.setImageUrl(servidor+response.getString("foto_usuario"), imageLoader);


                                        SQLiteDatabase db = usdbh.getWritableDatabase();

                                        if(db != null) {


                                            //Insertamos los datos en la tabla Usuarios
                                            //Insertamos los datos en la tabla Usuarios
                                            db.execSQL("UPDATE Usuario set ciudad = " + "'" +location + "'" + "," + "ultima_localizacion=" + "'" +location+ "'");


                                            //Cerramos la base de datos
                                            db.close();
                                        }

                                            if(Geocoder.isPresent()){
                                                try {

                                                    Geocoder gc = new Geocoder(MapaCanchas.this);
                                                    addresses= gc.getFromLocationName(location, 1); // get the found Address Objects

                                                    ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                                                    for(Address a : addresses){
                                                        if(a.hasLatitude() && a.hasLongitude()){
                                                            ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                                                                if(bandera2&&(loc==null)) {
                                                                    CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(ll.get(0), 13F);
                                                                    mMap.moveCamera(camUpd1);
                                                                    bandera2=false;
                                                                }

                                                        }
                                                    }
                                                } catch (IOException e) {
                                                    // handle the exception
                                                }
                                            }

                                    }

                                }

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

    @Override
    protected void onDestroy() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            Toast.makeText(MapaCanchas.this, "GPS Sin permiso, instale nuevamente la aplicacion", Toast.LENGTH_SHORT).show();// display toast


            return;
        }else{

                if (loc!=null){
                    locManager.removeUpdates(locListener);
                    locManager = null;
                    latitud=null;
                    longitud=null;

                }





        }
        super.onDestroy();
    }



    //Paso 1. Verificar permiso
    private void verifyPermission() {

        //WRITE_EXTERNAL_STORAGE tiene implícito READ_EXTERNAL_STORAGE porque pertenecen al mismo
        //grupo de permisos

        int writePermission = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            writePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {

        }
    }


    //Paso 2: Solicitar permiso
    private void requestPermission() {
        //shouldShowRequestPermissionRationale es verdadero solamente si ya se había mostrado
        //anteriormente el dialogo de permisos y el usuario lo negó
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showSnackBar();
        } else {
            //si es la primera vez se solicita el permiso directamente
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Si el requestCode corresponde al que usamos para solicitar el permiso y
        //la respuesta del usuario fue positiva
        if (requestCode == LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent inte = new Intent(MapaCanchas.this, MainActivity.class);
                startActivity(inte);
                finish();

            } else {
                showSnackBar();
            }
        }
    }


    private void showSnackBar() {
        Snackbar.make(mLayout, "Se necesita la configuracion de permisos",
                Snackbar.LENGTH_LONG)
                .setAction("CONFIGURACION", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openSettings();
                    }
                })
                .show();
    }


    public void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
