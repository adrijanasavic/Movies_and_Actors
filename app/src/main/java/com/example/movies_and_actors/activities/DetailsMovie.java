package com.example.movies_and_actors.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.movies_and_actors.R;
import com.example.movies_and_actors.adapters.AdapterGlumci;
import com.example.movies_and_actors.adapters.SliderPagerAdapter;
import com.example.movies_and_actors.adapters.VideoAdapter;
import com.example.movies_and_actors.model_movie.Backdrop;
import com.example.movies_and_actors.model_movie.Cast;
import com.example.movies_and_actors.model_movie.Genre;
import com.example.movies_and_actors.model_movie.MovieDetails;
import com.example.movies_and_actors.model_movie.Result;
import com.example.movies_and_actors.model_omdb.Details;
import com.example.movies_and_actors.net.MyService1;
import com.example.movies_and_actors.net.MyService2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movies_and_actors.net.MyServiceContract.APIKEY1;
import static com.example.movies_and_actors.net.MyServiceContract.APIKEY2;
import static com.example.movies_and_actors.net.MyServiceContract.IMAGEBASEURL;


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

        getAllMoviesData(id);

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

    public String firstFour(String godina) {
        return godina.length() < 4 ? godina : godina.substring(0, 4);
    }

    public void getGenre(List<Genre> zandrovi) {
        try {
            if (zandrovi.size() >= 3) {
                zanr1.setText(zandrovi.get(0).getName());
                zanr2.setText(zandrovi.get(1).getName());
                zanr3.setText(zandrovi.get(2).getName());
            }
            if (zandrovi.size() == 2) {
                zanr1.setText(zandrovi.get(0).getName());
                zanr2.setText(zandrovi.get(1).getName());
            }
            if (zandrovi.size() == 1) {
                zanr1.setText(zandrovi.get(0).getName());
            }
            if (zandrovi.size() == 0) {
                zanr1.setVisibility(View.GONE);
                zanr2.setVisibility(View.GONE);
                zanr3.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getMovieData(final String imbd_id) {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("apikey", APIKEY2);
        queryParams.put("i", imbd_id);

        MyService2.apiInterface().getMovieData(queryParams)
                .enqueue(new Callback<Details>() {
                    public void onResponse(Call<Details> call, Response<Details> response) {
                        if (response.code() == 200) {
                            Log.d("REZ", "200");

                            detalji = response.body();
                            if (detalji != null) {

                                direktor.setText(detalji.getDirector());
                                pisci.setText(detalji.getWriter());

                                if (detalji.getImdbRating() == null) {
                                    linearLayout.setVisibility(View.GONE);
                                } else if (detalji.getImdbRating().equals("N/A")) {
                                    linearLayout.setVisibility(View.GONE);
                                } else {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    imdbOcena.setText(detalji.getImdbRating());
                                    imdbVotes.setText(detalji.getImdbVotes());
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Details> call, Throwable t) {
                        Toast.makeText(DetailsMovie.this, "No information about the movie", Toast.LENGTH_SHORT).show();
                        Toast.makeText(DetailsMovie.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void getAllMoviesData(int id) {
        MyService1.apiInterface().getAllMoviesData(id, APIKEY1, "credits,images,videos", "en,null")
                .enqueue(new Callback<MovieDetails>() {
                    @Override
                    public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                        if (response.code() == 200) {
                            try {
                                movieDetails = response.body();

                                getMovieData(movieDetails.getImdbId());

                                getGenre(movieDetails.getGenres());
                                runtime.setText(movieDetails.getRuntime() + "min");
                                nazivFilma.setText(movieDetails.getTitle());
                                nazivFilma2.setText(movieDetails.getTitle());
                                plotFilma.setText(movieDetails.getOverview());
                                godina.setText(firstFour(movieDetails.getReleaseDate()));
                                Glide.with(DetailsMovie.this)
                                        .load(IMAGEBASEURL + movieDetails.getPosterPath())
                                        .into(slikaFilma);


                                movieCast = movieDetails.getCredits().getCast();

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailsMovie.this, LinearLayoutManager.HORIZONTAL, false);
                                rvListaGlumaca.setLayoutManager(linearLayoutManager);

                                adapter = new AdapterGlumci(DetailsMovie.this, movieCast);

                                rvListaGlumaca.setAdapter(adapter);

                                if (movieDetails.getImages().getBackdrops().isEmpty()) {
                                    frameLayout.setVisibility(View.GONE);
                                    nazivFilma2.setVisibility(View.VISIBLE);
                                } else {
                                    backdrop = movieDetails.getImages().getBackdrops();

                                    adapterSlider = new SliderPagerAdapter(DetailsMovie.this, backdrop);
                                    sliderpager.setAdapter(adapterSlider);

                                    Timer timer = new Timer();
                                    timer.scheduleAtFixedRate(new DetailsMovie.SliderTimer(), 4000, 6000);
                                }


                                List<Result> videoResult = movieDetails.getVideos().getResults();
                                List<Result> videoResult2 = new ArrayList<>();

                                for (int i = 0; i < videoResult.size(); i++) {
                                    if (videoResult.get(i).getType().equals("Trailer")) {
                                        videoResult2.add(videoResult.get(i));
                                        if (videoResult.isEmpty()) {
                                            trailersLayout.setVisibility(View.GONE);
                                        } else {
                                            trailersLayout.setVisibility(View.VISIBLE);

                                            videoRecycler.setHasFixedSize(true);
                                            videoRecycler.setLayoutManager(new LinearLayoutManager(DetailsMovie.this, LinearLayoutManager.HORIZONTAL, false));

                                            VideoAdapter videoAdapter = new VideoAdapter(DetailsMovie.this, videoResult2);

                                            videoRecycler.setAdapter(videoAdapter);
                                        }

                                    }
                                }


                            } catch (NullPointerException e) {
                                Toast.makeText(DetailsMovie.this, "Ne postoji film/serija sa tim nazivom", Toast.LENGTH_SHORT).show();
                                Toast.makeText(DetailsMovie.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.v("response.code", " Greska sa serverom");
                            Toast.makeText(DetailsMovie.this, "No information about the movie", Toast.LENGTH_SHORT).show();
                            Toast.makeText(DetailsMovie.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetails> call, Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(DetailsMovie.this, "No information about the movie", Toast.LENGTH_SHORT).show();
                        Toast.makeText(DetailsMovie.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class SliderTimer extends TimerTask {

        public int getCount() {
            if (backdrop.size() > 10) {
                return 10;
            } else {
                return backdrop.size();
            }
        }

        @Override
        public void run() {


            DetailsMovie.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderpager.getCurrentItem() < getCount() - 1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem() + 1);
                    } else
                        sliderpager.setCurrentItem(0);
                }
            });


        }
    }
}
