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

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity implements IWebService {
    private EditText eFPEmail;
    private Button ebtnOtp;
    private EditText eOtp;
    private Button eVerfifyOtp;
    private  EditText eNewPwd;
    private Button ebtnSubmitFP;
    String genOtp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        eFPEmail=findViewById(R.id.etFPEmail);
        ebtnOtp=findViewById(R.id.btnOtp);
        eOtp=findViewById(R.id.etOtp);
        eVerfifyOtp=findViewById(R.id.btnVerifyOtp);
        eNewPwd=findViewById(R.id.etNewPwd);
        ebtnSubmitFP=findViewById(R.id.btnSubmitFP);
        getSupportActionBar().hide();


        ebtnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailFP=eFPEmail.getText().toString();
                if(emailFP.isEmpty()){
                    Toast.makeText(getBaseContext(), getString(R.string.msg_enter_email), Toast.LENGTH_SHORT).show();
                }
                else{
                    eOtp.setVisibility(View.VISIBLE);
                    eVerfifyOtp.setVisibility(View.VISIBLE);
                    callOtpGenerator(emailFP);
                }
            }
        });

        eVerfifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=eOtp.getText().toString();
                if(otp.isEmpty()){
                    Toast.makeText(getBaseContext(), getString(R.string.msg_enter_otp_to_verify), Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyOtp(otp,eFPEmail.getText().toString());
                }
            }
        });

        ebtnSubmitFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPwd=eNewPwd.getText().toString();
                resetPassword(eFPEmail.getText().toString(),newPwd);
            }
        });

    }

    private void resetPassword(String emailFP, String newPwd) {
        MainActivity.globalVariables.setEmail(emailFP);
        Server server = new Server(this);
        JSONObject resp = new JSONObject();
        server.resetPassword(emailFP,newPwd);
    }

    private void verifyOtp(String otp,String emailFP) {
        Server server = new Server(this);
        JSONObject resp = new JSONObject();
        server.verifyOtp(otp,emailFP);
    }

    private void callOtpGenerator(String emailFP) {
        Server server = new Server(this);
        JSONObject resp = new JSONObject();
        server.generateOtp(emailFP);
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
                        Log.d("INFO",genOtp);
                        String phoneNum = responseObj.get("phone_number").toString();
                        sendOtpMessage(phoneNum, genOtp);
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else if (type.equalsIgnoreCase("Verify")){
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        eNewPwd.setVisibility(View.VISIBLE);
                        ebtnSubmitFP.setVisibility(View.VISIBLE);
                    }else if(type.equalsIgnoreCase("Reset")) {
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        MainActivity.globalVariables.setLoggedIn(true);
                        startActivity(new Intent(ForgotPasswordActivity.this, ProfileActivity.class));
                    }else{
                        Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
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