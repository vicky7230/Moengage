package com.vicky7230.moengage.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.vicky7230.moengage.R;
import com.vicky7230.moengage.network.News;
import com.vicky7230.moengage.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public static final String TAG = "RecyclerView";

    interface Callback {
        void onItemClick(News news);
    }

    private ArrayList<News> newsArrayList;
    private Callback callback;
    private SimpleDateFormat inputDateFormat;
    private SimpleDateFormat outputDateFormat;

    public NewsAdapter(ArrayList<News> newsArrayList) {
        this.newsArrayList = newsArrayList;
        inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        outputDateFormat = new SimpleDateFormat("MMM dd yyyy '  •  ' hh:mm a", Locale.ENGLISH);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void updateItems(ArrayList<News> newsArrayList) {
        this.newsArrayList.clear();
        this.newsArrayList = newsArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsViewHolder newsViewHolder =
                new NewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false));
        newsViewHolder.itemView.setOnClickListener(v -> {
            int position = newsViewHolder.getAdapterPosition();
            if (callback != null) {
                callback.onItemClick(newsArrayList.get(position));
            }
        });
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.onBind(newsArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView image;
        public AppCompatTextView title;
        public AppCompatTextView time;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            time = itemView.findViewById(R.id.date_time);
        }

        void onBind(News news) {
            if (CommonUtils.isValidUrl(news.getUrlToImage())) {
                Glide.with(itemView.getContext())
                        .load(news.getUrlToImage())
                        .error(R.drawable.ic_warning)
                        .transform(new CenterCrop(), new RoundedCorners(25))
                        .into(image);
                title.setText(news.getTitle());
            } else {
                image.setImageResource(R.drawable.ic_warning);
            }

            try {
                Date date = inputDateFormat.parse(news.getPublishedAt());
                time.setText(outputDateFormat.format(date));
            } catch (Exception e) {
                Log.e(TAG, "Date Parsing : onBind : " + e.getLocalizedMessage());
            }
        }
    }
}
