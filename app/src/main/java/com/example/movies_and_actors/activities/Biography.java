package com.example.movies_and_actors.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movies_and_actors.R;
import com.example.movies_and_actors.models.ActorDetailsResponse;
import com.example.movies_and_actors.net.MyService1;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movies_and_actors.net.MyServiceContract.APIKEY1;

public class Biography extends AppCompatActivity {

    private int id;
    private TextView biografija;
    private ActorDetailsResponse actorDetailsResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_biography );

        biografija = findViewById( R.id.cela_biografija );

        id = getIntent().getIntExtra( "id", 0 );

        getGlumacData( id );
    }

    private void getGlumacData(int id) {
        MyService1.apiInterface().getActorData( id, APIKEY1 )
                .enqueue( new Callback<ActorDetailsResponse>() {
                    @Override
                    public void onResponse(Call<ActorDetailsResponse> call, Response<ActorDetailsResponse> response) {

                        actorDetailsResponse = response.body();


                        biografija.setText( actorDetailsResponse.getBiography() );

                    }

                    @Override
                    public void onFailure(Call<ActorDetailsResponse> call, Throwable t) {
                        Toast.makeText( Biography.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT ).show();

                    }
                } );
    }
}
