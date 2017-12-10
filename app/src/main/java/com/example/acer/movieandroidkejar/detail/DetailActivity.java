package com.example.acer.movieandroidkejar.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.acer.movieandroidkejar.R;
import com.example.acer.movieandroidkejar.main.MainDao;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private MainDao mData;
    private ImageView imageToolbar;
    private TextView txtTitle;
    private ImageView imagePoster;
    private TextView txtReleaseDate;
    private TextView txtOverview;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imagePoster = (ImageView) findViewById(R.id.imagePoster);
        imageToolbar = (ImageView) findViewById(R.id.imageToolbar);
        txtReleaseDate = (TextView) findViewById(R.id.txtReleaseDate);
        txtOverview = (TextView) findViewById(R.id.txtOverview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mData = getIntent().getParcelableExtra("dataMovie");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mData.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        txtTitle.setText(mData.getTitle());
        txtReleaseDate.setText(mData.getReleaseDate());
        txtOverview.setText(mData.getDescription());

        Picasso.with(this)
                .load(mData.getImageBackground())
                .into(imageToolbar);

        Picasso.with(this)
                .load(mData.getImageUrl())
                .into(imagePoster);
    }
}
