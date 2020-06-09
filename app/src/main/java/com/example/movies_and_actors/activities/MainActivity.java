package com.example.movies_and_actors.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies_and_actors.DividerKlasa;
import com.example.movies_and_actors.R;
import com.example.movies_and_actors.adapters.AdapterFilmovi;
import com.example.movies_and_actors.adapters.AdapterSearch;
import com.example.movies_and_actors.models.MoviesResponse;
import com.example.movies_and_actors.models.ResultsMovieItem;
import com.example.movies_and_actors.net.MyService1;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movies_and_actors.net.MyServiceContract.APIKEY1;

public class MainActivity extends AppCompatActivity implements AdapterSearch.OnItemClickListener, AdapterFilmovi.OnItemClickListener{

    private AdapterSearch adapterSearch;
    private AdapterFilmovi adapterFilmovi;

    private List<ResultsMovieItem> searchedMovieList;
    private RecyclerView popularRecycler, topRatedRecycler, upcomingRecycler, searchRecycler;
    private ScrollView glavni_layout;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        initViews();

        getPopularMovies();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search);

        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Enter movie name");


        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                glavni_layout.setVisibility( View.GONE);
                searchRecycler.setVisibility(View.VISIBLE);
                searchMovieByName(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                glavni_layout.setVisibility(View.GONE);
                searchRecycler.setVisibility(View.VISIBLE);
                searchMovieByName(newText);

                return false;
            }
        });

        searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchRecycler.setVisibility(View.GONE);

                glavni_layout.setVisibility(View.VISIBLE);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initViews() {

        glavni_layout = findViewById( R.id.glavni_layout );
        popularRecycler = findViewById( R.id.recyclerViewPopularMovies );
        topRatedRecycler = findViewById( R.id.recyclerViewTopRatedMovies );
        upcomingRecycler = findViewById( R.id.recyclerViewUpcomingMovies );
        searchRecycler = findViewById( R.id.search_movies );

        searchRecycler.addItemDecoration( new DividerKlasa( popularRecycler.getContext(), DividerKlasa.VERTICAL ) );
        searchRecycler.setHasFixedSize( true );
        searchRecycler.setItemViewCacheSize( 20 );

        popularRecycler.setHasFixedSize( true );
        popularRecycler.setItemViewCacheSize( 20 );

        topRatedRecycler.setHasFixedSize( true );
        topRatedRecycler.setItemViewCacheSize( 20 );

        upcomingRecycler.setHasFixedSize( true );
        upcomingRecycler.setItemViewCacheSize( 20 );
    }

    private void getPopularMovies() {
        MyService1.apiInterface().getPopularMovies(APIKEY1, "GB")
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        if (response.code() == 200) {
                            try {

                                searchedMovieList = response.body().getResults();

                                Log.v("onResponse", searchedMovieList.size() + " Movies");

                                linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                popularRecycler.setLayoutManager(linearLayoutManager);

                                adapterFilmovi = new AdapterFilmovi(MainActivity.this, searchedMovieList, MainActivity.this);

                                popularRecycler.setAdapter(adapterFilmovi);


                            } catch (NullPointerException e) {
//                                Toast.makeText(MainActivity.this, "Ne postoji film/serija sa tim nazivom", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {

//                            Log.v("response.code", " Greska sa serverom");
                            Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
//                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void searchMovieByName(String query) {
        MyService1.apiInterface().searchForMovies(query, APIKEY1)
                .enqueue(new Callback<MoviesResponse>() {
                    @Override
                    public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                        if (response.code() == 200) {
                            try {

                                searchedMovieList = response.body().getResults();

                                Log.v("onResponse", searchedMovieList.size() + " Movies");

                                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                                searchRecycler.setLayoutManager(linearLayoutManager);

                                adapterSearch = new AdapterSearch(MainActivity.this, searchedMovieList, MainActivity.this);

                                searchRecycler.setAdapter(adapterSearch);


                            } catch (NullPointerException e) {
                                Toast.makeText(MainActivity.this, "Ne postoji film/serija sa tim nazivom", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Log.v("response.code", " Greska sa serverom");
                        }
                    }

                    @Override
                    public void onFailure(Call<MoviesResponse> call, Throwable t) {
                        Log.v("onFailure", " Failed to get movies");
                        Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onSearchMovieClick(int position) {

    }

    @Override
    public void onItemClick(int position) {

    }
}
