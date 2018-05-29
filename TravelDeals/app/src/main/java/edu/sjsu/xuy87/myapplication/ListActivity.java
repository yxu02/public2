package edu.sjsu.xuy87.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
//    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference mDatabaseReference;
//    private ChildEventListener mChildEventListener;
//    private TextView mTv_title;
//    private TextView mTv_price;
//    private TextView mTv_desc;
//    private ArrayList<TravelDeal> mDeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_deal_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.new_deal);
        if(FirebaseUtil.isAdmin==true)
            menuItem.setVisible(true);
        else menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_deal:
                Intent intent = new Intent(this, DealActivity.class);
                startActivity(intent);
                return true;
            case R.id.log_out:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ListActivity.this, "Logged out...", Toast.LENGTH_SHORT).show();
                                FirebaseUtil.attachFirebaseAuthListencer();
                            }
                        });
                FirebaseUtil.detachFirebaseAuthListener();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseUtil.detachFirebaseAuthListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUtil.openFirebaseUtil("travedeals", this);

        RecyclerView rvDeals = findViewById(R.id.recyclerview);
        final DealAdapter adapter = new DealAdapter();
        rvDeals.setAdapter(adapter);
        LinearLayoutManager dealLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rvDeals.setLayoutManager(dealLayoutManager);

        FirebaseUtil.attachFirebaseAuthListencer();
    }

    public void showMenu(){
        invalidateOptionsMenu();
    }
}
