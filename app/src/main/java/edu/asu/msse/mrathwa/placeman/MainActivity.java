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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlaceDescription placeObj = new PlaceDescription("ASU-Poly", "Home of ASU's Software Engineering Programs",
                "School", "ASU Software Engineering", "7171 E Sonoran Arroyo Mall\nPeralta Hall 230\nMesa AZ 85212",
                1300.0, 33.306388, -111.679121);

        TextView setTitleText = (TextView) findViewById(R.id.tv_title);

        setTitleText.setText("Name: " + placeObj.getName() +
                "\n" + "Description: " + placeObj.getDescription() +
                "\n" + "Category: " + placeObj.getCategory() +
                "\n" + "Address Title: " + placeObj.getAddress_title() +
                "\n" + "Address Street: " + placeObj.getAddress_street() +
                "\n" + "Elevation: " +placeObj.getElevation() +
                "\n" + "Latitude: " + placeObj.getLatitude() +
                "\n" + "Longitude: " + placeObj.getLongitude());
    }
}