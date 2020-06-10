package com.example.movies_and_actors.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies_and_actors.R;
import com.example.movies_and_actors.activities.DetailsMovie;
import com.example.movies_and_actors.model_actor.Cast;

import java.util.List;

import static com.example.movies_and_actors.net.MyServiceContract.IMAGEBASEURL;

public class AdapterSviFilmovi extends RecyclerView.Adapter<AdapterSviFilmovi.MyViewHolder> {

    private Context context;
    private List<Cast> searchItems;

    public String firstFour(String godina) {
        if (godina == null) {
            return "";
        }
        if (godina.length() > 4) {
            return godina.substring( 0, 4 );
        } else {
            return "";
        }

    }

    public AdapterSviFilmovi(Context context, List<Cast> searchItems) {
        this.context = context;
        this.searchItems = searchItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.rv_prikaz_svih_glumaca, parent, false );

        return new MyViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.nazivFIlma.setText( searchItems.get( position ).getTitle() );
//        Picasso.with(context).load(searchItems.get(position).getPosterPath()).into(holder.slikaGlumca);

        if (searchItems.get( position ).getPosterPath() == null) {
            holder.slikaFilma.setImageResource( R.drawable.image );
            holder.slikaFilma.setScaleType( ImageView.ScaleType.FIT_CENTER );
        } else {
            Glide.with( holder.itemView )
                    .load( IMAGEBASEURL + searchItems.get( position ).getPosterPath() )
                    .into( holder.slikaFilma );
        }


        holder.karakter.setText( firstFour( searchItems.get( position ).getReleaseDate() ) + "\n\n" + searchItems.get( position ).getCharacter() );


        holder.glavni_constraint_layout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - holder.lastClickTime < 1000) {
                    return;
                } else {
                    holder.lastClickTime = SystemClock.elapsedRealtime();
                    final Intent intent = new Intent( context, DetailsMovie.class );
                    intent.putExtra( "id", searchItems.get( position ).getId() );
                    context.startActivity( intent );
                }
            }
        } );

    }

    @Override
    public int getItemCount() {

        return searchItems.size();

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private TextView nazivFIlma;
        private TextView karakter;
        private ImageView slikaFilma;
        private ConstraintLayout glavni_constraint_layout;
        private long lastClickTime;

        MyViewHolder(@NonNull View itemView) {
            super( itemView );

            nazivFIlma = itemView.findViewById( R.id.ime_glumca );
            karakter = itemView.findViewById( R.id.ime_uloge );
            slikaFilma = itemView.findViewById( R.id.slika_glumca );
            glavni_constraint_layout = itemView.findViewById( R.id.svi_glumci_layout );
            lastClickTime = 0;

        }

    }
}
