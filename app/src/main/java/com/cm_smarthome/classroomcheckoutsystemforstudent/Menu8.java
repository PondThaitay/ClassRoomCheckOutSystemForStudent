package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by AdminPond on 5/5/2558.
 */
public class Menu8 extends Fragment {

    View rootView;

    ServerDB serverDB = new ServerDB();
    getDataWebService webService = new getDataWebService();

    private SwipeRefreshLayout swipeLayout;

    private TextView tvMenu8;
    private TextView tvRate;
    private ImageView imRate;
    private Button btnRate;
    private static final String TAG = "COUNT";
    private String SESSION_ID;
    private int COUNT;
    private int STATUS;
    private String im;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu8, container, false);

        SESSION_ID = getActivity().getIntent().getStringExtra("sessionID");

        tvMenu8 = (TextView) rootView.findViewById(R.id.tvMenu8);
        tvRate = (TextView) rootView.findViewById(R.id.tvRate);
        imRate = (ImageView) rootView.findViewById(R.id.imRate);
        btnRate = (Button) rootView.findViewById(R.id.btnRate);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        swipeLayout.setColorScheme(android.R.color.holo_orange_dark, android.R.color.holo_orange_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        myAsyncTaskCount taskCount = new myAsyncTaskCount();
                        taskCount.execute(SESSION_ID);
                    }
                }, 3000);
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("RATE", STATUS + "");
                Intent intent = new Intent(getActivity(), FacebookSDK.class);
                if (STATUS == 3) {
                    im = "3";
                } else if (STATUS == 2) {
                    im = "2";
                } else if (STATUS == 1) {
                    im = "1";
                } else if (STATUS == 0) {
                    im = "0";
                }
                intent.putExtra("STATUS", "1");
                intent.putExtra("IM", im);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private class myAsyncTaskCount extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webService.getResultBySessionID(params[0]);
            String studentID = webService.StudentCode;
            serverDB.getCountQR(studentID);
            serverDB.getCountShake(studentID);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            tvMenu8.setVisibility(View.GONE);
            tvRate.setVisibility(View.VISIBLE);
            imRate.setVisibility(View.VISIBLE);
            btnRate.setVisibility(View.VISIBLE);

            COUNT = serverDB.CountQR + serverDB.CountShake;
            if (COUNT >= 11) {
                imRate.setImageResource(R.drawable.best);
                tvRate.setText("คุณเข้าเรียนเป็นประจำ อยู่ในระดับดีมาก");
                STATUS = 3;
            } else if (COUNT >= 6) {
                imRate.setImageResource(R.drawable.better);
                tvRate.setText("คุณเข้าเรียนสม่ำเสอม อยู่ในระดับดี");
                STATUS = 2;
            } else {
                imRate.setImageResource(R.drawable.good);
                tvRate.setText("คุณเข้าเรียนตามปกติ อยู่ในระดับปานกลาง");
                STATUS = 1;
            }

            Log.e(TAG, (serverDB.CountQR + serverDB.CountShake) + "");
        }
    }
}
