package com.example.movies_and_actors.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movies_and_actors.R;

public class ListMovies extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list_movies );
    }
}
