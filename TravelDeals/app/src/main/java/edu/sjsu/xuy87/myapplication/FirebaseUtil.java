package edu.sjsu.xuy87.myapplication;

import android.app.Activity;
import android.app.ListActivity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    private static final int RC_SIGN_IN = 211;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    private static FirebaseUtil mFirebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseAuth.AuthStateListener mAuthStateListener;
    private static edu.sjsu.xuy87.myapplication.ListActivity caller;
    public static ArrayList<TravelDeal> mTravelDeals;
    public static boolean isAdmin;

    private FirebaseUtil() {
    }

    public static void openFirebaseUtil(String ref, final edu.sjsu.xuy87.myapplication.ListActivity callerActivity){
        if(mFirebaseUtil == null){
            mFirebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;
            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser()==null) {
                        FirebaseUtil.signIn();
                    } else{
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    Toast.makeText(caller.getBaseContext(), "Welcome Back!", Toast.LENGTH_SHORT).show();
                }
            };
        }
        mTravelDeals = new ArrayList<>();
        mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    private static void checkAdmin(String userId) {
            FirebaseUtil.isAdmin = false;
            DatabaseReference ref = mFirebaseDatabase.getReference().child("admin").child(userId);
            ChildEventListener listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FirebaseUtil.isAdmin = true;
                    Log.d("Admin", "admin logged in!");
                    caller.showMenu();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            ref.addChildEventListener(listener);
    }

    public static void attachFirebaseAuthListencer(){
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public static void detachFirebaseAuthListener(){
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    public static void signIn(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);
    }
}
