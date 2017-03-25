package edu.asu.msse.mrathwa.placeman;

/*
 * Copyright 2017 Mihir Rathwa,
 *
 * This license provides the instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class contains the handles:
 * 1. server method invokation via AsycTaskHandler.java
 * 2. manipulating server response
 * 3. setting relevent UI adapters for MainActivity and EditPlaceActivity
 * for Assignment 5
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Mihir Rathwa Mihir.Rathwa@asu.edu
 *         Software Engineering, CIDSE, ASU Poly
 * @version March 23, 2017
 */

/**
 * Created by Mihir on 03/23/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;


import static java.lang.Math.pow;

public class PlacesHandler implements AsyncTaskResponseListener {

    private String TAG = this.getClass().getSimpleName();
    private Context context;

    private List<String> categories;
    private List<String> categoryPlaceNames;
    private List<String> allPlaceNames;

    private String[] currentCategory;
    private int inCategoryCount = 0;
    private int outCategoryCount = 0;
    private String callingPurpose = "setEditPlaceUI";

    private Map<String, List<String>> placesofEachCategory = new HashMap<>();

    private PlaceDescription placeDescription = null;

    private ExpandableListAdapter placeListAdapter;
    private ExpandableListView expandableListView;

    private Spinner spinPlaces;
    private TextView distance;
    private Button btnRemove;

    private EditText etName;
    private EditText etDescription;
    private EditText etCategory;
    private EditText etAddressTitle;
    private EditText etAddressStreet;
    private EditText etElevation;
    private EditText etLatitude;
    private EditText etLongitude;
    private EditText etImage;

    public PlacesHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onAsyncTaskResponse(String requestId, String result) {
        Log.w(TAG, "Server Request: " + requestId + " Response: " + result);

        JSONObject responseJSON = stringToJSON(result);

        switch (requestId) {
            case "getNames":
                allPlaceNames = jsonToList(responseJSON);
                setEditPlaceUXSpinner();

                break;

            case "getCategoryNames":
                categories = jsonToList(responseJSON);
                currentCategory = new String[categories.size()];

                for (String s : categories){
                    currentCategory[inCategoryCount] = s;
                    getPlacesForCategory(s);
                    inCategoryCount++;
                }

                break;

            case "getNamesInCategory":
                categoryPlaceNames = jsonToList(responseJSON);

                if(outCategoryCount < inCategoryCount){
                    placesofEachCategory.put(currentCategory[outCategoryCount],
                            categoryPlaceNames);
                    outCategoryCount++;
                }

                setExpandableListView();
                break;

            case "get":
                if (callingPurpose.equals("setEditPlaceUI")){
                    placeDescription = jsonToPlaceObject(responseJSON);
                    setEditPlaceUXwithData();
                }
                else if (callingPurpose.equals("getSpinnerPlace")) {
                    double firstLatitude = placeDescription.getLatitude();
                    double firstLongitude = placeDescription.getLongitude();

                    PlaceDescription spinnerPlace = jsonToPlaceObject(responseJSON);
                    double secondLatitude = spinnerPlace.getLatitude();
                    double secondLongitude = spinnerPlace.getLongitude();

                    double greatCircle = calculateGreatCircle(firstLatitude,
                            firstLongitude,
                            secondLatitude,
                            secondLongitude);

                    double initialBearing = calculateIntialBearing(firstLatitude,
                            firstLongitude,
                            secondLatitude,
                            secondLongitude);

                    distance.setText("Distance:                 " + Double.toString(greatCircle)
                            + "\nInitial bearing:    " + Double.toString(initialBearing));
                }

                callingPurpose = "setEditPlaceUI";

                break;

            case "add":
                Log.w(TAG, "Place added");
                Intent addIntent = new Intent(context, MainActivity.class);
                context.startActivity(addIntent);

                break;

            case "remove":
                Log.w(TAG, "Place removed");
                Intent removeIntent = new Intent(context, MainActivity.class);
                context.startActivity(removeIntent);

                break;

            case "saveToJsonFile":
                Log.w(TAG, "Saved to Server JSON File");
                break;

            case "resetFromJsonFile":
                Log.w(TAG, "Data Reset from Server JSON File");
                break;

            default:
                Log.w(TAG, "Invalid Calling Method");
                break;
        }


    }

    public void getAllPlaces(){
        PlaceRequestHandler placeRequest =
                new PlaceRequestHandler(context,
                        "getNames",
                        new String[]{" "});
        AsyncTaskHandler callAsyncTask = new AsyncTaskHandler("getNames", this);
        callAsyncTask.execute(placeRequest);
    }

    public void getAllCategories(){
        PlaceRequestHandler placeRequest = new PlaceRequestHandler(context,
                "getCategoryNames",
                new String[]{" "});
        AsyncTaskHandler callAsyncTask = new AsyncTaskHandler("getCategoryNames", this);
        callAsyncTask.execute(placeRequest);
    }

    private void getPlacesForCategory(String category){
        PlaceRequestHandler placeRequest = new PlaceRequestHandler(context,
                "getNamesInCategory",
                new String[]{category});
        AsyncTaskHandler callAsyncTask = new AsyncTaskHandler("getNamesInCategory", this);
        callAsyncTask.execute(placeRequest);
    }

    public void getPlaceDescription(String placeName){
        PlaceRequestHandler placeRequest = new PlaceRequestHandler(context,
                "get",
                new String[]{placeName});
        AsyncTaskHandler callAsyncTask = new AsyncTaskHandler("get", this);
        callAsyncTask.execute(placeRequest);
    }

    public void addPlace(PlaceDescription placeObject) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address-title", placeObject.getAddress_title());
            jsonObject.put("address-street", placeObject.getAddress_street());
            jsonObject.put("elevation", placeObject.getElevation());
            jsonObject.put("image", placeObject.getImage());
            jsonObject.put("latitude", placeObject.getLatitude());
            jsonObject.put("longitude", placeObject.getLongitude());
            jsonObject.put("name", placeObject.getName());
            jsonObject.put("description", placeObject.getDescription());
            jsonObject.put("category", placeObject.getCategory());

            System.out.println("JSON Object: " + jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        PlaceRequestHandler placeRequest = new PlaceRequestHandler(context,
                "add",
                new Object[]{jsonObject});

        AsyncTaskHandler callAsyncTask = new AsyncTaskHandler("add", this);
        callAsyncTask.execute(placeRequest);
    }

    public void removePlace(String placeName){
        PlaceRequestHandler placeRequest = new PlaceRequestHandler(context,
                "remove",
                new String[]{placeName});
        AsyncTaskHandler callAsyncTask = new AsyncTaskHandler("remove", this);
        callAsyncTask.execute(placeRequest);
    }

    public void savePlace(){
        PlaceRequestHandler placeRequest = new PlaceRequestHandler(context,
                "saveToJsonFile",
                new String[]{});
        AsyncTaskHandler callAsyncTask = new AsyncTaskHandler("saveToJsonFile", this);
        callAsyncTask.execute(placeRequest);
    }

    public void resetPlaces() {
        PlaceRequestHandler placeRequest = new PlaceRequestHandler(context,
                "resetFromJsonFile",
                new String[]{});
        AsyncTaskHandler callAsyncTask = new AsyncTaskHandler("resetFromJsonFile", this);
        callAsyncTask.execute(placeRequest);
    }

    protected JSONObject stringToJSON (String jsonString) {
        JSONObject jsonResponse = null;

        try {
            jsonResponse = new JSONObject(jsonString);
        }
        catch (Exception e) {
            Log.w("stringToJSON: ", "Error converting String to JSON");
            e.printStackTrace();
        }

        return jsonResponse;
    }

    protected List<String> jsonToList (JSONObject listJSON){
        List<String> stringList = new ArrayList<String>();

        try {
            JSONArray placeArray = listJSON.getJSONArray("result");

            if (placeArray != null){
                for (int i = 0; i < placeArray.length(); i++){
                    stringList.add(placeArray.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stringList;
    }

    protected PlaceDescription jsonToPlaceObject (JSONObject jsonObject){
        PlaceDescription placeObject = null;

        try {
            JSONObject placeJSON = jsonObject.getJSONObject("result");

            String placeName = placeJSON.getString("name");
            String description = placeJSON.getString("description");
            String category = placeJSON.getString("category");
            String address_title = placeJSON.getString("address-title");
            String address_street = placeJSON.getString("address-street");
            Double elevation = Double.valueOf(placeJSON.getString("elevation"));
            Double latitude = Double.valueOf(placeJSON.getString("latitude"));
            Double longitude = Double.valueOf(placeJSON.getString("longitude"));
            String image = placeJSON.getString("image");

            placeObject = new PlaceDescription(placeName,
                    description,
                    category,
                    address_title,
                    address_street,
                    elevation,
                    latitude,
                    longitude,
                    image);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return placeObject;
    }

    protected void setMainActivityUITags(ExpandableListView expandableListViewTag){
        this.expandableListView = expandableListViewTag;
    }

    protected void setEditPlaceUITags(EditText etNameTag,
                                      EditText etDescriptionTag,
                                      EditText etCategoryTag,
                                      EditText etAddressTitleTag,
                                      EditText etAddressStreetTag,
                                      EditText etElevationTag,
                                      EditText etLatitudeTag,
                                      EditText etLongitudeTag,
                                      EditText etImageTag,
                                      Spinner spinPlacesTag,
                                      TextView distanceTag,
                                      Button btnRemoveTag) {
        this.etName = etNameTag;
        this.etDescription = etDescriptionTag;
        this.etCategory = etCategoryTag;
        this.etAddressTitle = etAddressTitleTag;
        this.etAddressStreet = etAddressStreetTag;
        this.etElevation = etElevationTag;
        this.etLatitude = etLatitudeTag;
        this.etLongitude = etLongitudeTag;
        this.etImage = etImageTag;
        this.spinPlaces = spinPlacesTag;
        this.distance = distanceTag;
        this.btnRemove = btnRemoveTag;
    }

    protected void setExpandableListView(){
        placeListAdapter = new MyExpandableListAdapter(context, categories, placesofEachCategory);
        expandableListView.setAdapter(placeListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String placeName = placesofEachCategory.get(categories.get(i)).get(i1);

                Intent intent = new Intent(context, EditPlaceActivity.class);
                //intent.putExtra("placeLibrary", placeLibrary);
                intent.putExtra("placeName", placeName);
                intent.putExtra("callingActivity", "MainActivity");
                context.startActivity(intent);

                return false;
            }
        });
    }

    protected void setEditPlaceUXwithData() {

        etName.setText(placeDescription.getName());
        etDescription.setText(placeDescription.getDescription());
        etCategory.setText(placeDescription.getCategory());
        etAddressTitle.setText(placeDescription.getAddress_title());
        etAddressStreet.setText(placeDescription.getAddress_street());
        etElevation.setText(Double.toString(placeDescription.getElevation()));
        etLatitude.setText(Double.toString(placeDescription.getLatitude()));
        etLongitude.setText(Double.toString(placeDescription.getLongitude()));
        etImage.setText(placeDescription.getImage());
    }

    protected void setEditPlaceUXSpinner(){
        //ArrayList<String> places = new ArrayList<String>();
        //allPlaceNames.addAll(places);

        ArrayAdapter<String> spinAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, allPlaceNames);
        spinPlaces.setAdapter(spinAdapter);

        spinPlaces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedPlace = adapterView.getItemAtPosition(i).toString();

                callingPurpose = "getSpinnerPlace";
                getPlaceDescription(selectedPlace);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
