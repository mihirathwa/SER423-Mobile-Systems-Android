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
 * for Assignment 3
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

public class PlaceRequestHandler {
    private Context context;
    private String methodName;
    private Object[] params;

    public PlaceRequestHandler(Context context, String methodName, Object[] params) {
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

    public Object[] getParams() {
        return params;
    }
}
