package com.example.luccas.match;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Inicio extends AppCompatActivity {



    String email;
    String pass;

    String servidor = "http://52.72.248.41/";
    String id_user;
    String estado;

    apiBd usdbh = new apiBd(this, "DBUsuario", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button btnInicio = (Button) findViewById(R.id.btnIngreso);
        Button btnReg = (Button) findViewById(R.id.btnRegistro);
        final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        final EditText txtPass = (EditText) findViewById(R.id.txtPass);

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url =servidor+"ingreso.php";






        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                email = String.valueOf(txtEmail.getText());
                pass = String.valueOf(txtPass.getText());


                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // Operaciones http

                    HashMap<String, String> params = new HashMap<>();

                    params.put("email", email);
                    params.put("tipo_usuario", "jugador");
                    params.put("contrasena", pass);

                    final JSONObject jsObj = new JSONObject(params);
                    final JsonObjectRequest postRequest;


                    postRequest = new JsonObjectRequest(Request.Method.POST,url , jsObj,
                            new Response.Listener<JSONObject>()
                            {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // response
                                    try {

                                        Log.d("Response", response.getString("id_user"));


                                        estado = response.getString("estado");

                                        if(estado.equals("Error1") || estado.equals("Error2") ){
                                            Snackbar.make(view, "Error. Compruebe email y contraseña", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                        }
                                        else{

                                            id_user = response.getString("id_user");

                                            SQLiteDatabase db = usdbh.getWritableDatabase();

                                            if(db != null) {


                                                //Insertamos los datos en la tabla Usuarios
                                                db.execSQL("INSERT INTO Usuario (id_user)  VALUES (" + id_user + ")");


                                                //Cerramos la base de datos
                                                db.close();
                                            }

                                            Intent inte = new Intent(Inicio.this, MapaCanchas.class);
                                            Bundle bun = new Bundle();
                                            bun.putString("id_user", id_user);
                                            inte.putExtras(bun);
                                            startActivity(inte);
                                            finish();
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Snackbar.make(view, "Error. Compruebe email y contraseña", Snackbar.LENGTH_LONG)
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
                            }
                    );
                    // Añadir petición a la cola
                    MatchSingleton.getInstance(getApplicationContext()).addToRequestQueue(postRequest);



                }else {
                    Snackbar.make(view, "Usted no esta conectado a internet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });


        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, RegistroUsuario.class);
                startActivity(intent);
                finish();
            }
        });











    }

}
