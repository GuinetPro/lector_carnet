package com.poch.asynctask.pdf417.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poch.asynctask.pdf417.models.Bitacora;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by ricardo.gutierrez on 18-01-2016.
 */
public class BitacoraAdapter extends RecyclerView.Adapter<BitacoraAdapter.ViewHolder>{

    private RealmList<Bitacora> bitacoras;
    private Realm realm ;
    private Context mContext;

    public BitacoraAdapter(RealmList<Bitacora> bitacoras,Context mContext) {
        this.bitacoras = bitacoras;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.bitacoras.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
