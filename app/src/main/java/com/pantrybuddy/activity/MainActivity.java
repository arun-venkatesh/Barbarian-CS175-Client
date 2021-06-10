package com.pantrybuddy.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pantrybuddy.R;
import com.pantrybuddy.server.Server;
import com.pantrybuddy.stubs.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements IWebService {

    private EditText eEmail;
    private EditText ePassword;
    private TextView eattmptsrem;
    private Button eloginBut;
    private TextView eSignUp;
    private CheckBox eRemMe;
    private  TextView eForgotPwd;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPrefEditor;

    private boolean isValid = false;
    private int counter = 5;
    private String savedEmail;
    private String savedPwd;
    public static GlobalClass globalVariables;
    private WorkManager mWorkManager;
    private boolean prev_remember_me = false;

    @Override
    public void onBackPressed() {
        if(!sharedPreferences.getBoolean("RememberMeCheckBox", false)){
            sharedPrefEditor.putBoolean("userLoggedIn", false);
            sharedPrefEditor.apply();
        }
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         globalVariables = (GlobalClass) getApplicationContext();

         mWorkManager = WorkManager.getInstance(this);

        PeriodicWorkRequest callDataRequest = new PeriodicWorkRequest.Builder(com.pantrybuddy.Schedules.AppNotifier.class,
               16, TimeUnit.MINUTES, 16, TimeUnit.MINUTES)
                .addTag("TAG")
                .build();
        mWorkManager.enqueue(callDataRequest);
       // mWorkManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP , callDataRequest);
         sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
         sharedPrefEditor = sharedPreferences.edit();
        prev_remember_me = sharedPreferences.getBoolean("RememberMeCheckBox", false);
         if (sharedPreferences != null) {
             if (sharedPreferences.getBoolean("RememberMeCheckBox", false)) {
                 if (sharedPreferences.getBoolean("userLoggedIn", false)) {
                     MainActivity.globalVariables.setEmail(sharedPreferences.getString("LastSavedEmail", ""));
                     Server server = new Server(this);
                     server.fetchUserDetails();
                     Intent profileIntent = new Intent(this, ProfileActivity.class);
                     startActivity(profileIntent);
                     return;
                 }
             }
         setContentView(R.layout.activity_main);
         eEmail = findViewById(R.id.etEmail);
        ePassword = findViewById(R.id.etPassword);
        eattmptsrem = findViewById(R.id.tvattmptsrem);
        eloginBut = findViewById(R.id.elogin);
        eSignUp = findViewById(R.id.btnSignUp);
        eRemMe = findViewById(R.id.cbRememberMe);
        eForgotPwd=findViewById(R.id.tvForgotPwd);


            savedEmail = sharedPreferences.getString("LastSavedEmail", "");
            savedPwd = sharedPreferences.getString("LastSavedPassword", "");

            if (sharedPreferences.getBoolean("RememberMeCheckBox", false)) {
                eEmail.setText(savedEmail);
                ePassword.setText(savedPwd);
                eRemMe.setChecked(true);
            }
        }

        eloginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputEmail = eEmail.getText().toString();
                globalVariables.setEmail(inputEmail);
                String inputPassword = ePassword.getText().toString();
                savedEmail =inputEmail;
                savedPwd =inputPassword;
                if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(getBaseContext(), getString(R.string.msg_details_missing), Toast.LENGTH_SHORT).show();
                } else {
                    validate(inputEmail, inputPassword);
                }
            }
        });

        eSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });

        eRemMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefEditor.putBoolean("RememberMeCheckBox", eRemMe.isChecked());
                sharedPrefEditor.apply();
            }
        });

        eForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    private void validate(String ename, String pword) {
         MainActivity.globalVariables.setPasssword(pword);
        Server server = new Server(this);
        server.fetchUserDetails();
    }

    @Override
    public void processResponse(JSONObject responseObj) throws JSONException {
            if (responseObj != null) {
                String code = responseObj.get("Code").toString();
                String message = responseObj.get("Message").toString();
                if(code.equalsIgnoreCase("205")){
                    MainActivity.globalVariables.setFirstName(responseObj.getString("first_name"));
                    MainActivity.globalVariables.setLastName(responseObj.getString("last_name"));
                    MainActivity.globalVariables.setEmail(responseObj.getString("emailId"));
                    MainActivity.globalVariables.setNumber(responseObj.getString("phoneNumber"));
                    if(responseObj.has("allergy")) {
                        Log.d(TAG, "fetchUserDetails: " + responseObj.getString("allergy"));
                        MainActivity.globalVariables.setAllergy(responseObj.getString("allergy"));
                        Log.d(TAG, "fetchUserDetails: " + MainActivity.globalVariables.getAllergy());
                    }

                    MainActivity.globalVariables.setActive(responseObj.getString("isActive").equalsIgnoreCase("1"));
                    boolean loggedIn = sharedPreferences.getBoolean("userLoggedIn", false);
                    if(!loggedIn || !prev_remember_me) {
                        Server server = new Server(this);
                        server.loginEmailPwd(MainActivity.globalVariables.getEmail(), MainActivity.globalVariables.getPasssword());
                    }
                }
               if (code.equalsIgnoreCase("200")) {
                        sharedPrefEditor.putString("LastSavedEmail", savedEmail);
                        sharedPrefEditor.putString("LastSavedPassword", savedPwd);
                        sharedPrefEditor.apply();
                        globalVariables.setLoggedIn(true);
                        globalVariables.setEmail(savedEmail);

                        if(!MainActivity.globalVariables.isActive()){
                            Toast.makeText(MainActivity.this, "User is not verified yet, Please verify phone number using the 'forget password' option", Toast.LENGTH_LONG).show();
                            return;
                        }
                       sharedPrefEditor.putBoolean("userLoggedIn", true);
                       sharedPrefEditor.apply();
                        Log.d("de", "processResponse:  allergy " +  MainActivity.globalVariables.getAllergy());
                        if(MainActivity.globalVariables.getAllergy() != null) {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(MainActivity.this, AllergyDetailsActivity.class);
                            startActivity(intent);
                        }
                    } else if(code.equalsIgnoreCase("401") || code.equalsIgnoreCase("116")) {
                        counter--;
                        eattmptsrem.setText(getString(R.string.attmps_rem_1) + counter);
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                        sharedPrefEditor.putBoolean("userLoggedIn", false);
                        sharedPrefEditor.apply();
                   if (counter == 0) {
                            eloginBut.setEnabled(false);
                        }
                    }
                }
            }


}



