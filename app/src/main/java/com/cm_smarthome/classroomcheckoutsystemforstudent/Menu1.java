package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by AdminPond on 5/5/2558.
 */
public class Menu1 extends Fragment {

    View rootView;

    private static final String TAG = "WEBSERVICE";
    getDataWebService webService = new getDataWebService();

    private String SESSION_ID;
    private TextView textView;
    private TextView tvFrist;
    private TextView tvLast;
    private TextView tvID;
    private TextView tvF;
    private TextView tvS;
    private TextView tvSID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu1, container, false);

        textView = (TextView) rootView.findViewById(R.id.tvMenu1);

        tvFrist = (TextView) rootView.findViewById(R.id.tvFrist);
        tvLast = (TextView) rootView.findViewById(R.id.tvLastName);
        tvID = (TextView) rootView.findViewById(R.id.tvID);
        tvF = (TextView) rootView.findViewById(R.id.tvF);
        tvS = (TextView) rootView.findViewById(R.id.tvS);
        tvSID = (TextView) rootView.findViewById(R.id.tvSID);

        SESSION_ID = getActivity().getIntent().getStringExtra("sessionID");

        myAsyncTask asyncTask = new myAsyncTask();
        asyncTask.execute(SESSION_ID);

        return rootView;
    }

    private class myAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            webService.getResultBySessionID(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            textView.setText("รหัสนิสิต : " + webService.StudentCode + "\n" + "ชื่อ : " + webService.FirstName_TH + "\n"
                    + "นามสุกล : " + webService.LastName_TH + "\n" + "สาขา : " + webService.ProgramName_TH + "\n"
                    + webService.FacultyName_TH);

            tvFrist.setText(webService.FirstName_TH);
            tvLast.setText(webService.LastName_TH);
            tvID.setText(webService.CitizenID);
            tvF.setText(webService.FacultyName_TH);
            tvS.setText(webService.ProgramName_TH);
            tvSID.setText(webService.StudentCode);
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
