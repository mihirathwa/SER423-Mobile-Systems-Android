/*
 * Copyright 2017 Mihir Rathwa,
 *
 * This license provides the instructor Dr. Tim Lindquist and Arizona
 * State University the right to build and evaluate the package for the
 * purpose of determining grade and program assessment.
 *
 * Purpose: This class contains the Alert activity invoking methods as required
 * in Assignment 2
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Mihir Rathwa Mihir.Rathwa@asu.edu
 *         Software Engineering, CIDSE, ASU Poly
 * @version January 24, 2017
 */

package edu.asu.msse.mrathwa.androidlife;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
    }

    /*
    This method is invoked when the user presses the Home button when the Alert Activity is
    in foreground and the launches the app again by clicking the app icon
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        android.util.Log.w(this.getClass().getSimpleName(), "onRestart");
    }

    /*
    This method is invoked when the user lands on this Activity by clicking the button
    from the last activity or when the user clicks the app when this activity is in foreground
     */
    @Override
    protected void onStart() {
        super.onStart();

        android.util.Log.w(this.getClass().getSimpleName(), "onStart");
    }

    /*
    This method is invoked when the user lands on this Activity by clicking the button
    from the last activity or when the user clicks the app when activity is in foreground
     */
    @Override
    protected void onResume() {
        super.onResume();

        android.util.Log.w(this.getClass().getSimpleName(), "onResume");
    }

    /*
    This method is invoked when the user clicks OK on the alert dialog box or when
    the home button is clicked when the user clicks Home Button on Emulator
     */
    @Override
    protected void onPause() {
        super.onPause();

        android.util.Log.w(this.getClass().getSimpleName(), "onPause");
    }

    /*
    This activity is invoked after clicking OK on dialog box or when the user clicks the
    Home Button on Emulator or when the user clicks Power Button of phone
     */
    @Override
    protected void onStop() {
        super.onStop();

        android.util.Log.w(this.getClass().getSimpleName(), "onStop");
    }

    /*
    This activity is invoked after clicking OK on dialog box
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        android.util.Log.w(this.getClass().getSimpleName(), "onDestroy");
    }

    public void AA_btnOK_onClick(View view) {
        finish();
    }
}
