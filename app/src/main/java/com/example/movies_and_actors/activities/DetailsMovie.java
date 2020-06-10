package com.example.movies_and_actors.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.movies_and_actors.R;
import com.example.movies_and_actors.adapters.AdapterGlumci;
import com.example.movies_and_actors.adapters.SliderPagerAdapter;
import com.example.movies_and_actors.model_actor.Cast;
import com.example.movies_and_actors.model_movie.Backdrop;
import com.example.movies_and_actors.model_movie.MovieDetails;
import com.example.movies_and_actors.model_omdb.Details;

import java.util.List;

public class DetailsMovie extends AppCompatActivity {

    private TextView nazivFilma, plotFilma, godina, runtime, direktor, pisci, imdbOcena, imdbVotes, zanr1, zanr2, zanr3, nazivFilma2;
    private ImageView slikaFilma;
    private MovieDetails movieDetails;
    private List<Cast> movieCast;
    private List<Backdrop> backdrop;
    private FrameLayout frameLayout;
    private SliderPagerAdapter adapterSlider;

    private ViewPager sliderpager;

    private RecyclerView rvListaGlumaca;
    private AdapterGlumci adapter;
    private LinearLayout linearLayout, trailersLayout;

    private Button btn_see_all;

    private Details detalji;
    int id;
    private long lastClickTime = 0;
    private RecyclerView videoRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details_movie );

        initViews();

        id = getIntent().getIntExtra( "id", 0 );


        btn_see_all.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                } else {
                    lastClickTime = SystemClock.elapsedRealtime();

                    Intent intent = new Intent( DetailsMovie.this, ListActors.class );
                    intent.putExtra( "id", id );
                    startActivity( intent );
                }
            }
        } );
    }


    private void initViews() {

        nazivFilma = findViewById( R.id.nazivFilma );
        nazivFilma2 = findViewById( R.id.nazivFilma2 );
        slikaFilma = findViewById( R.id.slikaFilma );
        plotFilma = findViewById( R.id.tv_plot );

        rvListaGlumaca = findViewById( R.id.rvListaGlumaca );
        rvListaGlumaca.setHasFixedSize( true );
        rvListaGlumaca.setItemViewCacheSize( 20 );

        btn_see_all = findViewById( R.id.tv_see_all );

        godina = findViewById( R.id.film_godina );
        runtime = findViewById( R.id.film_trajanje );
        direktor = findViewById( R.id.ime_direktora );
        pisci = findViewById( R.id.ime_pisaca );
        imdbOcena = findViewById( R.id.imdb_ocena );
        imdbVotes = findViewById( R.id.imdb_votes );
        zanr1 = findViewById( R.id.film_zanr1 );
        zanr2 = findViewById( R.id.film_zanr2 );
        zanr3 = findViewById( R.id.film_zanr3 );

        linearLayout = findViewById( R.id.gridLayout );

        sliderpager = findViewById( R.id.slider_pager );
        frameLayout = findViewById( R.id.movie_frame );


        videoRecycler = findViewById( R.id.video_recycler );
        trailersLayout = findViewById( R.id.trailers_layout );
    }
}
