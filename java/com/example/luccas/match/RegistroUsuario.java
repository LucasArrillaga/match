package com.example.luccas.match;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class RegistroUsuario extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    int RESULT_LOAD_IMAGE = 3;


    String id_user;
    String item;


    String nombre;
    String email;
    String telefono;
    String contra;

    apiBd usdbh = new apiBd(this, "DBUsuario", null, 1);

    String servidor = "http://52.72.248.41/";
    ImageView imgCancha;
    Bitmap bm_cancha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgCancha = (ImageView) findViewById(R.id.imgUsuario);

        final EditText txtNombre = (EditText) findViewById(R.id.txtNombre);
        final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        final EditText txtTelefono = (EditText) findViewById(R.id.txtTelefono);

        final EditText txtContra = (EditText) findViewById(R.id.txtContra);



        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Ciudades, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                nombre= String.valueOf(txtNombre.getText());
                email= String.valueOf(txtEmail.getText());
                telefono= String.valueOf(txtTelefono.getText());
                contra = String.valueOf(txtContra.getText());


                final ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();



                if (networkInfo != null && networkInfo.isConnected()) {
                    // Operaciones http
                    final ProgressDialog loading = ProgressDialog.show(RegistroUsuario.this,"Creando perfil...","Espere por favor...",false,false);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, servidor + "crear_usuario.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String respuesta) {
                                    //Disimissing the progress dialog
                                    loading.dismiss();
                                    //Showing toast message of the response

                                    if (respuesta.equals("error1")  || respuesta.equals("error2") || respuesta.equals("error3") ){

                                        Snackbar.make(view, respuesta, Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();

                                    }else {

                                        id_user=respuesta;

                                        if(id_user!=null){


                                            SQLiteDatabase db = usdbh.getWritableDatabase();


                                            if(db != null)
                                            {

                                                //Insertamos los datos en la tabla Usuarios
                                                db.execSQL("INSERT INTO Usuario (id_user,ciudad,ultima_localizacion)  VALUES (" + id_user + "," + "'" +item + "'" + "," + "'" + item + "'" +") ");

                                                //Cerramos la base de datos
                                                db.close();

                                                Intent intent = new Intent(RegistroUsuario.this,MapaCanchas.class);

                                                Bundle bun = new Bundle();
                                                bun.putString("id_user", id_user);
                                                intent.putExtras(bun);
                                                startActivity(intent);
                                                finish();

                                            }




                                        }



                                    }



                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    //Dismissing the progress dialog
                                    loading.dismiss();

                                    //Showing toast

                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            //Converting Bitmap to String
                            String image = getStringImage(bm_cancha);

                            //Creating parameters
                            Map<String,String> params = new Hashtable<String, String>();
                            //Adding parameters
                            params.put("foto_usuario", image);
                            params.put("nom_user", nombre);
                            params.put("telefono", telefono);
                            params.put("email",email);
                            params.put("contrasena",contra);
                            params.put("tipo_usuario", "jugador");
                            params.put("ciudad",item);
                            //returning parameters
                            return params;
                        }
                    };

                    MatchSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


                }

                else{

                    Snackbar.make(view, "Sin conexion a internet", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }
        });







    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


/////////////////////////////////////////////////////////////////////////////////////////////////////
        // FOTO DESDE CAMARA


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //// FOTO DESDE GALERIA

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {


            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bm_cancha = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imgCancha.setImageBitmap(bm_cancha);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_registro_usuario, menu);

        MenuItem galeria = menu.findItem(R.id.galeria);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            case R.id.galeria:

                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMAGE);

                return true;




        }
        return super.onOptionsItemSelected(item);
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }




    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
