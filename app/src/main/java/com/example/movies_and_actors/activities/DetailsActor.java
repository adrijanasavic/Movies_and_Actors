package com.example.movies_and_actors.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies_and_actors.R;
import com.example.movies_and_actors.adapters.AdapterFilmoviGlumca;
import com.example.movies_and_actors.model_actor.ActorData;
import com.example.movies_and_actors.model_actor.Cast;
import com.example.movies_and_actors.net.MyService1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movies_and_actors.net.MyServiceContract.APIKEY1;
import static com.example.movies_and_actors.net.MyServiceContract.IMAGEBASEURL;

public class DetailsActor extends AppCompatActivity {

    private TextView imeGlumca, bornGlumac, diedGlumac, biografijaGlumac, birthplaceGLumac;
    private ImageView slikaGlumca;
    private RecyclerView filmoviGlumca;
    private AdapterFilmoviGlumca adapter;

    private ActorData actorDetails;
    private List<com.example.movies_and_actors.model_actor.Cast> actorCast;
    private Cast pojedinacanActorCast;

    private Button btn_see_all;
    private long lastClickTime = 0;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_details_actor );

        initViews();

        id = getIntent().getIntExtra( "id", 0 );
        getAllActorsData( id );


        btn_see_all.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                } else {
                    lastClickTime = SystemClock.elapsedRealtime();

                    Intent intent = new Intent( DetailsActor.this, ListMovies.class );
                    intent.putExtra( "id", id );
                    startActivity( intent );
                }
            }
        } );

        biografijaGlumac.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                } else {
                    lastClickTime = SystemClock.elapsedRealtime();

                    Intent intent = new Intent( DetailsActor.this, Biography.class );
                    intent.putExtra( "id", id );
                    startActivity( intent );
                }
            }
        } );

    }

    private void initViews() {

        imeGlumca = findViewById( R.id.ime_glumaca );
        slikaGlumca = findViewById( R.id.slika_glumaca );
        bornGlumac = findViewById( R.id.born_glumac );
        diedGlumac = findViewById( R.id.died_glumac );
        biografijaGlumac = findViewById( R.id.biografija_glumca );
        birthplaceGLumac = findViewById( R.id.birthplace_glumac );
        filmoviGlumca = findViewById( R.id.filmovi_glumca );

        btn_see_all = findViewById( R.id.tv_see_all_filmove_glumca );

    }

    private void getAllActorsData(int id) {
        MyService1.apiInterface().getAllActorsData( id, APIKEY1, "movie_credits" )
                .enqueue( new Callback<ActorData>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onResponse(Call<ActorData> call, Response<ActorData> response) {

                        actorDetails = response.body();

                        imeGlumca.setText( actorDetails.getName() );

                        if (actorDetails.getBiography().isEmpty()) {
                            biografijaGlumac.setVisibility( View.GONE );

                        } else {
                            biografijaGlumac.setVisibility( View.VISIBLE );
                            biografijaGlumac.setText( actorDetails.getBiography() );
                        }

                        if (actorDetails.getPlaceOfBirth() != null) {
                            birthplaceGLumac.setVisibility( View.VISIBLE );
                            birthplaceGLumac.setText( "in " + actorDetails.getPlaceOfBirth() );
                        }
                        if (actorDetails.getBirthday() != null) {
                            bornGlumac.setVisibility( View.VISIBLE );
                            bornGlumac.setText( "Born: " + actorDetails.getBirthday() );
                        }
                        if (actorDetails.getDeathday() != null) {
                            diedGlumac.setVisibility( View.VISIBLE );
                            diedGlumac.setText( "Died: " + actorDetails.getDeathday() );
                        }

//                        Picasso.with(DetailsActor.this).load(IMAGEBASEURL + actorDetailsResponse.getProfilePath()).into(slikaGlumca);

                        if (actorDetails.getProfilePath() == null) {
                            slikaGlumca.setImageDrawable( getDrawable( R.drawable.men ) );
                        } else {
                            Glide.with( DetailsActor.this )
                                    .load( IMAGEBASEURL + actorDetails.getProfilePath() )
                                    .into( slikaGlumca );
                        }

                        actorCast = actorDetails.getMovieCredits().getCast();

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( DetailsActor.this, LinearLayoutManager.HORIZONTAL, false );
                        filmoviGlumca.setLayoutManager( linearLayoutManager );

                        for (int j = 0; j < actorCast.size() - 1; j++) {
                            for (int k = j + 1; k < actorCast.size(); k++) {
                                if (actorCast.get( j ).getPopularity() < actorCast.get( k ).getPopularity()) {
                                    pojedinacanActorCast = actorCast.get( j );
                                    actorCast.set( j, actorCast.get( k ) );
                                    actorCast.set( k, pojedinacanActorCast );

                                }
                            }
                        }

                        adapter = new AdapterFilmoviGlumca( DetailsActor.this, actorCast );

                        filmoviGlumca.setAdapter( adapter );

                    }

                    @Override
                    public void onFailure(Call<ActorData> call, Throwable t) {
                        Toast.makeText( DetailsActor.this, "Greska sa podacima", Toast.LENGTH_SHORT ).show();
                        Toast.makeText( DetailsActor.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );
    }
}
