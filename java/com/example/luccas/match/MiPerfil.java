package com.example.luccas.match;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;


public class MiPerfil extends AppCompatActivity {

    String servidor = "http://52.72.248.41/";
    String url;
    String id_user;

    TextView txtNom;
    TextView txtEmail;
    TextView txtTel;
    NetworkImageView ImgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Bundle bundle = this.getIntent().getExtras();
        id_user=bundle.getString("id_user");


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Operaciones http
            url = servidor+"mi_perfil.php?id_usuario="+id_user;

            final JsonObjectRequest getRequest;
            getRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                    new Response.Listener<JSONObject>(){


                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            try {

                                txtNom = (TextView) findViewById(R.id.nomUser);
                                txtEmail = (TextView) findViewById(R.id.email);
                                ImgUser = (NetworkImageView) findViewById(R.id.imgUser);
                                txtTel = (TextView) findViewById(R.id.telefono);
                                if(txtNom!= null) {

                                    txtNom.setText(response.getString("nombre_usuario"));
                                    txtEmail.setText(response.getString("email"));
                                    txtTel.setText((response.getString("telefono_usuario")));

                                    ImageLoader imageLoader = MatchSingleton.getInstance(getApplicationContext()).getImageLoader();

                                    if(id_user.equals("1")){

                                        ImgUser.setImageUrl("https://s3.amazonaws.com/match-contentdelivery-mobilehub-711091535/icono_persona.png", imageLoader);

                                    }
                                    else{


                                        ImgUser.setImageUrl(servidor+response.getString("foto_usuario"), imageLoader);
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
