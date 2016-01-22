package com.poch.asynctask.pdf417.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.poch.asynctask.pdf417.R;
import com.poch.asynctask.pdf417.adapters.BitacoraAdapter;
import com.poch.asynctask.pdf417.adapters.VisitaAdapter;
import com.poch.asynctask.pdf417.models.Bitacora;
import com.poch.asynctask.pdf417.models.Visita;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ricardo.gutierrez on 18-01-2016.
 */
public class CrearBitacora extends AppCompatActivity {


    private ImageButton btnvisitas;
    private List<Visita> visitaList;
    private VisitaAdapter  adapter;
    private ListView list;
    private EditText fecha_ingreso,motivo;
    private Button crearBitacora;

    private Toolbar toolbar;

    ArrayList<Visita> visitaList2 = new ArrayList<Visita>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_bitacora);
        setTitle("Crear Bitacora");


        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        toolbar = (Toolbar) findViewById( R.id.toolbar );
        btnvisitas = (ImageButton)findViewById( R.id.visitas );
        fecha_ingreso = (EditText)findViewById( R.id.fecha_ingreso );
        crearBitacora = (Button)findViewById( R.id.buttonEnviar );
        motivo =  (EditText)findViewById( R.id.motivo );

        visitaList = new ArrayList<Visita>();

        adapter = new  VisitaAdapter(this,visitaList2);

        list = (ListView) findViewById(R.id.listView);

        list.setAdapter(adapter);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());



        fecha_ingreso.setText(timeStamp);

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

                //motivo.getText().toString();
                //guardamos la bitacora
                



            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
