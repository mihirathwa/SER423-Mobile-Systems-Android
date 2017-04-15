package edu.asu.msse.mrathwa.placeman;

/*
 * Copyright 2017 Mihir Rathwa,
 *
 * This license provides the instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class connects to Server using Async Task over Http
 * and gets the generated response
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
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class AsyncTaskHandler extends AsyncTask<PlaceRequestHandler, Integer, String>{

    private String TAG = this.getClass().getSimpleName();
    private AsyncTaskResponseListener responseListener;
    private String requestId = null;

    public AsyncTaskHandler(String requestId, AsyncTaskResponseListener responseListener) {
        this.requestId = requestId;
        this.responseListener = responseListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.w(TAG, "onPreExecute");
    }

    @Override
    protected String doInBackground(PlaceRequestHandler... placeRequestHandlers) {
        Log.w(TAG, "doInBackground");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            //URL defaultURL = new URL("http://10.0.2.2:8080"); //Android Emulator
            //URL deviceURL = new URL("http://192.168.42.149:8080"); //My External Android device

            Context context = placeRequestHandlers[0].getContext();
            String stringURL = context.getString(R.string.device_url);
            URL serverURL = new URL(stringURL);

            HttpURLConnection conn = (HttpURLConnection) serverURL.openConnection();

            conn.addRequestProperty("Accept-Encoding", "gzip");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            Log.w(TAG, "Connecting...");
            conn.connect();
            Log.w(TAG, "Connection Established...");

            OutputStream out = null;
            try{
                Log.w(TAG, "trying to write Output Stream");
                out = conn.getOutputStream();

                JSONArray parameters = new JSONArray(placeRequestHandlers[0].getParams());

                System.out.println("JSON Parameters: " + parameters.toString());

                String outStr = "{\"jsonrpc\": \"2.0\",\"method\": \""
                        + placeRequestHandlers[0].getMethodName()
                        + "\", \"params\":"
                        + parameters.toString()
                        + ", \"id\": 3}";

                out.write(outStr.getBytes());
                out.flush();
                out.close();

                int statusCode = conn.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    throw new Exception("Unexpected status from post: " + statusCode);
                }
            }
            catch (Exception e){
                Log.w(TAG, "could not write Output Stream");
                System.out.println(e);
            }
            finally{
                if (out != null){
                    out.close();
                }
            }

            String responseEncoding = conn.getHeaderField("Content-Encoding");
            responseEncoding = (responseEncoding == null ? "" : responseEncoding.trim());
            InputStream in = conn.getInputStream();

            try {
                Log.w(TAG, "trying to get Input Stream");
                in = conn.getInputStream();

                if("gzip".equalsIgnoreCase(responseEncoding)) {
                    in = new GZIPInputStream(in);
                }

                in = new BufferedInputStream(in);
                byte[] buff = new byte[1024];
                int n;
                while ((n = in.read(buff)) > 0){
                    bos.write(buff, 0, n);
                }
                bos.flush();
                bos.close();
                Log.w(TAG, "Server Output: " + bos.toString());
            }
            catch (Exception e){
                Log.w(TAG, "could not get Input Stream");
                System.out.println(e);
            }
            finally {
                if (in != null){
                    in.close();
                }
            }
        }
        catch (Exception e){
            Log.w(TAG, "HTTP Error");
            System.out.println(e);
        }

        return bos.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.w(TAG, "onPostExecute");

        if (responseListener != null){
            responseListener.onAsyncTaskResponse(requestId, s);
        }
    }
}
