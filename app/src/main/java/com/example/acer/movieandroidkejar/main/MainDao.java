package com.example.acer.movieandroidkejar.main;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ACER on 01/12/2017.
 */

public class MainDao implements Parcelable{

    private String title;
    private String description;
    private String imageBackground;
    private String releaseDate;
    private String ImageUrl;

    public MainDao(String title, String description, String imageBackground, String releaseDate, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageBackground = imageBackground;
        this.releaseDate = releaseDate;
        ImageUrl = imageUrl;
    }

    protected MainDao(Parcel in) {
        title = in.readString();
        description = in.readString();
        imageBackground = in.readString();
        releaseDate = in.readString();
        ImageUrl = in.readString();
    }

    public static final Creator<MainDao> CREATOR = new Creator<MainDao>() {
        @Override
        public MainDao createFromParcel(Parcel in) {
            return new MainDao(in);
        }

        @Override
        public MainDao[] newArray(int size) {
            return new MainDao[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageBackground() {
        return imageBackground;
    }

    public void setImageBackground(String imageBackground) {
        this.imageBackground = imageBackground;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(imageBackground);
        parcel.writeString(releaseDate);
        parcel.writeString(ImageUrl);
    }
}
