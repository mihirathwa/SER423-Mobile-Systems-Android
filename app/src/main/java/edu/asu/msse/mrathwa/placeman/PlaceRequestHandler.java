package edu.asu.msse.mrathwa.placeman;

import android.content.Context;

/**
 * Created by Mihir on 03/23/2017.
 */

public class PlaceRequestHandler {
    private Context context;
    private String methodName;
    private String[] params;

    public PlaceRequestHandler(Context context, String methodName, String[] params) {
        this.context = context;
        this.methodName = methodName;
        this.params = params;
    }

    public Context getContext() {
        return context;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParams() {
        return params;
    }
}
