package edu.asu.msse.mrathwa.placeman;

/*
 * Copyright 2017 Mihir Rathwa,
 *
 * This license provides the instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class lists all places as per category using Expandable ListView
 * for Assignment 3
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Mihir Rathwa Mihir.Rathwa@asu.edu
 *         Software Engineering, CIDSE, ASU Poly
 * @version February 08, 2017
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private ExpandableListView expandableListView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w(TAG, "onCreate");
        context = this;
        expandableListView = (ExpandableListView) findViewById(R.id.AM_elvPlaces);

        PlacesHandler placesHandler = new PlacesHandler(this);
        placesHandler.getAllCategories();
        placesHandler.setMainActivityUITags(expandableListView);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.w(TAG, "onRestart");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.w("MA", "onActivityResult");

                PlacesHandler placesHandler = new PlacesHandler(this);
                placesHandler.getAllCategories();
                placesHandler.setMainActivityUITags(expandableListView);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int actionId = item.getItemId();

        if (actionId == R.id.action_add) {
            Intent intent = new Intent(context, EditPlaceActivity.class);
            intent.putExtra("callingActivity", "AddActivity");
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

}