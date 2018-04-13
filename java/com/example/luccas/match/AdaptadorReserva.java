package com.example.luccas.match;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by luccas on 06/07/16.
 */
public class AdaptadorReserva extends RecyclerView.Adapter<AdaptadorReserva.ReservaViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    private ArrayList<Reserva> datosReserva;
    Context context;



    @Override
    public AdaptadorReserva.ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemreserva, parent, false);
        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        ReservaViewHolder reservaVH = new ReservaViewHolder(itemView);
        context = parent.getContext();
        return reservaVH;
    }

    @Override
    public void onBindViewHolder(AdaptadorReserva.ReservaViewHolder holder, int position) {
        Reserva item = datosReserva.get(position);

        holder.bindReserva(item);
    }

    @Override
    public int getItemCount() {
        return datosReserva == null ? 0 : datosReserva.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public class ReservaViewHolder extends RecyclerView.ViewHolder {

        String servidor = "http://52.72.248.41/";

        private CardView card;
        private TextView txtHoraFecha;
        private TextView txtNomCancha;
        private TextView nomComplejo;
        private NetworkImageView imgCancha;


        public ReservaViewHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_view);
            txtHoraFecha = (TextView)itemView.findViewById(R.id.FechaHora);
            nomComplejo =(TextView)itemView.findViewById(R.id.nomComplejo);
            txtNomCancha = (TextView)itemView.findViewById(R.id.NomCancha);
            imgCancha = (NetworkImageView) itemView.findViewById(R.id.imgCancha);


        }
        public void bindReserva(Reserva e) {
            txtHoraFecha.setText(e.getFecha());
            nomComplejo.setText(e.getNom_complejo());
            txtNomCancha.setText("Cancha " + e.getNom_cancha());
            ImageLoader imageLoader = MatchSingleton.getInstance(context.getApplicationContext()).getImageLoader();
            imgCancha.setImageUrl(servidor+e.getFoto_cancha(), imageLoader);


        }

    }


        public AdaptadorReserva(ArrayList<Reserva> datos) {
            this.datosReserva = datos;
        }
}
