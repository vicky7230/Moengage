package com.vicky7230.moengage.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.vicky7230.moengage.R;
import com.vicky7230.moengage.network.News;
import com.vicky7230.moengage.network.NewsLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends BaseActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<News>>,
        NewsAdapter.Callback {

    private static final int NEWS_LOADER_ID = 1234;
    private static final int SORT_NEWEST_FIRST = 1;
    private static final int SORT_OLDEST_FIRST = 2;
    private static final String TAG = "NewsScreen";

    private ArrayList<News> newsList = new ArrayList<>();
    private NewsAdapter newsAdapter = new NewsAdapter(new ArrayList<>());
    private SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

    private Toolbar toolbar;
    private RecyclerView newsListView;
    private LinearLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsAdapter.setCallback(this);

        logFirebaseToken();

        initUI();
    }

    private void logFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed : logFirebaseToken", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    if (task.getResult() != null) {
                        String token = task.getResult().getToken();
                        Log.d(TAG, "Firebase token: logFirebaseToken : " + token);
                    }
                });
    }

    private void initUI() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("News Feed");
        }

        newsListView = findViewById(R.id.news_list);
        newsListView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        newsListView.setLayoutManager(new LinearLayoutManager(this));
        newsListView.setAdapter(newsAdapter);

        errorLayout = findViewById(R.id.error_layout);

        LoaderManager.getInstance(this).initLoader(NEWS_LOADER_ID, null, this);
        showLoading();
    }

    @NonNull
    @Override
    public Loader<ArrayList<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<News>> loader, ArrayList<News> data) {
        hideLoading();
        if (data != null && !data.isEmpty()) {
            newsListView.setVisibility(View.VISIBLE);
            errorLayout.setVisibility(View.GONE);
            if (newsList != data) {
                newsList = data;
                showNews(data);
            }
            Log.d(TAG, "Loader finished with a success response");
        } else {
            newsListView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            showError(getResources().getString(R.string.text_api_failed));
            Log.d(TAG, "Loader finished with a failure response");
        }
    }

    private void showNews(ArrayList<News> newsArrayList) {
        newsAdapter.updateItems(newsArrayList);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<News>> loader) {
        //Do nothing
    }

    @Override
    public void onItemClick(News news) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(news.getUrl()));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newest_first:
                if (newsList.size() > 0) {
                    sortNewsList(SORT_NEWEST_FIRST);
                    newsAdapter.notifyDataSetChanged();
                }
                return true;
            case R.id.oldest_first:
                if (newsList.size() > 0) {
                    sortNewsList(SORT_OLDEST_FIRST);
                    newsAdapter.notifyDataSetChanged();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void sortNewsList(int sortOrder) {
        Collections.sort(newsList, (o1, o2) -> {
            long t1;
            long t2;
            try {
                t1 = inputDateFormat.parse(o1.getPublishedAt()).getTime();
                t2 = inputDateFormat.parse(o2.getPublishedAt()).getTime();
            } catch (Exception e) {
                Log.e(TAG, " Date comparison : sortNewsList" + e.getLocalizedMessage());
                t1 = 0;
                t2 = 0;
            }
            if (sortOrder == SORT_NEWEST_FIRST)
                return Long.compare(t2, t1);
            else
                return Long.compare(t1, t2);
        });
    }


    public void onRetryClick(View view) {
        errorLayout.setVisibility(View.GONE);
        showLoading();
        LoaderManager.getInstance(this).restartLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}
