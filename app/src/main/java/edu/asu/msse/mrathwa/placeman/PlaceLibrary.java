package edu.asu.msse.mrathwa.placeman;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Math.pow;

/**
 * Created by Mihir on 02/07/2017.
 */

public class PlaceLibrary implements Serializable{

    private HashMap<String, PlaceDescription> placesCatalogue;

    public PlaceLibrary(Context context) {

        String placesCatalogue = readJSONFile(context);
        JSONObject jsonObject = stringToJSON(placesCatalogue);
        this.placesCatalogue = jsonToHashMap(jsonObject);
    }

    public HashMap<String, PlaceDescription> getPlacesCatalogue() {
        return placesCatalogue;
    }

    public void setPlacesCatalogue(HashMap<String, PlaceDescription> placesCatalogue) {
        this.placesCatalogue = placesCatalogue;
    }

    protected String readJSONFile (Context context) {
        Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(R.raw.places);
        InputStreamReader inputStreamReader =  new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuffer stringBuffer = new StringBuffer();
        String data = null;
        String jsonString = null;

        try {
            while ((data = bufferedReader.readLine()) != null) {
                stringBuffer.append(data);
            }

            jsonString = stringBuffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    protected JSONObject stringToJSON (String jsonString) {
        JSONObject jsonPlaces = null;

        try {
            jsonPlaces = new JSONObject(jsonString);
        }
        catch (Exception e) {
            Log.w("stringToJSON: ", "Error converting String to JSON");
            e.printStackTrace();
        }

        return jsonPlaces;
    }

    protected HashMap jsonToHashMap (JSONObject jsonObject) {

        HashMap<String, PlaceDescription> places = new HashMap<>();

        Iterator<String> iterator = jsonObject.keys();

        PlaceDescription placeDescription;

        while (iterator.hasNext()) {
            String placeKey = iterator.next();

            try {
                JSONObject place = (JSONObject) jsonObject.get(placeKey);

                placeDescription = new PlaceDescription(place.getString("name"),
                        place.getString("description"),
                        place.getString("category"),
                        place.getString("address-title"),
                        place.getString("address-street"),
                        place.getDouble("elevation"),
                        place.getDouble("latitude"),
                        place.getDouble("longitude"),
                        place.getString("image"));

                places.put(placeKey, placeDescription);
            }
            catch (JSONException j) {
                Log.w("jsonToHashMap: ", "Error converting JSON to HashMap");
                j.printStackTrace();
            }
        }

        return places;
    }

    public double calculateGreatCircle(String firstPlace, String secondPlace) {
        double greatDistance;

        double firstLatitude = placesCatalogue.get(firstPlace).getLatitude();
        double firstLongitude = placesCatalogue.get(firstPlace).getLongitude();

        double secondLatitude = placesCatalogue.get(secondPlace).getLatitude();
        double secondLongitude = placesCatalogue.get(secondPlace).getLongitude();

        double R = 6371 * pow(10, 3);

        double phi1 = Math.toRadians(firstLatitude);
        double phi2 = Math.toRadians(secondLatitude);

        double deltaPhi = Math.toRadians(secondLatitude - firstLatitude);
        double deltaLambda = Math.toRadians(secondLongitude - firstLongitude);

        double a = Math.cos(deltaPhi/2)
                * Math.sin(deltaPhi/2)
                + Math.cos(phi1)
                * Math.cos(phi2)
                * Math.sin(deltaLambda/2)
                * Math.sin(deltaLambda/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        greatDistance = R * c;

        return greatDistance;
    }

    public double calculateIntialBearing(String firstPlace, String secondPlace) {
        double bearing;

        double firstLatitude = placesCatalogue.get(firstPlace).getLatitude();
        double firstLongitude = placesCatalogue.get(firstPlace).getLongitude();

        double secondLatitude = placesCatalogue.get(secondPlace).getLatitude();
        double secondLongitude = placesCatalogue.get(secondPlace).getLongitude();

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

    public void removeJSONObject (String placeName) {
        placesCatalogue.remove(placeName);

        Log.w("PlaceLibrary", placeName + " removed");
    }

    public void addJSONObject (PlaceDescription placeDescription) {
        placesCatalogue.put(placeDescription.getName(), placeDescription);

        Log.w("PlaceLibrary", placeDescription.getName() + " added");
    }
}
