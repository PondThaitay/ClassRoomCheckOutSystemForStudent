package com.cm_smarthome.classroomcheckoutsystemforstudent;

import android.util.Base64;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by AdminPond on 5/5/2558.
 */
public class getDataWebService {

    private final String RESULT = "RESULT";
    private String encodUsername;
    private String encodPasswrod;
    protected String Result;

    protected String StudentCode;
    protected String FirstName_TH;
    protected String LastName_TH;
    protected String ProgramName_TH;
    protected String FacultyName_TH;
    protected String CitizenID;
    protected int STATUS;

    public void getResult() {

        final String NAMESPACE = "http://tempuri.org/";
        final String URL = "https://ws.up.ac.th/mobile/AuthenService.asmx?op=Login";
        final String SOAP_ACTION = "http://tempuri.org/Login";
        final String METHOD_NAME = "Login";

        //Initialize soap request + add parameters
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //Use this to add parameters
        request.addProperty("username", encodUsername);
        request.addProperty("password", encodPasswrod);
       /* request.addProperty("platformName", null);
        request.addProperty("deviceType", null);
        request.addProperty("ProductName", null);
        request.addProperty("ServiceUser", null);
        request.addProperty("ServicePass", null);*/

        //Declare the version of the SOAP request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            //this is the actual part that will call the webservice
            androidHttpTransport.call(SOAP_ACTION, envelope);

            // Get the SoapResult from the envelope body.
            SoapObject result = (SoapObject) envelope.bodyIn;

            if (result != null) {
                Result = result.getProperty(0).toString();

                if (Result.equalsIgnoreCase("anyType{}")) {
                    STATUS = 0;
                } else {
                    STATUS = 1;
                    Log.i(RESULT, Result);
                }
            } else {
                STATUS = 0;
                Log.e("ERROR", "getResult");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResultBySessionID(String sessionID) {

        final String NAMESPACE = "http://tempuri.org/";
        final String URL = "https://ws.up.ac.th/mobile/StudentService.asmx?op=GetStudentInfo";
        final String SOAP_ACTION = "http://tempuri.org/GetStudentInfo";
        final String METHOD_NAME = "GetStudentInfo";

        //Initialize soap request + add parameters
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        //Use this to add parameters
        request.addProperty("sessionID", sessionID);

        //Declare the version of the SOAP request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        try {
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            //this is the actual part that will call the webservice
            androidHttpTransport.call(SOAP_ACTION, envelope);

            // Get the SoapResult from the envelope body.
            SoapObject result = (SoapObject) envelope.bodyIn;

            if (result != null) {
                //Get the first property and change the label text
                SoapObject result3 = (SoapObject) result.getProperty(0);
                StudentCode = result3.getProperty("StudentCode").toString();
                FirstName_TH = result3.getProperty("FirstName_TH").toString();
                LastName_TH = result3.getProperty("LastName_TH").toString();
                ProgramName_TH = result3.getProperty("ProgramName_TH").toString();
                FacultyName_TH = result3.getProperty("FacultyName_TH").toString();
                CitizenID = result3.getProperty("CitizenID").toString();
                Log.e("StudentCode", StudentCode);
            } else {
                Log.e("ERROR", "getResultBySessionID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EncodeBaseX64(String username, String password) {
        encodUsername = Base64.encodeToString(username.getBytes(), Base64.DEFAULT);
        encodPasswrod = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
        Log.e("EncodeUsername", encodUsername);
        Log.e("EncodePassword", encodPasswrod);
    }
}
