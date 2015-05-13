package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class Menu1 extends Fragment {

    View rootView;

    getDataWebService webService = new getDataWebService();
    ServerDB serverDB = new ServerDB();

    private static final String TAG = "WEBSERVICE";

    private String SESSION_ID;
    private TextView tvFrist;
    private TextView tvLast;
    private TextView tvID;
    private TextView tvF;
    private TextView tvS;
    private TextView tvSID;

    private ImageView imPF;
    private Button btnUpdate;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu1, container, false);

        tvFrist = (TextView) rootView.findViewById(R.id.tvFrist);
        tvLast = (TextView) rootView.findViewById(R.id.tvLastName);
        tvID = (TextView) rootView.findViewById(R.id.tvID);
        tvF = (TextView) rootView.findViewById(R.id.tvF);
        tvS = (TextView) rootView.findViewById(R.id.tvS);
        tvSID = (TextView) rootView.findViewById(R.id.tvSID);

        imPF = (ImageView) rootView.findViewById(R.id.imPF);
        btnUpdate = (Button) rootView.findViewById(R.id.btnUpdatePF);

        //imPF.setImageURI(/storage/emulated/0/DCIM/Facebook/FB_IMG_1430031529836.jpg);

        myAsyncTaskLoadPath loadPath = new myAsyncTaskLoadPath();
        loadPath.execute("1");

        SESSION_ID = getActivity().getIntent().getStringExtra("sessionID");

        myAsyncTask asyncTask = new myAsyncTask();
        asyncTask.execute(SESSION_ID);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

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

    private class myAsyncTaskImage extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            serverDB.UpdatePathImage(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i("Path", "Post");
        }

    }

    private class myAsyncTaskLoadPath extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            serverDB.LoadPathImage(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            String path = serverDB.PATH;
            if (path.equals("0")) {
                imPF.setVisibility(View.GONE);
            } else {
                Bitmap storage = BitmapFactory.decodeFile(path);
                imPF.setImageBitmap(storage);
            }
            Log.i("LoadPath", "onPostExecute");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);

                myAsyncTaskImage taskImage = new myAsyncTaskImage();
                taskImage.execute(selectedImagePath);

                System.out.println("Image Path : " + selectedImagePath);
                imPF.setVisibility(View.VISIBLE);
                imPF.setImageURI(selectedImageUri);
                //Log.e("IMG", )
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
