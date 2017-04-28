package edu.asu.msse.mrathwa.placeman;

/*
 * Copyright 2017 Mihir Rathwa,
 *
 * This license provides the instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class shows the Map and sets markers with
 * ability to Add a place, long press to edit it
 * for Extended Lab
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Mihir Rathwa Mihir.Rathwa@asu.edu
 *         Software Engineering, CIDSE, ASU Poly
 * @version April 28, 2017
 */

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener,
        DialogInterface.OnClickListener {

    private String TAG = getClass().getSimpleName();
    private Context context;
    private EditText dialogEditText;
    private GoogleMap gMap;
    private LatLng newPoint;

    private String dialogCallBox;

    PlaceDescriptionDB db;
    SQLiteDatabase dbCursor;

    private String selectedPlace;

    private HashMap<String, LatLng> places;

    static final LatLng myPos = new LatLng(40, -79);

    private MapFragment googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.w(TAG, "onCreate");
        context = this;

        places = new HashMap<>();

        db = new PlaceDescriptionDB(this);
        dbCursor = db.openDB();

        Cursor placesCursor = dbCursor.rawQuery("select name, latitude, longitude from placeDescriptions;", new String[]{});

        while(placesCursor.moveToNext()) {
            try {
                places.put(placesCursor.getString(0), new LatLng(placesCursor.getDouble(1), placesCursor.getDouble(2)));
            } catch (Exception e) {
                Log.w(TAG, "Exception while getting latitude, longitude from database" + e.getMessage());
            }
        }

        try {
            if(googleMap == null) {
                googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.mapsfragment);
                googleMap.getMapAsync(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setTrafficEnabled(true);
        gMap.setBuildingsEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);

        gMap.setOnMapLongClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                LatLng coordinate = new LatLng(latitude, longitude);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 1);
                gMap.animateCamera(yourLocation);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        for (String placeName: places.keySet()) {
            gMap.addMarker(new MarkerOptions()
                    .position(places
                            .get(placeName))
                    .title(placeName)
                    .snippet("Long Press to Edit"));
        }

        gMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                selectedPlace = marker.getTitle();

                dialogCallBox = "edit";
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Edit this Place?");
                alert.setPositiveButton("OK", (DialogInterface.OnClickListener) context);
                alert.setNegativeButton("Cancel", (DialogInterface.OnClickListener) context);
                alert.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.w(TAG, "onActivityResult");

                places = new HashMap<>();
                Cursor placesCursor = dbCursor.rawQuery("select name, latitude, longitude from placeDescriptions;", new String[]{});

                while(placesCursor.moveToNext()) {
                    try {
                        places.put(placesCursor.getString(0), new LatLng(placesCursor.getDouble(1), placesCursor.getDouble(2)));
                    } catch (Exception e) {
                        Log.w(TAG, "Exception while getting latitude, longitude from database" + e.getMessage());
                    }
                }

                gMap.clear();

                for (String placeName: places.keySet()) {
                    gMap.addMarker(new MarkerOptions()
                            .position(places
                                    .get(placeName))
                            .title(placeName)
                            .snippet("Long Press to Edit"));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        newPoint = latLng;

        dialogEditText = new EditText(this);
        dialogEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        dialogCallBox = "add";
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add a New Place?");
        alert.setView(dialogEditText);
        alert.setPositiveButton("OK", this);
        alert.setNegativeButton("Cancel", this);
        alert.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == DialogInterface.BUTTON_POSITIVE) {
            if (dialogCallBox == "add") {
                String newPlaceName = dialogEditText.getText().toString();
                Double newLatitude = newPoint.latitude;
                Double newLongitude = newPoint.longitude;

                gMap.addMarker(new MarkerOptions()
                        .position(newPoint)
                        .title(newPlaceName)
                        .snippet("Long Press to Edit"));

                String insertCommand = "insert into placeDescriptions (name, latitude, longitude) values ('"
                        + newPlaceName + "', " + newLatitude + ", " + newLongitude + ");";

                dbCursor.execSQL(insertCommand);
            }
            else if (dialogCallBox == "edit") {
                Intent intent = new Intent(context, EditPlaceActivity.class);
                intent.putExtra("placeName", selectedPlace);
                intent.putExtra("callingActivity", "MapsActivity");
                startActivityForResult(intent, 1);
            }
        }
        else if (i == DialogInterface.BUTTON_NEGATIVE) {
            Toast.makeText(this, "No Action", Toast.LENGTH_SHORT).show();
        }
    }
}
