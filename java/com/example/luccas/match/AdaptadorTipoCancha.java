package com.example.luccas.match;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luccas on 16/11/16.
 */

public class AdaptadorTipoCancha extends RecyclerView.Adapter<AdaptadorTipoCancha.TipoCViewHolder>
        implements View.OnClickListener {


    private View.OnClickListener listener;
    private ArrayList<TipoCancha> datoshl;

    @Override
    public AdaptadorTipoCancha.TipoCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tipo_cancha, parent, false);
        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        AdaptadorTipoCancha.TipoCViewHolder tcvh = new AdaptadorTipoCancha.TipoCViewHolder(itemView);

        return tcvh;

    }

    @Override
    public void onBindViewHolder(AdaptadorTipoCancha.TipoCViewHolder holder, int position) {
        TipoCancha item = datoshl.get(position);

        holder.bindTipoCancha(item);
    }

    @Override
    public int getItemCount() {
        return datoshl == null ? 0 : datoshl.size();
    }
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }


    public class TipoCViewHolder extends RecyclerView.ViewHolder {

        private TextView tipo_cancha;



        public TipoCViewHolder(View itemView) {
            super(itemView);

            tipo_cancha = (TextView)itemView.findViewById(R.id.tipoCancha);



        }
        public void bindTipoCancha(TipoCancha e) {
            tipo_cancha.setText("Fútbol " + e.getNum_jugadores());


        }
    }

    public AdaptadorTipoCancha(ArrayList<TipoCancha> datoshl) {
        this.datoshl = datoshl;
    }


}
