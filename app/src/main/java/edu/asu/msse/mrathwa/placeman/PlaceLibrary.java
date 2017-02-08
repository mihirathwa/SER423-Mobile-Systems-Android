package edu.asu.msse.mrathwa.placeman;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Mihir on 02/07/2017.
 */

public class PlaceLibrary {

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
}
