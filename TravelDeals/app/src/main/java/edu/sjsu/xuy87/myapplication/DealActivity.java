package edu.sjsu.xuy87.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText mTxt_title;
    private EditText mTxt_price;
    private EditText mTxt_desc;
    private TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        mTxt_title = findViewById(R.id.txt_title);
        mTxt_price = findViewById(R.id.txt_price);
        mTxt_desc = findViewById(R.id.txt_desc);
        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(deal == null){
            deal = new TravelDeal();
        }
        this.deal = deal;
        mTxt_title.setText(deal.getTitle());
        mTxt_desc.setText(deal.getDesc());
        mTxt_price.setText(deal.getPrice());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal data saved", Toast.LENGTH_SHORT).show();
                returnToPrevious();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal data deleted", Toast.LENGTH_SHORT).show();
                returnToPrevious();
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

    private void returnToPrevious() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    private void deleteDeal() {
        if(deal.getId()==null){
            Toast.makeText(this, "ERROR: No item to be deleted", Toast.LENGTH_SHORT).show();
        } else{
            mDatabaseReference.child(deal.getId()).removeValue();
        }
    }

    private void saveDeal() {
        String title = mTxt_title.getText().toString();
        String price = mTxt_price.getText().toString();
        String desc = mTxt_desc.getText().toString();
        deal.setTitle(title);
        deal.setPrice(price);
        deal.setDesc(desc);
//        mDatabaseReference.push().setValue(deal);
        if(deal.getId()==null)
            mDatabaseReference.push().setValue(deal);
        else
            mDatabaseReference.child(deal.getId()).setValue(deal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        if(FirebaseUtil.isAdmin==true){
            menu.findItem(R.id.save_menu).setVisible(true);
            menu.findItem(R.id.delete_menu).setVisible(true);
        } else{
            menu.findItem(R.id.save_menu).setVisible(false);
            menu.findItem(R.id.delete_menu).setVisible(false);
        }

        return true;
    }
}
