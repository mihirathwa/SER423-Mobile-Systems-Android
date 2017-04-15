package edu.asu.msse.mrathwa.placeman;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Mihir on 04/14/2017.
 */

public class PlaceDescriptionDB extends SQLiteOpenHelper {
    private final String TAG = this.getClass().getSimpleName();

    private static final boolean debugon = false;
    private static final int DATABASE_VERSION = 3;
    private static String dbName = "placeDB";
    private String dbPath;
    private SQLiteDatabase cursorDB;
    private final Context context;


    public PlaceDescriptionDB(Context context) {
        super(context, dbName, null, DATABASE_VERSION);
        this.context = context;
        dbPath = context.getFilesDir().getPath() + "/";

        Log.d(TAG, "dbPath: " + dbPath);
    }

    private void debug(String hdr, String msg){
        Log.d(hdr, msg);
    }

    public void createDB() {
        this.getReadableDatabase();
        copyDB();
    }

    private boolean checkDB() {
        SQLiteDatabase checkDB = null;
        boolean returnValue = false;

        try {
            String path = dbPath + dbName + ".db";
            debug("PlaceDB:: checkDB", "Opened DB at: " + checkDB.getPath());

            File aFile = new File(path);

            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB != null) {
                    debug("PlaceDB:: CheckDB", "Opened DB at " + checkDB.getPath());

                    Cursor tabCheck = checkDB.rawQuery("SELECT name FROM sqlite_master " +
                            "WHERE TYPE='table' AND " +
                            "NAME='placeDescriptions';", null);
                    Boolean cursorTabExists = false;

                    if(tabCheck == null){
                        debug("PlaceDB:: CheckDB", "Check for PlaceDescription Table " +
                                "result set is null");
                    }
                    else {
                        tabCheck.moveToNext();
                        debug("PlaceDB:: CheckDB", "Check for PlaceDescription Table " +
                                "result set is " +
                                (tabCheck.isAfterLast() ?
                                        "Empty" : (String) tabCheck.getString(0)));
                        cursorTabExists = !tabCheck.isAfterLast();
                    }

                    if(cursorTabExists) {
                        Cursor tableCursor =
                                checkDB.rawQuery("SELECT * FROM placeDescriptions", null);
                        tableCursor.moveToFirst();

                        while(!tableCursor.isAfterLast()) {
                            String cursorName = tableCursor.getString(0);
                            int cursorID = tableCursor.getInt(1);

                            debug("PlaceDB:: CheckDB", "Place Table has TableName: " +
                                    cursorName + "\tCourseID: " + cursorID);
                            tableCursor.moveToNext();
                        }

                        returnValue = true;
                    }
                }
            }
        } catch (SQLiteException e) {
            Log.w("CourseDB:: CheckDB", e.getMessage());
        }

        if(checkDB != null){
            checkDB.close();
        }

        return returnValue;
    }

    public void copyDB() {
        try {
            if(!checkDB()){
                debug("PlaceDB:: CopyDB", "checkDB false, Copying data");

                InputStream inputStream = context.getResources().openRawResource(R.raw.placedb);
                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op = dbPath + dbName + ".db";
                OutputStream outputStream = new FileOutputStream(op);

                byte[] buffer = new byte[1024];
                int length;

                while((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            Log.w("CourseDb:: CopyDb", "IOException: " + e.getMessage());
        }
    }

    public SQLiteDatabase openDB() {
        String myPath = dbPath + dbName + ".db";
        if (checkDB()) {
            cursorDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            debug("CourseDB:: OpenDB", "Opened DB at Path: " + cursorDB.getPath());
        } else {
            try {
                this.copyDB();
                cursorDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            } catch (Exception e) {
                Log.w(TAG, "Unable to Copy and Open DB: " + e.getMessage());
            }
        }

        return cursorDB;
    }

    @Override
    public synchronized void close() {
        if(cursorDB != null){
            cursorDB.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
