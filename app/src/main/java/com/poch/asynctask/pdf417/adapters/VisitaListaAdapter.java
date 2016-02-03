package com.poch.asynctask.pdf417.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.poch.asynctask.pdf417.R;
import com.poch.asynctask.pdf417.models.Visita;

import java.util.List;

/**
 * Created by ricardo.gutierrez on 25-01-2016.
 */
public class VisitaListaAdapter  extends ArrayAdapter<Visita> {


    private Context context;
    private List<Visita> visitas;

    public VisitaListaAdapter(Context context,  List<Visita> visitas ) {

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
        View item = inflater.inflate(R.layout.visitas_lista, null);

        TextView nombre = (TextView) item.findViewById(R.id.nombre_visita);
        nombre.setText(visitas.get(position).getNombre());

        TextView rut = (TextView) item.findViewById(R.id.rut);
        rut.setText(visitas.get(position).getRut());


        return item;


    }


}
