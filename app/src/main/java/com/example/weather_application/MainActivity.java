package com.example.weather_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    ListView citiesList;
    ArrayList<String> cities = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateListView();
    }

    public void updateListView(){
        Cities cityObj = new Cities();
        try {
            String response = cityObj.execute("https://api.openaq.org/v1/cities?country=IN").get();
            Log.i("Content DATA :", response);
            JSONObject jsonObject = new JSONObject(response);
            String results = jsonObject.getString("results");
            Log.i("\nData", results);
            JSONArray array = new JSONArray(results);
            String city= "";
            for(int i = 0; i<array.length();i++){
                JSONObject part = array.getJSONObject(i);
                city = part.getString("city");
                cities.add(city);
            }
            Log.i("cities Array: ", String.valueOf(cities));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        citiesList = (ListView)findViewById(R.id.cityListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, cities);
        citiesList.setAdapter(arrayAdapter);
        citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent intent = new Intent(MainActivity.this, WeatherInformation.class);
               intent.putExtra("cityname", cities.get(position));
               startActivity(intent);
            }
        });
//        RecycleView recycleView = new RecycleView();
//        recycleView.setCitiesList();
    }

//    public class RecycleView extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
//        public void setCitiesList(){
//
//        }
//
//        @NonNull
//        @Override
//        public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, int position) {
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//    }
    public class Cities extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... address) {
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                int data = isr.read();
                String content = "";
                char ch;
                while(data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data  = isr.read();
                }
                return content;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}