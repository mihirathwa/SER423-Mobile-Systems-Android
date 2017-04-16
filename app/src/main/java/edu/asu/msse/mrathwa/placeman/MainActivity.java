package edu.asu.msse.mrathwa.placeman;

/*
 * Copyright 2017 Mihir Rathwa,
 *
 * This license provides the instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class lists all places as per category using Expandable ListView
 * for Assignment 5
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    private List<String> categories;
    private Map<String, List<String>> placesofEachCategory;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.w(TAG, "onCreate");
        context = this;
        expandableListView = (ExpandableListView) findViewById(R.id.AM_elvPlaces);

        categories = new ArrayList<String>();
        placesofEachCategory = new HashMap<>();

        //Insert Code to set the Categories List
        makeExpandableListView();
    }

    protected void makeExpandableListView() {
        PlaceDescriptionDB db = new PlaceDescriptionDB(this);
        SQLiteDatabase dbCursor = db.openDB();

        Cursor categoriesCursor = dbCursor.rawQuery("select distinct category from placeDescriptions;", new String[]{});
        categories = new ArrayList<String>();

        while(categoriesCursor.moveToNext()) {
            try {
                categories.add(categoriesCursor.getString(0));
            } catch (Exception e) {
                Log.w(TAG, "Exception while getting categories from database" + e.getMessage());
            }
        }

        for(String s: categories) {
            Cursor placesCursor = dbCursor.rawQuery("select name from placeDescriptions where category like '" + s + "';", new String[]{});
            List<String> placesOfCategory = new ArrayList<String>();

            while(placesCursor.moveToNext()) {
                try {
                    placesOfCategory.add(placesCursor.getString(0));
                } catch (Exception e) {
                    Log.w(TAG, "Exception while getting places for a category from database" + e.getMessage());
                }
            }

            placesofEachCategory.put(s, placesOfCategory);
        }

        expandableListAdapter = new MyExpandableListAdapter(this, categories, placesofEachCategory);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selectedPlace = placesofEachCategory.get(categories.get(i)).get(i1);

                Intent intent = new Intent(context, EditPlaceActivity.class);
                intent.putExtra("placeName", selectedPlace);
                intent.putExtra("callingActivity", "MainActivity");
                startActivityForResult(intent, 1);
                return false;
            }
        });
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

                //Insert Code to updating the category list
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