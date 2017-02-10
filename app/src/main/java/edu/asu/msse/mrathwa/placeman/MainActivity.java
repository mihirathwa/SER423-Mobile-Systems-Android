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
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private PlaceLibrary placeLibrary;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter placeListAdapter;

    private HashMap<String, PlaceDescription> placesCatalogue;
    private List<String> categories;
    private Map<String, List<String>> placesofEachCategory;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        expandableListView = (ExpandableListView) findViewById(R.id.AM_elvPlaces);

        placeLibrary = new PlaceLibrary(this);
        parseLibraryData(placeLibrary);

        makeExapandableListView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                placeLibrary = (PlaceLibrary) data.getSerializableExtra("placeLibrary");

                Log.w("MA", "onActivityResult");

                parseLibraryData(placeLibrary);
                makeExapandableListView();
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
            intent.putExtra("placeLibrary", placeLibrary);
            intent.putExtra("callingActivity", "AddActivity");
            startActivityForResult(intent, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void makeExapandableListView () {
        placeListAdapter = new MyExpandableListAdapter(this, categories, placesofEachCategory);
        expandableListView.setAdapter(placeListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String placeName = placesofEachCategory.get(categories.get(i)).get(i1);

                Intent intent = new Intent(context, EditPlaceActivity.class);
                intent.putExtra("placeLibrary", placeLibrary);
                intent.putExtra("placeName", placeName);
                intent.putExtra("callingActivity", "MainActivity");
                startActivityForResult(intent, 1);
                return false;
            }
        });
    }

    public void parseLibraryData (PlaceLibrary placeLibrary) {
        placesCatalogue = placeLibrary.getPlacesCatalogue();
        PlaceDescription placeObject;
        HashSet<String> compileCategories = new HashSet<>();

        placesofEachCategory = new HashMap<>();

        HashMap<String, String> categoryWithPlaces = new HashMap<>();

        Set<String> keySet = placesCatalogue.keySet();
        Iterator<String> catalogueIterator = keySet.iterator();

        while (catalogueIterator.hasNext()){
            String key = catalogueIterator.next();
            placeObject = placesCatalogue.get(key);
            compileCategories.add(placeObject.getCategory());
        }

        categories = new ArrayList<>(compileCategories);

        Iterator<String> categoriesIterator = compileCategories.iterator();

        while (categoriesIterator.hasNext()) {
            String category = categoriesIterator.next();
            ArrayList<String> places = new ArrayList<>();

            Iterator<String> catalogIterator = keySet.iterator();
            while (catalogIterator.hasNext()) {
                String key = catalogIterator.next();
                placeObject = placesCatalogue.get(key);
                if (placeObject.getCategory().equals(category)) {
                    places.add(placeObject.getName());
                }
            }

            placesofEachCategory.put(category, places);
        }
    }
}