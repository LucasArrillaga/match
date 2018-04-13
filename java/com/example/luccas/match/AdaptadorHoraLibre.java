package com.example.luccas.match;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luccas on 04/06/16.
 */
public class AdaptadorHoraLibre extends RecyclerView.Adapter<AdaptadorHoraLibre.HoraLViewHolder>
        implements View.OnClickListener {


    private View.OnClickListener listener;
    private ArrayList<HoraLibre> datoshl;

    @Override
    public HoraLViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemhorafecha, parent, false);
        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        HoraLViewHolder hlvh = new HoraLViewHolder(itemView);

        return hlvh;

    }

    @Override
    public void onBindViewHolder(HoraLViewHolder holder, int position) {
        HoraLibre item = datoshl.get(position);

        holder.bindHoraLibre(item);
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


    public class HoraLViewHolder extends RecyclerView.ViewHolder {

        private TextView txtHora;



        public HoraLViewHolder(View itemView) {
            super(itemView);

            txtHora = (TextView)itemView.findViewById(R.id.horaLibre);



        }
        public void bindHoraLibre(HoraLibre e) {
            txtHora.setText(e.getHora() + "hs");



        }
    }

    public AdaptadorHoraLibre(ArrayList<HoraLibre> datoshl) {
        this.datoshl = datoshl;
    }
}
