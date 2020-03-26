package com.vicky7230.moengage.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.vicky7230.moengage.network.APIManager;
import com.vicky7230.moengage.network.News;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    private ArrayList<News> newsList;

    public NewsLoader(@NonNull Context appContext) {
        super(appContext);
    }

    @Override
    public ArrayList<News> loadInBackground() {
        return APIManager.getNews();
    }

    @Override
    protected void onStartLoading() {
        if (newsList != null) {
            // Use cached data
            deliverResult(newsList);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public void deliverResult(@Nullable ArrayList<News> data) {
        // We’ll save the data for later retrieval
        newsList = data;
        // We can do any pre-processing we want here
        super.deliverResult(data);
    }
}
