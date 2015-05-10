package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AdminPond on 5/5/2558.
 */
public class Menu4 extends Fragment {

    View rootView;
    private String jsonResult;

    private ListView listView;

    private String SSID;
    private String AC;
    private String SE = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu4, container, false);

        SSID = getActivity().getIntent().getStringExtra("sessionID");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        year += 543;
        AC = String.valueOf(year);

        listView = (ListView) rootView.findViewById(R.id.listView1);

        JsonReadTask task = new JsonReadTask();
        task.execute(SSID, AC, SE);

        return rootView;
    }

    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String url = "http://www.cm-smarthome.com/reg/pond.php";

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("sessionid", params[0]));
            nameValuePairs.add(new BasicNameValuePair("acadyear", params[1]));
            nameValuePairs.add(new BasicNameValuePair("semester", params[2]));

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            ListDrwaer();
        }
    }// end async task

    // build hash set for list view
    public void ListDrwaer() {
        List<Map<String, String>> employeeList = new ArrayList<Map<String, String>>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("emp_info");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("DayOfWeek");
                String number = jsonChildNode.optString("SubjectCode");
                String x = jsonChildNode.optString("SubjectName");
                String x1 = jsonChildNode.optString("StartTime");
                String x2 = jsonChildNode.optString("EndTime");
                String x3 = jsonChildNode.optString("RoomNo");
                String x4 = jsonChildNode.optString("TeacherName");

                String outPut = "วัน : " + name + "\n" + "รหัสวิชา : " + number + "\n" +
                        "ชื่อวิชา : " + x + "\n" + "เวลา : " + x1 + " - " + x2 + " น." + "\n" +
                        "ห้อง : " + x3 + "\n" + "อาจารย์ผู้สอน : " + x4.replaceAll(",", "\n");

                employeeList.add(createEmployee("employees", outPut));

            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), employeeList,
                android.R.layout.simple_list_item_1,
                new String[]{"employees"}, new int[]{android.R.id.text1});
        listView.setAdapter(simpleAdapter);
    }

    private HashMap<String, String> createEmployee(String name, String number) {
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

}
