package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;


public class Menu3 extends Fragment {

    ServerDB serverDB = new ServerDB();
    getDataWebService webService = new getDataWebService();

    //location
    GPSTracker gps;

    View rootView;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private String latitude;
    private String longitude;
    private String SUBJECT_ID;
    private String SESSION_ID;
    private String STATUS_QIZ;
    private int COUNT_SHAKE = 0;

    DecimalFormat df = new DecimalFormat("####0.0000");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu3, container, false);

        SESSION_ID = getActivity().getIntent().getStringExtra("sessionID");

        gps = new GPSTracker(getActivity());

        if (gps.canGetLocation()) {
            latitude = String.valueOf(df.format(gps.getLatitude()));
            longitude = String.valueOf(df.format(gps.getLongitude()));
        } else {
            gps.showSettingsAlert();
        }

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });

        return rootView;
    }

    private void handleShakeEvent(int count) {
        myAsyncTask myAsyncTask = new myAsyncTask();
        myAsyncTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private class myAsyncTask extends AsyncTask<String, Void, Void> {

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Checking...");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                Thread.sleep(2000);
                serverDB.getData(latitude, longitude);
                SUBJECT_ID = serverDB.SubjectID;
                STATUS_QIZ = serverDB.StatusQiz;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            this.dialog.dismiss();

            if (serverDB.STATUS_SHAKE.equals("1")) {
                ShowAlertDialog("You are not in the area!");
            } else {
                if (serverDB.Status.equals("0")) {
                    ShowAlertDialog("Time Out Check in!");
                } else {
                    if (COUNT_SHAKE == 1) {
                        ShowAlertDialog("You have verified....");
                    } else {
                        Log.e("SubjectID", SUBJECT_ID);
                        Log.e("Lat Long", latitude + "/" + longitude);
                        myAsyncTaskShake myAsyncTaskShake = new myAsyncTaskShake();
                        myAsyncTaskShake.execute(SESSION_ID, SUBJECT_ID);
                        COUNT_SHAKE = 1;
                        ShowAlertDialog("Checked in SubjectID : " + SUBJECT_ID);
                        if (STATUS_QIZ.equals("null")) {
                            Log.e("STATUS", STATUS_QIZ);
                            myAsyncTaskShakeQiz taskShakeQiz = new myAsyncTaskShakeQiz();
                            taskShakeQiz.execute(SUBJECT_ID, "0");
                        } else {
                            Log.e("STATUS", "1");
                            myAsyncTaskShakeQiz taskShakeQiz = new myAsyncTaskShakeQiz();
                            taskShakeQiz.execute(SUBJECT_ID, "1");
                        }
                    }
                }
            }
        }
    }

    private class myAsyncTaskShake extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webService.getResultBySessionID(params[0]);
            String studentID = webService.StudentCode;
            serverDB.InsertShake(studentID, params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class myAsyncTaskShakeQiz extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            serverDB.InsertQiz(params[0], params[1]);
            return null;
        }
    }

    public void ShowAlertDialog(String input) {
        final AlertDialog.Builder dDialog = new AlertDialog.Builder(getActivity());
        dDialog.setMessage(input);
        dDialog.setPositiveButton("ปิด", null);
        dDialog.show();
    }
}
