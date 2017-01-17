package edu.asu.msse.mrathwa.placeman;

/*
 * Copyright 2017 Mihir Rathwa,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This license also provides instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class contains description about the place as described
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