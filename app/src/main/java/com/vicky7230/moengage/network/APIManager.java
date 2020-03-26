package com.vicky7230.moengage.network;

import android.util.Log;

import com.vicky7230.moengage.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public final class APIManager {
    private static final String TAG = "APIManager";

    private static final String NEWS_API = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json";

    /**
     * Get ArrayList of News from Network
     *
     * @return ArrayList of News
     */
    public static ArrayList<News> getNews() {
        String response = executeGetRequest(NEWS_API);
        return Parser.parseNews(response);
    }

    /**
     * Executes a GET Request
     *
     * @param URI The API endpoint to hit
     * @return String representation of the API call response
     */
    public static String executeGetRequest(final String URI) {
        try {

            Log.d(TAG, "APIManager:executeGetRequest Hitting API: " + URI);

            URL url = new URL(URI);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

            if (200 != urlConnection.getResponseCode()) {
                Log.d(TAG, "Response: API Failed: " + URI);
                return null;
            }
            InputStream in = urlConnection.getInputStream();
            String resp = convertStreamToString(in);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Response: " + resp);
            }
            return resp;
        } catch (IOException e) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "error while fetching " + URI, e);
        }
        return null;
    }

    /**
     * Convert input stream to String
     *
     * @param inputStream The input stream from the API response entity
     * @return String representation of the API response
     */
    private static String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "APIManager:executeRequest: IOException", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                Log.e(TAG, "APIManager:executeRequest: IOException", e);
            }
        }
        return sb.toString();
    }

}


