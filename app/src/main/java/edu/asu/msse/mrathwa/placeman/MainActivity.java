package edu.asu.msse.mrathwa.placeman;

/*
 * Copyright 2017 Mihir Rathwa,
 *
 * This license provides the instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class contains the connection between Place Description and UI layout
 * in Assignment 1
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Mihir Rathwa Mihir.Rathwa@asu.edu
 *         Software Engineering, CIDSE, ASU Poly
 * @version January 16, 2017
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private PlaceLibrary placeLibrary;

    ExpandableListView expandableListView;
    ExpandableListAdapter placeListAdapter;

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
        parseLibraryData();

        placeListAdapter = new MyExpandableListAdapter(this, categories, placesofEachCategory);
        expandableListView.setAdapter(placeListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(context, EditPlaceActivity.class);
                intent.putExtra("placeName", placesofEachCategory.get(categories.get(i)).get(i1));
                startActivity(intent);
                return false;
            }
        });
    }

    public void parseLibraryData () {
        HashMap<String, PlaceDescription> placesCatalogue = placeLibrary.getPlacesCatalogue();
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