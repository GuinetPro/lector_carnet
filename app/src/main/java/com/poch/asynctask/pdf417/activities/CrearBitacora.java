package com.poch.asynctask.pdf417.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.poch.asynctask.pdf417.R;
import com.poch.asynctask.pdf417.adapters.BitacoraAdapter;
import com.poch.asynctask.pdf417.adapters.VisitaAdapter;
import com.poch.asynctask.pdf417.models.Bitacora;
import com.poch.asynctask.pdf417.models.Visita;
import com.poch.asynctask.pdf417.models.VisitaRealm;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ricardo.gutierrez on 18-01-2016.
 */
public class CrearBitacora extends ActionBarActivity {


    private ImageButton btnvisitas;
    private List<Visita> visitaList;
    private VisitaAdapter  adapter;
    private ListView list;
    private EditText fecha_ingreso,motivo,nombre;
    private Button crearBitacora;
    private static final String  SERVER = "http://www.pochrider.cl/bitacora_servidores/";

    private Toolbar toolbar;

    ArrayList<Visita> visitaList2 = new ArrayList<Visita>();

    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_bitacora);
        setTitle("Crear Bitacora");
        realm = Realm.getInstance(this);


        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences sp = getSharedPreferences("userData", Context.MODE_PRIVATE);




        toolbar = (Toolbar) findViewById( R.id.toolbar );
        btnvisitas = (ImageButton)findViewById( R.id.visitas );

        crearBitacora = (Button)findViewById( R.id.buttonEnviar );

        nombre = (EditText)findViewById( R.id.nombre );
        motivo =  (EditText)findViewById( R.id.motivo );
        fecha_ingreso = (EditText)findViewById( R.id.fecha_ingreso );

        visitaList = new ArrayList<Visita>();

        adapter = new  VisitaAdapter(this,visitaList2);

        list = (ListView) findViewById(R.id.listView);

        list.setAdapter(adapter);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());

        fecha_ingreso.setText(timeStamp);
        nombre.setText(sp.getString("nombre",""));

        btnvisitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(CrearBitacora.this);

                View promptView = layoutInflater.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CrearBitacora.this);


                final EditText nombre = (EditText) promptView.findViewById(R.id.nombre_visita);

                final EditText rut = (EditText) promptView.findViewById(R.id.rut);


                // set prompts.xml to be the layout file of the alertdialog builder
                alertDialogBuilder.setView(promptView);
                // setup a dialog window
                alertDialogBuilder
                        .setTitle("Ingresar Visita")
                        .setCancelable(false)
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result

                                if (TextUtils.isEmpty(nombre.getText().toString()) || TextUtils.isEmpty(rut.getText().toString())) {

                                    return;
                                } else {
                                    visitaList2.add(new Visita(nombre.getText().toString(), rut.getText().toString()));

                                    adapter.notifyDataSetChanged();
                                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                }

                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alertD = alertDialogBuilder.create();

                alertD.show();


            }
        });


        crearBitacora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());



                realm.beginTransaction();

                String uid = UUID.randomUUID().toString();
                Bitacora bit = new Bitacora();
                bit.setId(uid);
                bit.setMotivo(motivo.getText().toString());
                bit.setFechaIngreso(fecha_ingreso.getText().toString());
                bit.setFechaSalida(timeStamp);
                bit.setNombrePersona(nombre.getText().toString());

                realm.copyToRealmOrUpdate(bit);

                for( Visita vi : visitaList2){

                    VisitaRealm visitaBueva = new VisitaRealm();
                    visitaBueva.setId(UUID.randomUUID().toString());
                    visitaBueva.setBitacoraId(uid);
                    visitaBueva.setNombre(vi.getNombre());
                    visitaBueva.setRut(vi.getRut());
                    realm.copyToRealmOrUpdate(visitaBueva);
                }

                realm.commitTransaction();
                realm.close();

                /* enviamos los datos al servidor */
                EnviarDatosBitacoraServidor(bit, visitaList2);

                // Use the AlertDialog.Builder to configure the AlertDialog.
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(CrearBitacora.this)
                                .setTitle("Mensaje de la Aplicacion")
                                .setMessage("Bitacora creada con exito");


                AlertDialog alertDialog = alertDialogBuilder.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent i2 = new Intent(CrearBitacora.this,ListaBitacora.class);
                        startActivity(i2);
                        finish();
                    }
                }, 3000);

            }
        });

    }


    public void EnviarDatosBitacoraServidor( Bitacora bitacora,ArrayList<Visita> listaVisitas ){


        AsyncHttpClient client = new AsyncHttpClient();
        client.setEnableRedirects(true);

        RequestParams data = new RequestParams();
        data.put("fecha_ingreso", bitacora.getFechaIngreso());
        data.put("fecha_salida", bitacora.getFechaSalida());
        data.put("motivo", bitacora.getMotivo());
        data.put("encargado", bitacora.getNombrePersona());
        data.put("id", bitacora.getId());


        List<Map<String, String>> listVisitas = new ArrayList<Map<String,
                String>>();

        for( Visita vi : visitaList2){

            Map<String, String> user1 = new HashMap<String, String>();
            user1.put("nombre", vi.getNombre());
            user1.put("rut", vi.getRut());
            user1.put("bitacora_id", bitacora.getId());
            listVisitas.add(user1);
        }


        data.put("visitas",listVisitas);

        client.post(SERVER + "api/bitacora/create", data, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    String m = response.getString("message");

                    boolean success = response.getBoolean("success");

                    Log.v("respuesta",response.getString("visitas"));

                    /* exitoso pasamos a estado sincronuizado */
                    if (success) {


                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable t) {

            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {


                int totProgress = (int) (((float) bytesWritten * 100) / totalSize);


                if (totProgress >= 100) {



                }

            }

        });


    }

    public void  EnviarDatosVisitasServidor(ArrayList<Visita> listaVisitas ){

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:

                SharedPreferences preferencias = getSharedPreferences("userData", Context.MODE_PRIVATE);;
                SharedPreferences.Editor editor=  preferencias.edit();
                editor.putString("rut", "");
                editor.apply();


                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                onBackPressed();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        Intent i2 = new Intent(this,ListaBitacora.class);
        startActivity(i2);
        finish();
    }


}
