package com.example.luccas.match;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by luccas on 02/02/17.
 */

public class AdaptadorBusquedaCancha extends RecyclerView.Adapter<AdaptadorBusquedaCancha.BusquedaCanchaViewHolder>  implements View.OnClickListener{


    private View.OnClickListener listener;
    private ArrayList<BusquedaCancha> datosBusqueda;
    Context context;
    String servidor = "http://52.72.248.41/";


    @Override
    public AdaptadorBusquedaCancha.BusquedaCanchaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complejo_buscar, parent, false);
        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        BusquedaCanchaViewHolder BusquedaCanchaVH = new BusquedaCanchaViewHolder(itemView);
        context = parent.getContext();
        return BusquedaCanchaVH;

    }

    @Override
    public void onBindViewHolder(AdaptadorBusquedaCancha.BusquedaCanchaViewHolder holder, int position) {

        BusquedaCancha item = datosBusqueda.get(position);

        holder.bindBusqueda(item);

    }

    @Override
    public int getItemCount() {
        return datosBusqueda == null ? 0 : datosBusqueda.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }


    public class BusquedaCanchaViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private TextView txtPrecio;
        private NetworkImageView imgCancha;
        private TextView nomComplejo;
        private RatingBar calificacion;


        public BusquedaCanchaViewHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_view);
            txtPrecio = (TextView)itemView.findViewById(R.id.Precio);
            nomComplejo =(TextView)itemView.findViewById(R.id.nomComplejo);
            imgCancha = (NetworkImageView) itemView.findViewById(R.id.imgCancha);
            calificacion = (RatingBar) itemView.findViewById(R.id.estrellas);


        }
        public void bindBusqueda(BusquedaCancha e) {

            txtPrecio.setText("Desde "+ "$" + e.getPrecio_cancha());
            calificacion.setRating(e.getCalificacion_cancha());
            nomComplejo.setText(e.getNom_complejo());
            ImageLoader imageLoader = MatchSingleton.getInstance(context.getApplicationContext()).getImageLoader();
            imgCancha.setImageUrl(servidor+e.getFoto_cancha(), imageLoader);

        }

    }

    public AdaptadorBusquedaCancha(ArrayList<BusquedaCancha> datosBusqueda) {
        this.datosBusqueda = datosBusqueda;
    }

}
