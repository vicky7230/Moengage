package com.vicky7230.moengage.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public final class Parser {

    private static final String TAG = "Parser";
    private static final String ATTR_NEWS_LIST = "articles";
    private static final String ATTR_NEWS_AUTHOR = "author";
    private static final String ATTR_NEWS_TITLE = "title";
    private static final String ATTR_NEWS_DESCRIPTION = "description";
    private static final String ATTR_NEWS_URL = "url";
    private static final String ATTR_NEWS_URL_TO_IMAGE = "urlToImage";
    private static final String ATTR_NEWS_PUBLISHED_AT = "publishedAt";
    private static final String ATTR_NEWS_CONTENT = "content";

    /**
     *Parse Json Response of the news API
     *
     * @param response String representation of API response
     * @return ArrayList of News
     */
    public static ArrayList<News> parseNews(String response) {
        try {
            JSONObject responseObject = new JSONObject(response);
            if (responseObject.has(ATTR_NEWS_LIST)) {
                ArrayList<News> newsList = new ArrayList<>();
                JSONArray newsInfoList = responseObject.getJSONArray(ATTR_NEWS_LIST);
                int infoLength = newsInfoList.length();
                for (int i = 0; i < infoLength; i++) {
                    News news = new News();
                    JSONObject newsAttrs = newsInfoList.getJSONObject(i);
                    if (newsAttrs.has(ATTR_NEWS_AUTHOR)) {
                        news.setAuthor(newsAttrs.getString(ATTR_NEWS_AUTHOR));
                    }
                    if (newsAttrs.has(ATTR_NEWS_TITLE)) {
                        news.setTitle(newsAttrs.getString(ATTR_NEWS_TITLE));
                    }
                    if (newsAttrs.has(ATTR_NEWS_DESCRIPTION)) {
                        news.setDescription(newsAttrs.getString(ATTR_NEWS_DESCRIPTION));
                    }
                    if (newsAttrs.has(ATTR_NEWS_URL)) {
                        news.setUrl(newsAttrs.getString(ATTR_NEWS_URL));
                    }
                    if (newsAttrs.has(ATTR_NEWS_URL_TO_IMAGE)) {
                        news.setUrlToImage(newsAttrs.getString(ATTR_NEWS_URL_TO_IMAGE));
                    }
                    if (newsAttrs.has(ATTR_NEWS_PUBLISHED_AT)) {
                        news.setPublishedAt(newsAttrs.getString(ATTR_NEWS_PUBLISHED_AT));
                    }
                    if (newsAttrs.has(ATTR_NEWS_CONTENT)) {
                        news.setContent(newsAttrs.getString(ATTR_NEWS_CONTENT));
                    }
                    newsList.add(news);
                }
                return newsList;
            }
        } catch (Exception e) {
            Log.e(TAG, "PARSER: parseNews", e);
        }
        return null;
    }
}
