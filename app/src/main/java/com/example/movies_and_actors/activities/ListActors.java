package com.example.movies_and_actors.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies_and_actors.DividerKlasa;
import com.example.movies_and_actors.R;
import com.example.movies_and_actors.adapters.AdapterSviGlumci;
import com.example.movies_and_actors.models.CastItem;
import com.example.movies_and_actors.models.MovieCrewResponse;
import com.example.movies_and_actors.net.MyService1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movies_and_actors.net.MyServiceContract.APIKEY1;

public class ListActors extends AppCompatActivity {

    private int id;
    private RecyclerView rvListaGlumaca;
    private MovieCrewResponse movieCrewResponse;
    private AdapterSviGlumci adapter;

    List<CastItem> castItemList;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list_actors );

        id = getIntent().getIntExtra("id", 0);
        getMovieCast(id);

        actionBar = getSupportActionBar();
        try {
            actionBar.setTitle("Full Cast");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        rvListaGlumaca = findViewById(R.id.rv_lista_svih_glumaca);
        rvListaGlumaca.addItemDecoration(new DividerKlasa(rvListaGlumaca.getContext(), DividerKlasa.VERTICAL));
        rvListaGlumaca.setHasFixedSize(true);
        rvListaGlumaca.setItemViewCacheSize(20);

    }

    private Integer getMovieCast(int id) {
        MyService1.apiInterface().getMoviesCast(id, APIKEY1)
                .enqueue(new Callback<MovieCrewResponse>() {
                    @Override
                    public void onResponse(Call<MovieCrewResponse> call, Response<MovieCrewResponse> response) {
                        if (response.code() == 200) {
                            try {

                                movieCrewResponse = response.body();

                                castItemList = movieCrewResponse.getCast();

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListActors.this);
                                rvListaGlumaca.setLayoutManager(linearLayoutManager);


                                adapter = new AdapterSviGlumci(ListActors.this, castItemList);

                                rvListaGlumaca.setAdapter(adapter);


                            } catch (NullPointerException e) {
                                Toast.makeText(ListActors.this, "Ne postoji film/serija sa tim nazivom", Toast.LENGTH_SHORT).show();
                                Toast.makeText(ListActors.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Log.v("response.code", " Greska sa serverom");
                            Toast.makeText(ListActors.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<MovieCrewResponse> call, Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(ListActors.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        return id;
    }
}
