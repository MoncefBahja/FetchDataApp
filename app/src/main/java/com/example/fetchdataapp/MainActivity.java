package com.example.fetchdataapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    private TextView text;
    private Button btn;

    private static final String API_URL = "https://jsonplaceholder.typicode.com/todos/1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        btn = findViewById(R.id.btn);
    }
    public void fetchData(View view) {
        // Using AsyncTask
      //  new FetchDataTask().execute();
        // Using Thread and Handler
        // new Thread(new FetchDataRunnable()).start();
        // Using ExecutorService
        ExecutorService executor = Executors.newFixedThreadPool(1);
         executor.execute(new FetchDataRunnable());
         executor.shutdown();
    }
   /* private class FetchDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return fetchDataFromApi();
        }

        @Override
        protected void onPostExecute(String result) {
            text.setText(result);
        }
    }*/
    private class FetchDataRunnable implements Runnable {
        private final Handler handler = new Handler(Looper.getMainLooper());


        @Override
        public void run() {
            // Fetch data from API
            final String result = fetchDataFromApi();

            // Update UI using Handler and Runnable
            handler.post(new Runnable() {
                @Override
                public void run() {
                    text.setText(result);
                }
            });
        }
    }
    private String fetchDataFromApi() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            urlConnection.disconnect();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error fetching data";
        }
    }


}