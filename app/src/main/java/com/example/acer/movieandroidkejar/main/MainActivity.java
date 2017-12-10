package com.example.acer.movieandroidkejar.main;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.acer.movieandroidkejar.R;
import com.example.acer.movieandroidkejar.data.dao.MovieResponseDao;
import com.example.acer.movieandroidkejar.data.ApiClient;
import com.example.acer.movieandroidkejar.data.offline.MovieContract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    private MainAdapter adapter;
    private List<MainDao> mData = new ArrayList<>();
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(0, null, this);

        adapter = new MainAdapter(mData);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Popular Movie");

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeMain);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        getDataMovie();

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
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this){
        Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try{
                    return this.getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry._ID);
                } catch (Exception e){
                    Log.e(TAG, "Failed to asynchronously load data. \n"+e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("onLoadFinished: ", String.valueOf(data.getCount()));
        mData.clear();

        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);

            mData.add(new MainDao(
                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                    "https://image.tmdb.org/t/p/w185/"+data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)),
                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)),
                    "https://image.tmdb.org/t/p/w185/"+data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH))
                    ));
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void getDataMovie(){
        mRefreshLayout.setRefreshing(true);
        ApiClient.service().getMovieList("68e8409e6a0fe95d06fdf148b193ab13")
                .enqueue(new Callback<MovieResponseDao>() {
                    @Override
                    public void onResponse(Call<MovieResponseDao> call, Response<MovieResponseDao> response) {
                        if (response.isSuccessful()) {
                            mRefreshLayout.setRefreshing(false);

                            Uri deleteUri = MovieContract.MovieEntry.CONTENT_URI;
                            getContentResolver().delete(deleteUri, null, null);

                            for (MovieResponseDao.MovieData movieData : response.body().getResults()) {
//                                mData.add(new MainDao(movieData.getTitle(), "https://image.tmdb.org/t/p/w185/"+movieData.getPoster_path()));
                                ContentValues contentValues = new ContentValues();

                                contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE_IDS, movieData.getId());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieData.getTitle());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_ORI_TITLE, movieData.getOriginal_title());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movieData.getVote_count());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, movieData.isVideo() ? 1 : 0);
                                contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, movieData.getVote_average());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movieData.getPopularity());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movieData.getPoster_path());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG, movieData.getOriginal_language());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_GENRE, "");
                                contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movieData.getBackdrop_path());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT, movieData.isAdult() ? 1 : 0);
                                contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieData.getOverview());
                                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieData.getRelease_date());

                                Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

                                if(uri != null){
                                    Log.d("onResponse", "INSERT DATA SUCCESS");
                                }

                                getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            mRefreshLayout.setRefreshing(false);
                            Toast.makeText(MainActivity.this, "internal server error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponseDao> call, Throwable t) {
                        mRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRefresh() {
        getDataMovie();
    }
}
