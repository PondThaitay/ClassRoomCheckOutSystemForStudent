package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends ActionBarActivity {

    private static final String TAG = "WEBSERVICE";
    Context context = this;
    getDataWebService webService = new getDataWebService();
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    Toast.makeText(context, "กรุณากรอกข้อมูลด้วย ครับ", Toast.LENGTH_SHORT).show();
                } else {
                    myAsyncTask myAsyncTask = new myAsyncTask();
                    myAsyncTask.execute();
                }
            }
        });

    }

    private class myAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            webService.EncodeBaseX64(etUsername.getText().toString(), etPassword.getText().toString());
            webService.getResult();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            if (webService.STATUS == 0) {
                Toast.makeText(context, "ข้อมูลไม่ถูกต้อง ครับ", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("sessionID", webService.Result);
                startActivity(intent);
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", "กรุณารอสักครู่...");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }
    }
}
