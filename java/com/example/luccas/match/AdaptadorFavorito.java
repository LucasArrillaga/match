package com.example.luccas.match;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luccas on 26/07/16.
 */
public class AdaptadorFavorito extends RecyclerView.Adapter<AdaptadorFavorito.FavoritoViewHolder> implements View.OnClickListener{


    private View.OnClickListener listener;
    private ArrayList<Favorito> datosFavorito;




    @Override
    public AdaptadorFavorito.FavoritoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemfavorito, parent, false);
        itemView.setOnClickListener(this);
        //android:background="?android:attr/selectableItemBackground"

        FavoritoViewHolder favoritoVH = new FavoritoViewHolder(itemView);

        return favoritoVH;

    }

    @Override
    public void onBindViewHolder(AdaptadorFavorito.FavoritoViewHolder holder, int position) {

        Favorito item = datosFavorito.get(position);

        holder.bindFavorito(item);

    }

    @Override
    public int getItemCount() {
        return datosFavorito == null ? 0 : datosFavorito.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }


    public class FavoritoViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private TextView txtPrecio;
        private ImageView imgCancha;
        private TextView nomComplejo;
        private RatingBar calificacion;


        public FavoritoViewHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_view);
            txtPrecio = (TextView)itemView.findViewById(R.id.Precio);
            nomComplejo =(TextView)itemView.findViewById(R.id.nomComplejo);
            imgCancha = (ImageView) itemView.findViewById(R.id.imgCancha);
            calificacion = (RatingBar) itemView.findViewById(R.id.estrellas);


        }
        public void bindFavorito(Favorito e) {

            txtPrecio.setText("$" + e.getPrecio_cancha());
            calificacion.setRating(e.getCalificacion_cancha());
            nomComplejo.setText(e.getNom_complejo());
            imgCancha.setImageBitmap(e.getFoto_cancha());

        }

    }


    public AdaptadorFavorito(ArrayList<Favorito> datosFavorito) {
        this.datosFavorito = datosFavorito;
    }

}
