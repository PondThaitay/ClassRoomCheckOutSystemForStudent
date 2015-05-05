package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AdminPond on 5/5/2558.
 */
public class Menu2 extends Fragment {

    ServerDB serverDB = new ServerDB();
    getDataWebService webService = new getDataWebService();

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private static final String TAG = "TEST";
    private String SESSION_ID;

    View rootView;

    private Button btnScanQR;
    private TextView tvResultQR;
    private String contents;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu2, container, false);

        SESSION_ID = getActivity().getIntent().getStringExtra("sessionID");

        btnScanQR = (Button) rootView.findViewById(R.id.btnQR);
        tvResultQR = (TextView) rootView.findViewById(R.id.tvResultQR);

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACTION_SCAN);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                tvResultQR.setText("รหัสวิชา : " + contents);
                myAsyncTask myAsyncTask = new myAsyncTask();
                myAsyncTask.execute(SESSION_ID);
            }
        }
    }

    private class myAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            webService.getResultBySessionID(params[0]);
            String studentID = webService.StudentCode;
            serverDB.Insert(studentID, contents);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Toast.makeText(getActivity(), "ทำการเช็คชื่อสำเร็จแล้ว", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }
    }
}
