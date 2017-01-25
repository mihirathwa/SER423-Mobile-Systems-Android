package edu.asu.msse.mrathwa.androidlife;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.util.Log.w(this.getClass().getSimpleName(), "onCreate");
    }

    /*
    This method is invoked when the user clicks OK in the alert dialog box
    in the Alert Activity
    Or when the app is launched again via Home Screen from the Nexus5 Emulator
     */
    @Override
    protected void onRestart() {
        super.onRestart();

        android.util.Log.w(this.getClass().getSimpleName(), "onRestart");
    }

    /*
    This method is invoked when the user clicks OK in the alert dialog box
    in the Alert Activity
    Or when the app is launched again via Home Screen from the Nexus5 Emulator
     */
    @Override
    protected void onStart() {
        super.onStart();

        android.util.Log.w(this.getClass().getSimpleName(), "onStart");
    }

    /*
    This method will be invoked when user clicks the button "Click Here!"
    or when the Home button is clicked for Nexus5 Emulator
    */
    @Override
    protected void onPause() {
        super.onPause();

        android.util.Log.w(this.getClass().getSimpleName(), "onPause");
    }

    /*
    This method will be invoked when user clicks the button "Click Here!"
    or when the Home button is clicked for Nexus5 Emulator or when the
    Phone lock button is clicked on Phone
    */
    @Override
    protected void onStop() {
        super.onStop();

        android.util.Log.w(this.getClass().getSimpleName(), "onStop");
    }

    /*
    This method will be invoked when the user has the first screen in foreground
    and kills the app by swiping off the app from recent activity view in Android Emulator
    Clicking the multi-view hard button on Emulator and swiping the app left or right
    will invoke the below method
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        android.util.Log.w(this.getClass().getSimpleName(), "onDestroy");
    }



    public void MA_btnCH_onClick(View view) {

        Intent gotoAlertActivity = new Intent(this, AlertActivity.class);
        startActivity(gotoAlertActivity);
    }
}
