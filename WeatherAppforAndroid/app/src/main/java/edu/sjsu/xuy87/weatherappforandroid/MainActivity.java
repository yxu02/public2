package edu.sjsu.xuy87.weatherappforandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public static final String APIKEY = "&APPID=xxxxxxxxxxxxxxxxxxxxxxxxxx";
    public static final String APIURL = "http://api.openweathermap.org/data/2.5/weather?";
    private EditText city;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.et_city);
        resultText = findViewById(R.id.tv_data);
    }

    public void getWeatherData(View view) {
        String input = null;
        try {
            //encode " " to %20 instead of "+"
            input = URLEncoder.encode(city.getText().toString(),"UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //auto hide soft keyboard once input is done
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken(),0);
        Log.i("Query input: ", input);
        if (input!=null){


            String query = APIURL + "q=" + input +APIKEY;
            Log.i("Query is: ", query);
            WeatherAPI weatherAPI = new WeatherAPI();

            weatherAPI.execute(query);
        }

    }

    class WeatherAPI extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char curr = (char) data;
                    stringBuilder.append(curr);
                    data = reader.read();
                }
                Log.i("Response: ", stringBuilder.toString());
                return stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String results = jsonObject.getString("weather");
                    Log.i("API Results: ", results);
                    JSONArray weatherArr = new JSONArray(results);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < weatherArr.length(); i++) {
                        JSONObject jsonArrItem = new JSONObject(weatherArr.getString(i));
                        sb.append("Forecast: " + jsonArrItem.getString("main") + "\n");
                        sb.append("Description: " + jsonArrItem.getString("description") + "\n");
                    }
                    resultText.setText(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: cannot find the city", Toast.LENGTH_SHORT).show();
                }
            }else{
                resultText.setText("");
                Toast.makeText(getApplicationContext(), "Error: cannot find the city", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

