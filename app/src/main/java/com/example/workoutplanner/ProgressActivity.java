package com.example.workoutplanner;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ProgressActivity extends AppCompatActivity {

    public static final String TAG_LIST = "first";
    public static final String TAG_NEW_PROGRESS = "second";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fr = fragmentManager.findFragmentById(R.id.fragment_container);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new ProgressListFragment(), TAG_LIST);
//        ft.add(R.id.fragment_container, new AddProgressFragment(), TAG_NEW_PROGRESS);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

}