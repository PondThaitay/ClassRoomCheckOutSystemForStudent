package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AdminPond on 6/5/2558.
 */
public class ServerDB {

    protected String SubjectID;
    protected String STATUS_SHAKE;
    protected String Status;
    protected String StatusQiz;
    protected String PATH;
    protected String StatusQiz1;
    protected String SubjectID1;

    protected int CountQR;
    protected int CountShake;

    //Insert
    public void Insert(String StudentID, String SubjectID) {
        String url = "http://www.cm-smarthome.com/reg/qrcode.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sStudentID", StudentID));
        params.add(new BasicNameValuePair("sSubjectID", SubjectID));

        String resultServer = getHttpPost(url, params);

        String strStatusID = "0";
        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end Insert

    //Insert Shake
    public void InsertShake(String StudentID, String SubjectID) {
        String url = "http://www.cm-smarthome.com/reg/shake.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sStudentID", StudentID));
        params.add(new BasicNameValuePair("sSubjectID", SubjectID));

        String resultServer = getHttpPost(url, params);

        String strStatusID = "0";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end Insert

    //get Subject by Shake (lat and long)
    public void getData(String Lat, String Long) {
        String url = "http://www.cm-smarthome.com/reg/getData.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sLat", Lat));
        params.add(new BasicNameValuePair("sLong", Long));

        String resultServer = getHttpPost(url, params);

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            STATUS_SHAKE = c.getString("StatusID");
            SubjectID = c.getString("SubjectID");
            Status = c.getString("Status");
            StatusQiz = c.getString("Qiz");
            Log.e("Lat", SubjectID);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end getData

    //Insert
    public void InsertQiz(String SubjectID, String StatusQiz) {
        String url = "http://www.cm-smarthome.com/reg/insertQiz.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sSubjectID", SubjectID));
        params.add(new BasicNameValuePair("sStatus", StatusQiz));

        String resultServer = getHttpPost(url, params);

        String strStatusID = "0";
        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end Insert

    //get Status Qiz
    public void getStatusQiz() {
        String url = "http://www.cm-smarthome.com/reg/getStatusQiz.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sID", "1"));

        String resultServer = getHttpPost(url, params);

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            SubjectID1 = c.getString("SubjectID");
            StatusQiz1 = c.getString("Status");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end get Status Qiz

    //UpdatePathImage
    public void UpdatePathImage(String Path) {
        String url = "http://www.cm-smarthome.com/reg/imagePath.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sPath", Path));

        String resultServer = getHttpPost(url, params);

        String strStatusID = "0";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end UpdatePathImage

    //LoadPathImage
    public void LoadPathImage(String ID) {
        String url = "http://www.cm-smarthome.com/reg/loadPath.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sID", ID));

        String resultServer = getHttpPost(url, params);

        String strStatusID = "0";

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            strStatusID = c.getString("StatusID");
            PATH = c.getString("Path");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end LoadPathImage

    //get Count StudentID QR Code
    public void getCountQR(String StudentID) {
        String url = "http://www.cm-smarthome.com/reg/testCount.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sID", StudentID));

        String resultServer = getHttpPost(url, params);

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            CountQR = Integer.valueOf(c.getString("CountQR"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end Count StudentID QR Code

    //get Count StudentID Shake
    public void getCountShake(String StudentID) {
        String url = "http://www.cm-smarthome.com/reg/testCountShake.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sID", StudentID));

        String resultServer = getHttpPost(url, params);

        JSONObject c;
        try {
            c = new JSONObject(resultServer);
            CountShake = Integer.valueOf(c.getString("CountS"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //end Count StudentID Shake

    public String getHttpPost(String url, List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();

            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200)// Status OK
            {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;

                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
