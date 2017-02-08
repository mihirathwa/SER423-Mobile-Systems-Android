package edu.asu.msse.mrathwa.placeman;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class EditPlaceActivity extends AppCompatActivity {
    private PlaceLibrary placeLibrary;
    private HashMap<String, PlaceDescription> placeCatalogue;
    private PlaceDescription placeObject = null;
    String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);

        placeLibrary = new PlaceLibrary(this);
        placeCatalogue = placeLibrary.getPlacesCatalogue();

        Intent intent = getIntent();
        placeName = intent.getStringExtra("placeName");

        ArrayList<String> places = new ArrayList<>();

        Set<String> placeKeys= placeCatalogue.keySet();
        Iterator<String> placeIterator = placeKeys.iterator();

        while (placeIterator.hasNext()) {
            places.add(placeIterator.next());
        }

        Spinner spinPlaces = (Spinner) findViewById(R.id.AEP_spinPlaces);
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, places);
        spinPlaces.setAdapter(spinAdapter);

        setUXwithData();
    }

    protected void setUXwithData() {
        EditText etName = (EditText) findViewById(R.id.AEP_etName);
        EditText etDescription = (EditText) findViewById(R.id.AEP_etDescription);
        EditText etCategory = (EditText) findViewById(R.id.AEP_etCategory);
        EditText etAddressTitle = (EditText) findViewById(R.id.AEP_etAddressTitle);
        EditText etAddressStreet = (EditText) findViewById(R.id.AEP_etAddressStreet);
        EditText etElevation = (EditText) findViewById(R.id.AEP_etElevation);
        EditText etLatitude = (EditText) findViewById(R.id.AEP_etLatitude);
        EditText etLongitude = (EditText) findViewById(R.id.AEP_etLongitude);

        placeObject = placeCatalogue.get(placeName);

        etName.setText(placeObject.getName());
        etDescription.setText(placeObject.getDescription());
        etCategory.setText(placeObject.getCategory());
        etAddressTitle.setText(placeObject.getAddress_title());
        etAddressStreet.setText(placeObject.getAddress_street());
        etElevation.setText(Double.toString(placeObject.getElevation()));
        etLatitude.setText(Double.toString(placeObject.getLatitude()));
        etLongitude.setText(Double.toString(placeObject.getLongitude()));
    }
}
