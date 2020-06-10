package com.example.movies_and_actors.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies_and_actors.DividerKlasa;
import com.example.movies_and_actors.R;
import com.example.movies_and_actors.adapters.AdapterSviFilmovi;
import com.example.movies_and_actors.models.Cast;
import com.example.movies_and_actors.models.CastResult;
import com.example.movies_and_actors.net.MyService1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movies_and_actors.net.MyServiceContract.APIKEY1;

public class ListMovies extends AppCompatActivity {

    private int id;
    private RecyclerView rvListaFilmova;
    private AdapterSviFilmovi adapter;
    private List<com.example.movies_and_actors.models.Cast> cast;
    private Cast pojedinacanCast;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list_movies );

        actionBar = getSupportActionBar();

        try {
            actionBar.setTitle( "" );
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        id = getIntent().getIntExtra( "id", 0 );

        getGlumacFilmovi( id );

        rvListaFilmova = findViewById( R.id.rv_lista_svih_glumaca );
        rvListaFilmova.addItemDecoration( new DividerKlasa( rvListaFilmova.getContext(), DividerKlasa.VERTICAL ) );
        rvListaFilmova.setHasFixedSize( true );
        rvListaFilmova.setItemViewCacheSize( 20 );

    }

    public Integer firstFour(String godina) {
        if (godina == null) {
            return 0;
        }
        if (godina.length() > 4) {

            int noviBroj = Integer.parseInt( godina.substring( 0, 4 ) );
            return noviBroj;
        } else {
            return 0;
        }

    }

    private void getGlumacFilmovi(int id) {
        MyService1.apiInterface().getActorMovies( id, APIKEY1 )
                .enqueue( new Callback<CastResult>() {
                    @Override
                    public void onResponse(Call<CastResult> call, Response<CastResult> response) {
                        cast = response.body().getCast();

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( ListMovies.this );
                        rvListaFilmova.setLayoutManager( linearLayoutManager );

                        try {
                            actionBar.setTitle( "All Movies " + "(" + cast.size() + ")" );
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }


                        for (int j = 0; j < cast.size() - 1; j++) {
                            for (int k = j + 1; k < cast.size(); k++) {
                                if (firstFour( cast.get( j ).getReleaseDate() ) < firstFour( cast.get( k ).getReleaseDate() )) {
                                    pojedinacanCast = cast.get( j );
                                    cast.set( j, cast.get( k ) );
                                    cast.set( k, pojedinacanCast );

                                }
                            }
                        }

                        adapter = new AdapterSviFilmovi( ListMovies.this, cast );

                        rvListaFilmova.setAdapter( adapter );
                    }

                    @Override
                    public void onFailure(Call<CastResult> call, Throwable t) {
                        Toast.makeText( ListMovies.this, "Greska sa listom filmova", Toast.LENGTH_SHORT ).show();
                        Toast.makeText( ListMovies.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT ).show();

                    }
                } );
    }

}
