package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MenuCheckIn extends ActionBarActivity {

    private String SESSION_ID;
    private String SU_ID;
    ServerDB db = new ServerDB();
    getDataWebService webService = new getDataWebService();

    Context context = this;

    private static final String TYPE[] = {"QR Code", "Shake", "Qiz"};

    private Button QRbtn;
    private TextView tvSIDMenuC, shakeTV;
    private ImageView shakeIM;

    private LinearLayout layQiz, layMenu;
    private RadioGroup rgQ1, rgQ2, rgQ3, rgQ4, rgQ5;
    private Button btnQiz;

    private String sSID, sQr, sSh, sBa, sCh, sQi;

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private String contents;

    //location
    GPSTracker gps;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private String latitude;
    private String longitude;
    private String SUBJECT_ID;
    private int COUNT_SHAKE = 0;
    private int COUNT_QR = 0;
    private int COUNT_QIZ = 0;

    private int r1, r2, r3, r4, r5;

    DecimalFormat df = new DecimalFormat("####0.0000");
    private int STATUS_SHAKE;
    private Calendar cal;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_check_in);

        SESSION_ID = getIntent().getStringExtra("SSID");
        SU_ID = getIntent().getStringExtra("SUJECT_ID");

        QRbtn = (Button) findViewById(R.id.QRbtn);
        tvSIDMenuC = (TextView) findViewById(R.id.tvSIDMenuC);
        shakeTV = (TextView) findViewById(R.id.shakeTV);
        shakeIM = (ImageView) findViewById(R.id.shakeIM);
        layQiz = (LinearLayout) findViewById(R.id.layQiz);
        layMenu = (LinearLayout) findViewById(R.id.layMenu);

        rgQ1 = (RadioGroup) findViewById(R.id.fgQ1);
        rgQ2 = (RadioGroup) findViewById(R.id.fgQ2);
        rgQ3 = (RadioGroup) findViewById(R.id.fgQ3);
        rgQ4 = (RadioGroup) findViewById(R.id.fgQ4);
        rgQ5 = (RadioGroup) findViewById(R.id.fgQ5);

        btnQiz = (Button) findViewById(R.id.btnQiz);

        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = sdf.format(cal.getTime());

        myAsyncTaskGetStatusCheck taskGetStatusCheck = new myAsyncTaskGetStatusCheck();
        taskGetStatusCheck.execute(strDate, SU_ID);

        QRbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT_QR == 1) {
                    ShowAlertDialog("คุณได้ทำการเช็คชื่อด้วยการแสกน QR Code เรียบร้อยแล้ว");
                } else {
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                    startActivityForResult(intent, 0);
                    COUNT_QR = 1;
                }
            }
        });

        gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            latitude = String.valueOf(df.format(gps.getLatitude()));
            longitude = String.valueOf(df.format(gps.getLongitude()));
        } else {
            gps.showSettingsAlert();
        }

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                if (STATUS_SHAKE == 1) {
                    handleShakeEvent(count);
                }
            }
        });

        rgQ1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ansQ1_1) {
                    Log.e("QIZ", "NO");
                    r1 = 0;
                } else if (checkedId == R.id.ansQ1_2) {
                    Log.e("QIZ", "NO");
                    r1 = 0;
                } else if (checkedId == R.id.ansQ1_3) {
                    Log.e("QIZ", "YES : 1");
                    r1 = 1;
                    //CountScore(1);
                } else if (checkedId == R.id.ansQ1_4) {
                    Log.e("QIZ", "NO");
                    r1 = 0;
                }
            }

        });

        rgQ2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ansQ2_1) {
                    r2 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ2_2) {
                    r2 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ2_3) {
                    r2 = 1;
                    Log.e("QIZ", "YES : 2");
                    //CountScore(1);
                } else if (checkedId == R.id.ansQ2_4) {
                    r2 = 0;
                    Log.e("QIZ", "NO");
                }
            }

        });

        rgQ3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ansQ3_1) {
                    r3 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ3_2) {
                    Log.e("QIZ", "YES : 3");
                    r3 = 1;
                    //CountScore(1);
                } else if (checkedId == R.id.ansQ3_3) {
                    r3 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ3_4) {
                    r3 = 0;
                    Log.e("QIZ", "NO");
                }
            }

        });

        rgQ4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ansQ4_1) {
                    r4 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ4_2) {
                    r4 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ4_3) {
                    r4 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ4_4) {
                    r4 = 1;
                    Log.e("QIZ", "YES : 4");
                    //CountScore(1);
                }
            }

        });

        rgQ5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ansQ5_1) {
                    r5 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ5_2) {
                    Log.e("QIZ", "YES : 5");
                    r5 = 1;
                    //CountScore(1);
                } else if (checkedId == R.id.ansQ5_3) {
                    r5 = 0;
                    Log.e("QIZ", "NO");
                } else if (checkedId == R.id.ansQ5_4) {
                    r5 = 0;
                    Log.e("QIZ", "NO");
                }
            }

        });

        btnQiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (COUNT_QIZ == 1) {
                    ShowAlertDialog("คุณได้ทำการเช็คชื่อด้วยการทำ Qiz เรียบร้อยแล้ว");
                    layQiz.setVisibility(View.GONE);
                } else {
                    String result = String.valueOf(r1 + r2 + r3 + r4 + r5);
                    DialogScore(result);
                    myAsyncTaskQiz taskQiz = new myAsyncTaskQiz();
                    taskQiz.execute(SESSION_ID);
                    COUNT_QIZ = 1;
                }
            }
        });
    }

    private void handleShakeEvent(int count) {
        myAsyncTaskShake myAsyncTask = new myAsyncTaskShake();
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

    private class myAsyncTaskGetStatusCheck extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            db.getStatusCheck(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            sSID = db.sSID;
            if (sSID.equals("null")) {
                ShowAlertDialog("รหัสวิชา : " +SU_ID+ " นี้ปัจจุบันยังไม่มีการเปิดให้นิสิตทำ การเช็คชื่อ!");
            } else {
                sQr = db.sQr;
                sSh = db.sSh;
                sBa = db.sBa;
                sCh = db.sCh;
                sQi = db.sQi;

                tvSIDMenuC.setText("Subject ID : " + sSID);

                if (sQr.equals("1")) {
                    ShowAlertDialog("กรุณาคลิกปุ่ม QR Code เพื่อทำการเช็คชื่อ");
                    layMenu.setVisibility(View.VISIBLE);
                    QRbtn.setVisibility(View.VISIBLE);
                } else if (sSh.equals("1")) {
                    ShowAlertDialog("กรุณา Shake เพื่อทำการเช็คชื่อ");
                    layMenu.setVisibility(View.VISIBLE);
                    shakeTV.setVisibility(View.VISIBLE);
                    shakeIM.setVisibility(View.VISIBLE);
                    STATUS_SHAKE = 1;
                } else if (sQi.equals("1")) {
                    ShowAlertDialog("กรุณาทำ Qiz เพื่อทำการเช็คชื่อ");
                    layQiz.setVisibility(View.VISIBLE);
                } else if (sBa.equals("1")) {
                    ShowAlertDialog("กรุณานำบัตรนิสิต ไปแสกน Barcode ที่อาจารย์รายวิชา");
                } else if (sCh.equals("1")) {
                    ShowAlertDialog("อารจารย์ได้ทำการเช็คชื่อให้คุณแล้ว");
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == this.RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                myAsyncTaskQRCode myAsyncTaskQRCode = new myAsyncTaskQRCode();
                myAsyncTaskQRCode.execute(SESSION_ID);
            }
        }
    }

    private class myAsyncTaskQRCode extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webService.getResultBySessionID(params[0]);
            String studentID = webService.StudentCode;
            db.InsertCheckName(studentID, contents, TYPE[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ShowAlertDialog("คุณได้เช็คชื่อด้วย QR Code ในรายวิชา : " + contents + " สำเร็จแล้ว");
        }
    }

    private class myAsyncTaskQiz extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webService.getResultBySessionID(params[0]);
            String studentID = webService.StudentCode;
            db.InsertCheckName(studentID, sSID, TYPE[2]);
            return null;
        }
    }

    private class myAsyncTaskShake extends AsyncTask<String, Void, Void> {

        private final ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Checking...");
            this.dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                Thread.sleep(2000);
                db.getData(latitude, longitude);
                SUBJECT_ID = db.SubjectID;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            this.dialog.dismiss();

            if (db.STATUS_SHAKE.equals("1")) {
                ShowAlertDialog("You are not in the area!");
            } else {
                if (db.Status.equals("0")) {
                    ShowAlertDialog("Time Out Check in!");
                } else {
                    if (COUNT_SHAKE == 1) {
                        ShowAlertDialog("คุณได้ทำการเช็คชื่อด้วยการแสกน Shake เรียบร้อยแล้ว");
                    } else {
                        Log.e("SubjectID", SUBJECT_ID);
                        Log.e("Lat Long", latitude + "/" + longitude);
                        myAsyncTaskShakeInsert myAsyncTaskShakeInsert = new myAsyncTaskShakeInsert();
                        myAsyncTaskShakeInsert.execute(SESSION_ID, SUBJECT_ID);
                        COUNT_SHAKE = 1;
                        ShowAlertDialog("Checked in SubjectID : " + SUBJECT_ID);
                    }
                }
            }
        }
    }

    private class myAsyncTaskShakeInsert extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            webService.getResultBySessionID(params[0]);
            String studentID = webService.StudentCode;
            db.InsertCheckName(studentID, params[1], TYPE[1]);
            return null;
        }
    }

    public void ShowAlertDialog(String input) {
        final AlertDialog.Builder dDialog = new AlertDialog.Builder(context);
        dDialog.setMessage(input);
        dDialog.setPositiveButton("ปิด", null);
        dDialog.show();
    }

    public void DialogScore(final String input) {
        final AlertDialog.Builder dialogFacebook = new AlertDialog.Builder(context);
        dialogFacebook.setCancelable(false);
        dialogFacebook.setMessage("คุณได้ทำการเช็คชื่อด้วยการทำ Qiz สำเร็จแล้ว คุณได้คะแนน : " + input);

        dialogFacebook.setNegativeButton("ไม่แชร์", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogFacebook.setPositiveButton("แชร์", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                Intent intent = new Intent(context, FacebookSDK.class);
                intent.putExtra("SCORE", input);
                intent.putExtra("ID", sSID);
                intent.putExtra("STATUS", "0");
                startActivity(intent);
            }
        });
        dialogFacebook.show();
    }
}
