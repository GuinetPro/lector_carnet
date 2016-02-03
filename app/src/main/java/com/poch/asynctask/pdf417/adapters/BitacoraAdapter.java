package com.poch.asynctask.pdf417.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poch.asynctask.pdf417.R;
import com.poch.asynctask.pdf417.models.Bitacora;
import com.poch.asynctask.pdf417.models.Visita;
import com.poch.asynctask.pdf417.models.VisitaRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by ricardo.gutierrez on 18-01-2016.
 */
public class BitacoraAdapter extends RecyclerView.Adapter<BitacoraAdapter.BitacoraViewHolder>{

    private RealmList<Bitacora> bitacoras;
    private Realm realm ;
    private Context mContext;
    private VisitaListaAdapter  visitaAdapter;

    public BitacoraAdapter(RealmList<Bitacora> bitacoras,Context mContext) {
        this.bitacoras = bitacoras;
        this.mContext = mContext;
    }

    @Override
    public BitacoraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bitacora, parent, false);
        return new BitacoraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BitacoraViewHolder holder, int position) {

        holder.persona_encargada.setText(bitacoras.get(position).getNombrePersona());
        holder.fecha_ingreso.setText(bitacoras.get(position).getFechaIngreso());


        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                realm =  Realm.getInstance(mContext);

                List<Visita> visitaList= new ArrayList<Visita>();

                RealmResults <VisitaRealm>  visitas = realm.where(VisitaRealm.class).equalTo("bitacoraId", bitacoras.get(position).getId()).findAll();

                LayoutInflater layoutInflater = LayoutInflater.from(mContext);

                View promptView = layoutInflater.inflate(R.layout.view_detalle_bitacora, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

                alertDialogBuilder.setView(promptView);


                final EditText nombre = (EditText) promptView.findViewById(R.id.nombre_visita);

                final EditText fecha_ingreso = (EditText) promptView.findViewById(R.id.fecha_ingreso);

                final EditText fecha_salida = (EditText) promptView.findViewById(R.id.salida);

                final EditText motivo = (EditText) promptView.findViewById(R.id.motivo);


                for (VisitaRealm vis : visitas){

                    Visita visita = new Visita(vis.getNombre(),vis.getRut());
                    visitaList.add(visita);
                }

                visitaAdapter = new  VisitaListaAdapter(promptView.getContext(),visitaList);
                visitaAdapter.notifyDataSetChanged();

                motivo.setText(bitacoras.get(position).getMotivo());
                nombre.setText(bitacoras.get(position).getNombrePersona());
                fecha_ingreso.setText(bitacoras.get(position).getFechaIngreso());
                fecha_salida.setText(bitacoras.get(position).getFechaSalida());



                ListView list = (ListView) promptView.findViewById(R.id.listView);

                list.setAdapter(visitaAdapter);

                // setup a dialog window
                alertDialogBuilder
                        .setTitle("Detalle Bitacora")
                        .setCancelable(false)
                        .setNegativeButton("Cerrar",
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
    }

    @Override
    public int getItemCount() {
        return this.bitacoras.size();
    }


    public class BitacoraViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener{

        private ItemClickListener clickListener;
        private TextView persona_encargada;
        private TextView fecha_ingreso;

        public BitacoraViewHolder(View itemView) {
            super(itemView);
            persona_encargada = (TextView) itemView.findViewById(R.id.persona_encargada);
            fecha_ingreso = (TextView) itemView.findViewById(R.id.fecha_ingreso);


            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }
        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }


    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }
}
