package com.example.acer.movieandroidkejar.main;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.acer.movieandroidkejar.R;
import com.example.acer.movieandroidkejar.dao.MovieResponseDao;
import com.example.acer.movieandroidkejar.data.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MainAdapter adapter;
    private List<MainDao> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new MainAdapter(mData);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        ApiClient.service().getMovieList("68e8409e6a0fe95d06fdf148b193ab13")
                .enqueue(new Callback<MovieResponseDao>() {
                    @Override
                    public void onResponse(Call<MovieResponseDao> call, Response<MovieResponseDao> response) {
                        if (response.isSuccessful()) {
                            for (MovieResponseDao.MovieData movieData : response.body().getResults()) {
                                mData.add(new MainDao(movieData.getTitle(), "https://image.tmdb.org/t/p/w185/"+movieData.getPoster_path()));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponseDao> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mData.add(new MainDao("Omaewaa", "https://i.ytimg.com/vi/8HQjKZUbT4U/hqdefault.jpg"));
//                mData.add(new MainDao("Moishin", "https://i.ytimg.com/vi/8HQjKZUbT4U/hqdefault.jpg"));
//                mData.add(new MainDao("Deruu", "https://i.ytimg.com/vi/8HQjKZUbT4U/hqdefault.jpg"));
//
//                adapter.notifyDataSetChanged();
//            }
//        }, 5000);

        Toast.makeText(this, "Loading data 5 detik", Toast.LENGTH_SHORT).show();

    }
}
