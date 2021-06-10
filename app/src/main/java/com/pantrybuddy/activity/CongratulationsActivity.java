package com.pantrybuddy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pantrybuddy.R;
import com.pantrybuddy.server.Server;
import com.pantrybuddy.stubs.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

public class CongratulationsActivity extends AppCompatActivity implements IWebService {
    Button eVerifyOtp;
    private EditText eOtp;
    String genOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
        String emailId = MainActivity.globalVariables.getEmail();
        eVerifyOtp=findViewById(R.id.btnVerifyOtp);
        eOtp = findViewById(R.id.etOtp);
        getSupportActionBar().hide();

        callOtpGenerator(emailId);
        eVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=eOtp.getText().toString();
                if(otp.isEmpty()){
                    Toast.makeText(getBaseContext(), getString(R.string.msg_enter_otp_to_verify), Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyOtp(otp,emailId);
                }
            }
        });

    }

    private void callOtpGenerator(String emailFP) {
        Server server = new Server(this);
        JSONObject resp = new JSONObject();
        server.generateOtp(emailFP);
    }

    private void verifyOtp(String otp,String emailFP) {
        Server server = new Server(this);
        JSONObject resp = new JSONObject();
        server.verifyOtp(otp,emailFP);
    }

    @Override
    public void processResponse(JSONObject responseObj) throws JSONException {
        if (responseObj != null) {
            String code = responseObj.get("Code").toString();
            String message = responseObj.get("Message").toString();
            String type=responseObj.get("Type").toString();
            if (code != null && message != null) {
                if (code.equalsIgnoreCase("200")) {
                    if(type.equalsIgnoreCase("Generate")) {
                        genOtp = responseObj.get("otp").toString();
                        String phoneNum = responseObj.get("phone_number").toString();
                        Log.d("INFO", "OTP is "+genOtp);
                        sendOtpMessage(phoneNum, genOtp);
                        Toast.makeText(CongratulationsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else if (type.equalsIgnoreCase("Verify")){
                        Toast.makeText(CongratulationsActivity.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CongratulationsActivity.this, AllergyDetailsActivity.class));
                    }else{
                        Toast.makeText(CongratulationsActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CongratulationsActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public void sendOtpMessage(String phoneNum,String genOtp) {

        SmsManager smsManager = SmsManager.getDefault();
       

        final int PERMISSION_REQUEST_CODE = 1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }
        //smsManager.sendTextMessage("15555215554", null, "Welcome to Pantry Buddy. Your OTP is :"+genOtp, null, null);
        smsManager.sendTextMessage(phoneNum, null, getString(R.string.msg_text_otp)+genOtp, null, null);
    }

}