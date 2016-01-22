package com.poch.asynctask.pdf417.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.poch.asynctask.pdf417.R;
import com.poch.asynctask.pdf417.models.Visita;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo.gutierrez on 21-01-2016.
 */
public class VisitaAdapter  extends ArrayAdapter<Visita> {


    private Context context;
    private List<Visita> visitas;

    public VisitaAdapter(Context context,  List<Visita> visitas ) {

        super(context, R.layout.visitas, visitas);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.visitas = visitas;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {


        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.visitas, null);

        TextView nombre = (TextView) item.findViewById(R.id.nombre_visita);
        nombre.setText(visitas.get(position).getNombre());

        TextView rut = (TextView) item.findViewById(R.id.rut);
        rut.setText(visitas.get(position).getRut());


        Button btnEliminar= (Button) item.findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // setup a dialog window
                alertDialogBuilder
                        .setTitle("Realmente quieres Eliminar la visita?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result

                                visitas.remove(position);
                                VisitaAdapter.this.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No",
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

        return item;


    }



}
