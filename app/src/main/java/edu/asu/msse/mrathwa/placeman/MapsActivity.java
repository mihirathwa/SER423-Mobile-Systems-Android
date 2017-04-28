package edu.asu.msse.mrathwa.placeman;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String TAG = getClass().getSimpleName();
    private Context context;

    PlaceDescriptionDB db;
    SQLiteDatabase dbCursor;

    private List<LatLng> placeLocations;
    private List<String> placeNames;

    static final LatLng myPos = new LatLng(40, -79);

    private MapFragment googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Log.w(TAG, "onCreate");
        context = this;

        placeLocations = new ArrayList<LatLng>();
        placeNames = new ArrayList<String>();

        db = new PlaceDescriptionDB(this);
        dbCursor = db.openDB();

        Cursor placesCursor = dbCursor.rawQuery("select name, latitude, longitude from placeDescriptions;", new String[]{});

        while(placesCursor.moveToNext()) {
            try {
                placeLocations.add(new LatLng(placesCursor.getDouble(1), placesCursor.getDouble(2)));
                placeNames.add(placesCursor.getString(0));
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
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                LatLng coordinate = new LatLng(latitude, longitude);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 1);
                googleMap.animateCamera(yourLocation);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        int i = 0;
        for (LatLng placeLatLng: placeLocations) {
            googleMap.addMarker(new MarkerOptions().position(placeLatLng).title(placeNames.get(i)));
            i++;
        }

//        googleMap.addMarker(new MarkerOptions().position(myPos).title("Take me Here!"));
    }
}
