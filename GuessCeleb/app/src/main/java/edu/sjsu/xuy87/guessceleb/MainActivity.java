package edu.sjsu.xuy87.guessceleb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    String htmlString = "";
    List<String> imageURLList = new ArrayList<>();
    List<String> celebNameList = new ArrayList<>();
    Random rand = new Random(41);
    private Button butt0;
    private Button butt1;
    private Button butt2;
    private Button butt3;
    private String chosenCeleb = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageV);
        butt0 = findViewById(R.id.button0);
        butt1 = findViewById(R.id.button1);
        butt2 = findViewById(R.id.button2);
        butt3 = findViewById(R.id.button3);


        ImageURLPicker task1 = new ImageURLPicker();
        try {
            htmlString = task1.execute("http://www.posh24.se/kandisar").get();
            String[] splitResult = htmlString.split("<div class=\"listedArticles\">");

            Pattern p = Pattern.compile("img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);
            while(m.find()){
                imageURLList.add(m.group(1));
            }
            p = Pattern.compile("alt=\"(.*?)\"/>");
            m = p.matcher(splitResult[0]);
            while(m.find()){
                celebNameList.add(m.group(1));
            }
            chosenCeleb = imageURLList.get(rand.nextInt(imageURLList.size()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ImagePicker task2 = new ImagePicker();
        try {
            Bitmap temp = task2.execute(chosenCeleb).get();
            for (String str : celebNameList){
                Log.i("Celeb: ", str);
            }
            imageView.setImageBitmap(temp);
            butt0.setText(celebNameList.get(rand.nextInt(celebNameList.size())));
            butt1.setText(celebNameList.get(rand.nextInt(celebNameList.size())));
            butt2.setText(celebNameList.get(rand.nextInt(celebNameList.size())));
            butt3.setText(celebNameList.get(rand.nextInt(celebNameList.size())));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void selected(View view) {
    }
}

class ImageURLPicker extends AsyncTask<String, Void, String> {


    private StringBuilder result;

    @Override
    protected String doInBackground(String... strings) {

        try {
            URL url = new URL(strings[0]);
            result = new StringBuilder();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            Log.i("Connection success1: ", connection.getResponseMessage());
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1){
                char curr = (char) data;
                result.append(curr);
                data = reader.read();
            }
            Log.i("Retrieved web results: ", result.toString());
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

class ImagePicker extends AsyncTask<String, Void, Bitmap>{

    @Override
    protected Bitmap doInBackground(String... str) {
        try {
            URL url = new URL(str[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            Log.i("Connection success2: ", connection.getResponseMessage());
            InputStream in = connection.getInputStream();
            Bitmap image = BitmapFactory.decodeStream(in);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
