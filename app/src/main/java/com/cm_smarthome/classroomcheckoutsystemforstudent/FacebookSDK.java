package com.cm_smarthome.classroomcheckoutsystemforstudent;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

/**
 * Created by AdminPond on 8/5/2558.
 */
public class FacebookSDK extends FragmentActivity {

    Context context = this;

    private LoginButton loginBtn;
    private Button postImageBtn;
    private Button updateStatusBtn;

    private TextView userName;

    private UiLifecycleHelper uiHelper;

    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    private static String messageID = "คุณได้คะแนนจากการ Qiz ในรายวิชา : ";
    private static String messageSCORE = " คะแนน";
    private String SCORE;
    private String ID;
    private String message;
    private String STATUS;
    private Bitmap img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.facebooksdk);

        userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    userName.setText("สวัสดี คุณ , " + user.getName());
                } else {
                    userName.setText("You are not logged");
                }
            }
        });

        updateStatusBtn = (Button) findViewById(R.id.update_status);
        postImageBtn = (Button) findViewById(R.id.post_image);

        STATUS = getIntent().getStringExtra("STATUS");

        if (STATUS.equals("1")) {
            updateStatusBtn.setVisibility(View.GONE);
        } else {
            postImageBtn.setVisibility(View.GONE);
        }

        postImageBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                postImage();
            }
        });

        updateStatusBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                postStatusMessage();
            }
        });

        buttonsEnabled(false);
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                buttonsEnabled(true);
                Log.d("FacebookSampleActivity", "Facebook session opened");
            } else if (state.isClosed()) {
                buttonsEnabled(false);
                Log.d("FacebookSampleActivity", "Facebook session closed");
            }
        }
    };

    public void buttonsEnabled(boolean isEnabled) {
        postImageBtn.setEnabled(isEnabled);
        updateStatusBtn.setEnabled(isEnabled);
    }

    public void postImage() {
        if (checkPermissions()) {

            String im = getIntent().getStringExtra("IM");

            if (im.equals("3")) {
                img = BitmapFactory.decodeResource(getResources(),
                        R.drawable.better);
            } else if (im.equals("2")) {
                img = BitmapFactory.decodeResource(getResources(),
                        R.drawable.best);
            } else if (im.equals("1")) {
                img = BitmapFactory.decodeResource(getResources(),
                        R.drawable.good);
            } else if (im.equals("0")) {
                img = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher);
            }

            Request uploadRequest = Request.newUploadPhotoRequest(
                    Session.getActiveSession(), img, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(context,
                                    "Share ระดับการเข้าเรียนเรียบร้อยแล้ว ครับ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            uploadRequest.executeAsync();
        } else {
            requestPermissions();
        }
    }

    public void postStatusMessage() {
        if (checkPermissions()) {

            SCORE = getIntent().getStringExtra("SCORE");
            ID = getIntent().getStringExtra("ID");

            message = messageID + ID + " ได้ : " + " " + SCORE + " " + messageSCORE;

            Request request = Request.newStatusUpdateRequest(
                    Session.getActiveSession(), message,
                    new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            if (response.getError() == null)
                                Toast.makeText(context,
                                        "Share คะแนนการ Qiz เรียบร้อยแล้ว ครับ",
                                        Toast.LENGTH_LONG).show();
                        }
                    });
            request.executeAsync();
        } else {
            requestPermissions();
        }
    }

    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
        buttonsEnabled(Session.getActiveSession().isOpened());
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

}
