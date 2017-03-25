package edu.asu.msse.mrathwa.placeman;

/*
 * Copyright 2017 Mihir Rathwa,
 *
 * This license provides the instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class contains the description for each Place with
 * ability to Add a place, edit it or remove it
 * for Assignment 5
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Mihir Rathwa Mihir.Rathwa@asu.edu
 *         Software Engineering, CIDSE, ASU Poly
 * @version February 08, 2017
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditPlaceActivity extends AppCompatActivity {
    private Context context;

    private String placeName;
    private TextView distance;
    private Button btnRemove;
    private String callingActivity;

    private Spinner spinPlaces;

    private EditText etName;
    private EditText etDescription;
    private EditText etCategory;
    private EditText etAddressTitle;
    private EditText etAddressStreet;
    private EditText etElevation;
    private EditText etLatitude;
    private EditText etLongitude;
    private EditText etImage;

    private PlacesHandler placesHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        getUXItemIds();

        Intent intent = getIntent();
        callingActivity = intent.getStringExtra("callingActivity");

        if (callingActivity.equals("MainActivity")) {

            placeName = intent.getStringExtra("placeName");
            placesHandler = new PlacesHandler(this);
            placesHandler.getPlaceDescription(placeName);
            placesHandler.getAllPlaces();

            placesHandler.setEditPlaceUITags(etName,
                    etDescription,
                    etCategory,
                    etAddressTitle,
                    etAddressStreet,
                    etElevation,
                    etLatitude,
                    etLongitude,
                    etImage,
                    spinPlaces,
                    distance,
                    btnRemove);
        }
        else {

            btnRemove.setVisibility(View.GONE);
            spinPlaces.setVisibility(View.GONE);
            distance.setVisibility(View.GONE);
        }
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
            Intent intent = new Intent(this, EditPlaceActivity.class);
            intent.putExtra("callingActivity", "AddActivity");
            startActivityForResult(intent, 2);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void getUXItemIds() {
        spinPlaces = (Spinner) findViewById(R.id.AEP_spinPlaces);
        distance = (TextView) findViewById(R.id.AEP_tvDistance);
        btnRemove = (Button) findViewById(R.id.AEP_btnRemove);

        etName = (EditText) findViewById(R.id.AEP_etName);
        etDescription = (EditText) findViewById(R.id.AEP_etDescription);
        etCategory = (EditText) findViewById(R.id.AEP_etCategory);
        etAddressTitle = (EditText) findViewById(R.id.AEP_etAddressTitle);
        etAddressStreet = (EditText) findViewById(R.id.AEP_etAddressStreet);
        etElevation = (EditText) findViewById(R.id.AEP_etElevation);
        etLatitude = (EditText) findViewById(R.id.AEP_etLatitude);
        etLongitude = (EditText) findViewById(R.id.AEP_etLongitude);
        etImage = (EditText) findViewById(R.id.AEP_etImage);
    }

    public void AEP_SaveOnClick(View view) {
        boolean validated = true;

        if (etName.getText().toString().trim().equals("")){
            etName.setError("Name required");
            validated = false;
        }

        if (etDescription.getText().toString().trim().equals("")){
            etDescription.setError("Description required");
            validated = false;
        }

        if (etCategory.getText().toString().trim().equals("")){
            etCategory.setError("Category required");
            validated = false;
        }

        if (etAddressTitle.getText().toString().trim().equals("")){
            etAddressTitle.setError("Address Title required");
            validated = false;
        }

        if (etAddressStreet.getText().toString().trim().equals("")){
            etAddressStreet.setError("Address Street required");
            validated = false;
        }

        if (etElevation.getText().toString().trim().equals("")){
            etElevation.setError("Elevation required");
            validated = false;
        }

        if (etLatitude.getText().toString().trim().equals("")){
            etLatitude.setError("Latitude required");
            validated = false;
        }

        if (etLongitude.getText().toString().trim().equals("")){
            etLongitude.setError("Longitude required");
            validated = false;
        }

        if (etImage.getText().toString().trim().equals("")){
            etImage.setError("Image required");
            validated = false;
        }

        if (validated) {
            String placeName = etName.getText().toString();
            String placeDesc =  etDescription.getText().toString();
            String placeCategory = etCategory.getText().toString();
            String placeAddressTitle = etAddressTitle.getText().toString();
            String placeAddressStreet = etAddressStreet.getText().toString();
            String placeElevation = etElevation.getText().toString();
            String placeLatitude = etLatitude.getText().toString();
            String placeLongitude = etLongitude.getText().toString();
            String placeImage = etImage.getText().toString();

            PlaceDescription newPlace = new PlaceDescription(placeName,
                    placeDesc,
                    placeCategory,
                    placeAddressTitle,
                    placeAddressStreet,
                    Double.parseDouble(placeElevation),
                    Double.parseDouble(placeLatitude),
                    Double.parseDouble(placeLongitude),
                    placeImage);

            placesHandler = new PlacesHandler(this);
            placesHandler.addPlace(newPlace);
        }
        else {
            Toast.makeText(this, "Enter all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AEP_RemoveOnClick(View view) {
        placesHandler = new PlacesHandler(this);
        placesHandler.removePlace(placeName);
    }
}
