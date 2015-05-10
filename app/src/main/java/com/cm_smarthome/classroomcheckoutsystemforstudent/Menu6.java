package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AdminPond on 5/5/2558.
 */
public class Menu6 extends Fragment {

    View rootView;

    ServerDB serverDB = new ServerDB();

    private LinearLayout layAns1, layAns2, layAns3, layAns4, layAns5;

    private RadioGroup ans1, ans2, ans3, ans4, ans5;

    private RadioButton ans1_1, ans1_2, ans1_3, ans1_4,
            ans2_1, ans2_2, ans2_3, ans2_4,
            ans3_1, ans3_2, ans3_3, ans3_4,
            ans4_1, ans4_2, ans4_3, ans4_4,
            ans5_1, ans5_2, ans5_3, ans5_4;

    private Button btnSubmit;
    private TextView tvSubjectID;
    private TextView tvSwipe;
    private int score;
    private String TAG = "Menu6";
    private String sID;
    private String sQ;
    private String sSCORE;

    private static final String NO_QIZ = "อาจารย์ยังไม่มีการเปิด Qiz...";
    private SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu6, container, false);

        layAns1 = (LinearLayout) rootView.findViewById(R.id.layAns1);
        layAns2 = (LinearLayout) rootView.findViewById(R.id.layAns2);
        layAns3 = (LinearLayout) rootView.findViewById(R.id.layAns3);
        layAns4 = (LinearLayout) rootView.findViewById(R.id.layAns4);
        layAns5 = (LinearLayout) rootView.findViewById(R.id.layAns5);

        tvSubjectID = (TextView) rootView.findViewById(R.id.tvSubjectID);
        tvSwipe = (TextView) rootView.findViewById(R.id.tvSwipe);

        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);

        ans1 = (RadioGroup) rootView.findViewById(R.id.ans1);
        ans2 = (RadioGroup) rootView.findViewById(R.id.ans2);
        ans3 = (RadioGroup) rootView.findViewById(R.id.ans3);
        ans4 = (RadioGroup) rootView.findViewById(R.id.ans4);
        ans5 = (RadioGroup) rootView.findViewById(R.id.ans5);

        ans1_1 = (RadioButton) rootView.findViewById(R.id.ans1_1);
        ans1_2 = (RadioButton) rootView.findViewById(R.id.ans1_2);
        ans1_3 = (RadioButton) rootView.findViewById(R.id.ans1_3);
        ans1_4 = (RadioButton) rootView.findViewById(R.id.ans1_4);

        ans2_1 = (RadioButton) rootView.findViewById(R.id.ans2_1);
        ans2_2 = (RadioButton) rootView.findViewById(R.id.ans2_2);
        ans2_3 = (RadioButton) rootView.findViewById(R.id.ans2_3);
        ans2_4 = (RadioButton) rootView.findViewById(R.id.ans2_4);

        ans3_1 = (RadioButton) rootView.findViewById(R.id.ans3_1);
        ans3_2 = (RadioButton) rootView.findViewById(R.id.ans3_2);
        ans3_3 = (RadioButton) rootView.findViewById(R.id.ans3_3);
        ans3_4 = (RadioButton) rootView.findViewById(R.id.ans3_4);

        ans4_1 = (RadioButton) rootView.findViewById(R.id.ans4_1);
        ans4_2 = (RadioButton) rootView.findViewById(R.id.ans4_2);
        ans4_3 = (RadioButton) rootView.findViewById(R.id.ans4_3);
        ans4_4 = (RadioButton) rootView.findViewById(R.id.ans4_4);

        ans5_1 = (RadioButton) rootView.findViewById(R.id.ans5_1);
        ans5_2 = (RadioButton) rootView.findViewById(R.id.ans5_2);
        ans5_3 = (RadioButton) rootView.findViewById(R.id.ans5_3);
        ans5_4 = (RadioButton) rootView.findViewById(R.id.ans5_4);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);

        swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeLayout.setRefreshing(false);
                        myAsyncTaskGetStatusQiz taskGetStatusQiz = new myAsyncTaskGetStatusQiz();
                        taskGetStatusQiz.execute();
                    }
                }, 3000);
            }
        });

        ans1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ans1_1) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans1_2) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans1_3) {
                    Log.e("QIZ", "YES : 1");
                    CountScore(1);
                } else if (checkedId == R.id.ans1_4) {
                    Log.e("QIZ", "NO");
                }
            }

        });

        ans2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ans2_1) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans2_2) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans2_3) {
                    Log.e("QIZ", "YES : 2");
                    CountScore(1);
                } else if (checkedId == R.id.ans2_4) {
                    Log.e("QIZ", "NO");
                }
            }

        });

        ans3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ans3_1) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans3_2) {
                    Log.e("QIZ", "YES : 3");
                    CountScore(1);
                } else if (checkedId == R.id.ans3_3) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans3_4) {
                    Log.e("QIZ", "NO");
                }
            }

        });

        ans4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ans4_1) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans4_2) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans4_3) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans4_4) {
                    Log.e("QIZ", "YES : 4");
                    CountScore(1);
                }
            }

        });

        ans5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ans5_1) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans5_2) {
                    Log.e("QIZ", "YES : 5");
                    CountScore(1);
                } else if (checkedId == R.id.ans5_3) {
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ans5_4) {
                    Log.e("QIZ", "NO");
                }
            }

        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogScore(getScoreString());
                score = 0;
            }
        });

        return rootView;
    }

    public void CountScore(int input) {
        score += input;
    }

    public int getScore() {
        return score;
    }

    public String getScoreString() {
        return String.valueOf(score);
    }

    public void DialogScore(final String input) {
        final AlertDialog.Builder dialogFacebook = new AlertDialog.Builder(getActivity());
        dialogFacebook.setCancelable(false);
        dialogFacebook.setMessage("คุณได้คะแนน : " + getScoreString());
        dialogFacebook.setNegativeButton("ทำใหม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setRadioButtonFalse();
                dialog.dismiss();
                Log.e("AAAA", sID + "" + getScore());
            }
        });
        dialogFacebook.setPositiveButton("แชร์", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {

                Intent intent = new Intent(getActivity(), FacebookSDK.class);
                intent.putExtra("SCORE", input);
                intent.putExtra("ID", sID);
                intent.putExtra("STATUS", "0");
                startActivity(intent);
            }
        });
        dialogFacebook.show();
    }

    private void setRadioButtonFalse() {
        ans1_1.setChecked(false);
        ans1_2.setChecked(false);
        ans1_3.setChecked(false);
        ans1_4.setChecked(false);

        ans2_1.setChecked(false);
        ans2_2.setChecked(false);
        ans2_3.setChecked(false);
        ans2_4.setChecked(false);

        ans3_1.setChecked(false);
        ans3_2.setChecked(false);
        ans3_3.setChecked(false);
        ans3_4.setChecked(false);

        ans4_1.setChecked(false);
        ans4_2.setChecked(false);
        ans4_3.setChecked(false);
        ans4_4.setChecked(false);

        ans5_1.setChecked(false);
        ans5_2.setChecked(false);
        ans5_3.setChecked(false);
        ans5_4.setChecked(false);
    }

    private class myAsyncTaskGetStatusQiz extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            serverDB.getStatusQiz();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sID = serverDB.SubjectID1;
            sQ = serverDB.StatusQiz1;

            if (sQ.equals("1")) {
                setRadioButtonFalse();
                tvSubjectID.setText("Qiz... SubjectID :" + sID);
                layAns1.setVisibility(View.VISIBLE);
                layAns2.setVisibility(View.VISIBLE);
                layAns3.setVisibility(View.VISIBLE);
                layAns4.setVisibility(View.VISIBLE);
                layAns5.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.VISIBLE);
                tvSwipe.setVisibility(View.GONE);
            } else {
                tvSubjectID.setText("");
                layAns1.setVisibility(View.GONE);
                layAns2.setVisibility(View.GONE);
                layAns3.setVisibility(View.GONE);
                layAns4.setVisibility(View.GONE);
                layAns5.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                tvSwipe.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), NO_QIZ, Toast.LENGTH_SHORT).show();
            }
            Log.i(TAG, sID + "//" + sQ);
        }
    }
}
