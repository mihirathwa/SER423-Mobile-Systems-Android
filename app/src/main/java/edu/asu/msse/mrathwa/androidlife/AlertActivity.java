package edu.asu.msse.mrathwa.androidlife;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Hello Android Developer");
        alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog showAlert = alertDialog.create();
        showAlert.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        android.util.Log.w(this.getClass().getSimpleName(), "onRestart");
    }

    /*
    This method is invoked when the user lands on this Activity by clicking the button
    from the last activity
     */
    @Override
    protected void onStart() {
        super.onStart();

        android.util.Log.w(this.getClass().getSimpleName(), "onStart");
    }

    /*
    This method is invoked when the user lands on this Activity by clicking the button
    from the last activity, invoked after onStart()
     */
    @Override
    protected void onResume() {
        super.onResume();

        android.util.Log.w(this.getClass().getSimpleName(), "onResume");
    }

    /*
    This method is invoked when the user clicks OK on the alert dialog box
     */
    @Override
    protected void onPause() {
        super.onPause();

        android.util.Log.w(this.getClass().getSimpleName(), "onPause");
    }

    /*
    This activity is invoked after clicking OK on dialog box
     */
    @Override
    protected void onStop() {
        super.onStop();

        android.util.Log.w(this.getClass().getSimpleName(), "onStop");
    }

    /*
    This activity is invoked after clicking OK on dialog box, will
    come after onStop()
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        android.util.Log.w(this.getClass().getSimpleName(), "onDestroy");
    }
}
