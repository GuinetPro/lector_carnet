package com.poch.asynctask.pdf417.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.poch.asynctask.pdf417.R;
import com.poch.asynctask.pdf417.adapters.BitacoraAdapter;
import com.poch.asynctask.pdf417.models.Bitacora;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ricardo.gutierrez on 18-01-2016.
 */
public class ListaBitacora extends AppCompatActivity {

    public static RecyclerView recyclerView;
    public static Realm realm ;
    private RealmList<Bitacora> bitacoras;
    public static BitacoraAdapter mAdapter;
    private TextView datos;
    private Button btnAgregar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_bitacora);

        /* se muestrab el boton volver */

        setTitle("Lista de  Bitacoras");

        realm = Realm.getInstance(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycle);
        datos = (TextView) findViewById(R.id.datos);
        btnAgregar  = (Button) findViewById(R.id.btnAgregar);
        SharedPreferences sp = getSharedPreferences("userData", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RealmResults<Bitacora> realResultsList = realm.where(Bitacora.class).findAll();


        bitacoras= new RealmList<Bitacora>();

        for(Bitacora doc : realResultsList){
            bitacoras.add(doc);
        }


        mAdapter = new BitacoraAdapter(bitacoras,this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Bundle extras = getIntent().getExtras();

        String value = extras.getString("datos");

        datos.setText(value);

        String[] splited = value.split("\\s");


        for (int x=0;x<splited.length;x++){
            Log.v("data", "[" + x + "] " + splited[x].replaceAll("[^a-zA-Z0-9]", ""));

            if (x == 1) {
                editor.putString("nombre", splited[x].replaceAll("[^a-zA-Z0-9]", "") );
                editor.apply();
            }


        }

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ListaBitacora.this, CrearBitacora.class);
                startActivity(i);
                finish();

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


    @Override
    public void onBackPressed() {
        return;
    }

}
