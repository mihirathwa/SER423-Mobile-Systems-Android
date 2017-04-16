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
 * for Assignment 7
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Mihir Rathwa Mihir.Rathwa@asu.edu
 *         Software Engineering, CIDSE, ASU Poly
 * @version April 16, 2017
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;

public class EditPlaceActivity extends AppCompatActivity {
    private Context context;
    private final String TAG = getClass().getSimpleName();

    PlaceDescriptionDB db;
    SQLiteDatabase dbCursor;

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

    private Double firstLatitude;
    private Double firstLongitude;
    private Double secondLatitude;
    private Double secondLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_place);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        context = this;
        getUXItemIds();

        db = new PlaceDescriptionDB(this);
        dbCursor = db.openDB();

        Intent intent = getIntent();
        callingActivity = intent.getStringExtra("callingActivity");

        if (callingActivity.equals("MainActivity")) {

            placeName = intent.getStringExtra("placeName");

            firstLatitude = 0.0;
            firstLongitude = 0.0;
            secondLatitude = 0.0;
            secondLongitude = 0.0;

            setUXWithData(placeName);
            setSpinnerWithData();
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
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
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

    protected void setUXWithData(String selectedPlace){
        Cursor placeDescCursor = dbCursor.rawQuery("select * from placeDescriptions " +
                "where name like '" + selectedPlace + "';",
                new String[]{});

        while (placeDescCursor.moveToNext()){
            try {
                firstLatitude = placeDescCursor.getDouble(6);
                firstLongitude = placeDescCursor.getDouble(7);

                etName.setText(placeDescCursor.getString(0));
                etDescription.setText(placeDescCursor.getString(1));
                etCategory.setText(placeDescCursor.getString(2));
                etAddressTitle.setText(placeDescCursor.getString(3));
                etAddressStreet.setText(placeDescCursor.getString(4));
                etElevation.setText(Double.toString(placeDescCursor.getDouble(5)));
                etLatitude.setText(Double.toString(firstLatitude));
                etLongitude.setText(Double.toString(firstLongitude));
                etImage.setText(placeDescCursor.getString(8));
            } catch (Exception e ) {
                Log.w(TAG, "Error while getting place descriptions " + e.getMessage());
            }
        }
    }

    protected void setSpinnerWithData(){
        Cursor allPlacesCursor = dbCursor.rawQuery("select name from placeDescriptions;",
                new String[]{});
        ArrayList<String> places = new ArrayList<>();

        while (allPlacesCursor.moveToNext()){
            try {
                places.add(allPlacesCursor.getString(0));
            } catch (Exception e) {
                Log.w(TAG, "Error while getting name of places " + e.getMessage());
            }
        }

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                places);
        spinPlaces.setAdapter(spinAdapter);

        spinPlaces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String secondPlace = adapterView.getItemAtPosition(i).toString();

                Cursor secondPlaceCursor = dbCursor.rawQuery("select latitude, longitude " +
                                "from placeDescriptions where name like '" + secondPlace + "';",
                        new String[]{});

                while(secondPlaceCursor.moveToNext()){
                    try {
                        secondLatitude = secondPlaceCursor.getDouble(0);
                        secondLongitude = secondPlaceCursor.getDouble(1);
                    } catch (Exception e) {
                        Log.w(TAG, "Error while getting second place latitude and longitude");
                    }
                }

                Double greatCircle = calculateGreatCircle(firstLatitude,
                        firstLongitude,
                        secondLatitude,
                        secondLongitude);

                Double initialBearing = calculateIntialBearing(firstLatitude,
                        firstLongitude,
                        secondLatitude,
                        secondLongitude);

                distance.setText("Distance: " + Double.toString(greatCircle) +
                        "\nInitial Bearing: " + Double.toString(initialBearing));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            String newPlaceName = etName.getText().toString();
            String placeDesc =  etDescription.getText().toString();
            String placeCategory = etCategory.getText().toString();
            String placeAddressTitle = etAddressTitle.getText().toString();
            String placeAddressStreet = etAddressStreet.getText().toString();
            String placeElevation = etElevation.getText().toString();
            String placeLatitude = etLatitude.getText().toString();
            String placeLongitude = etLongitude.getText().toString();
            String placeImage = etImage.getText().toString();

            if (callingActivity.equals("MainActivity")) {
                String deleteCommand = "delete from placeDescriptions where name like '"
                        + placeName + "';";

                dbCursor.execSQL(deleteCommand);
            }

            String insertCommand = "insert into placeDescriptions values ('" + newPlaceName + "', '" +
                    placeDesc + "', '" + placeCategory + "', '" + placeAddressTitle + "', '" +
                    placeAddressStreet + "', " + Double.parseDouble(placeElevation) + ", " +
                    Double.parseDouble(placeLatitude) + ", " + Double.parseDouble(placeLongitude)
                    + ", '" + placeImage + "');";

            dbCursor.execSQL(insertCommand);

            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
        else {
            Toast.makeText(this, "Enter all the fields!", Toast.LENGTH_SHORT).show();
        }
    }

    public void AEP_RemoveOnClick(View view) {
        String deleteCommand = "delete from placeDescriptions where name like '"
                + placeName + "';";

        dbCursor.execSQL(deleteCommand);

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public double calculateGreatCircle(double firstLatitude,
                                       double firstLongitude,
                                       double secondLatitude,
                                       double secondLongitude) {
        double greatDistance;

        double R = 6371 * pow(10, 3);

        double phi1 = Math.toRadians(firstLatitude);
        double phi2 = Math.toRadians(secondLatitude);

        double deltaPhi = Math.toRadians(secondLatitude - firstLatitude);
        double deltaLambda = Math.toRadians(secondLongitude - firstLongitude);

        double a = Math.sin(deltaPhi/2)
                * Math.sin(deltaPhi/2)
                + Math.cos(phi1)
                * Math.cos(phi2)
                * Math.sin(deltaLambda/2)
                * Math.sin(deltaLambda/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        greatDistance = R * c;

        return greatDistance;
    }

    public double calculateIntialBearing(double firstLatitude,
                                         double firstLongitude,
                                         double secondLatitude,
                                         double secondLongitude) {
        double bearing;

        double phi1 = Math.toRadians(firstLatitude);
        double lambda1 = Math.toRadians(firstLongitude);

        double phi2 = Math.toRadians(secondLatitude);
        double lambda2 = Math.toRadians(secondLongitude);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2)
                - Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);

        double aTan2 = Math.atan2(y, x);
        bearing = Math.toDegrees(aTan2);

        return bearing;
    }
}
