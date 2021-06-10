package com.pantrybuddy.stubs;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.pantrybuddy.R;
import com.pantrybuddy.activity.IWebService;
import com.pantrybuddy.activity.MainActivity;
import com.pantrybuddy.activity.ProfileActivity;
import com.pantrybuddy.server.Server;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class GlobalClass extends Application implements IWebService {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    private String email;
    private String FirstName;
    private String LastName;
    private String allergy;
    private boolean active;
    private boolean isLoggedIn;

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAllergy (String allergy){
        this.allergy = allergy;
    }

    public String getAllergy(){
        return allergy;
    }

    public Boolean isActive(){return active;}
    public void setActive(boolean active){
        this.active = active;
    }

    private String passsword;
    private String number;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setLoggedIn(boolean loggedIn){
        this.isLoggedIn = loggedIn;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    public Drawable getDrawable(String name) {
        int resourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return context.getResources().getDrawable(resourceId);
    }

    public void createNotificationChannel() {

        CharSequence name = "My notifier";
        String description = "testing notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("ID", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        PendingIntent pendingIntent = null;
        if(MainActivity.globalVariables.isLoggedIn) {
            Intent intent = new Intent(this, com.pantrybuddy.activity.ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        } else{
            Intent intent = new Intent(this, com.pantrybuddy.activity.MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "ID")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Food expiry alert!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Hi there. You seem to have food that have expired or about to expire in your inventory. Use your pantry buddy to track them."))
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager1 = NotificationManagerCompat.from(this);
        notificationManager1.notify(1, builder.build());
    }


    @Override
    public void processResponse(JSONObject responseObj) throws JSONException {
        if(responseObj !=null){
            if(responseObj.getString("Code") != null && responseObj.getString("Code").equalsIgnoreCase("200")){
                if(responseObj.has("count") && responseObj.getInt("count") > 0){
                    MainActivity.globalVariables.createNotificationChannel();
                }
            }
        }
    }

}
