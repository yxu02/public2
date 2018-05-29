package edu.sjsu.xuy87.cmpe277_assgn5_yx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void insertNewItem(View view) {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    public void searchItem(View view) {
        Intent intent = new Intent(this, SearchItemActivity.class);
        startActivity(intent);
    }
}
