package com.pantrybuddy.activity;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pantrybuddy.R;
import com.pantrybuddy.server.Server;
import com.pantrybuddy.stubs.GlobalClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements IWebService , NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ImageView imageAddItem;
    GlobalClass globalClass;

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
       // getSupportActionBar().setTitle("Your Pantry");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("debug", "onCreate: Fetching details of the user");
        String emailId = MainActivity.globalVariables.getEmail();
        String firstName = MainActivity.globalVariables.getFirstName();
        String lastName = MainActivity.globalVariables.getLastName();
        Server server = new Server(this);
        server.fetchUserProducts(emailId);
        imageAddItem=(ImageView)findViewById(R.id.imageView);
        imageAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AddItemActivity.class));
            }
        });




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(ProfileActivity.this);
        View header = navigationView.getHeaderView(0);
        //TODO: The full name will be set when the names are stored in global
        TextView nametext = (TextView) header.findViewById(R.id.userFullName);
        nametext.setText((firstName+" "+lastName));


        TextView emailtext = (TextView) header.findViewById(R.id.userEmail);
        emailtext.setText(emailId);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem addProduct = menu.findItem(R.id.addIcon);
        addProduct.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                  @Override
                                                  public boolean onMenuItemClick(MenuItem item) {
                                                      startActivity(new Intent(ProfileActivity.this, AddItemActivity.class));
                                                      return true;
                                                  }
                                              });

        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id=item.getItemId();
        switch (id){

            case R.id.nav_edit_profile:
                Intent h= new Intent(ProfileActivity.this,EditProfileActivity.class);
                startActivity(h);
                break;
            case R.id.nav_allergy:
                Intent i= new Intent(ProfileActivity.this,AllergyDetailsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_logout:
                Intent logout_intent= new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(logout_intent);
                SharedPreferences.Editor sharedPrefEditor = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE).edit();
                sharedPrefEditor.putBoolean("userLoggedIn", false);
                sharedPrefEditor.apply();
                break;
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void processResponse(JSONObject responseObj) throws JSONException {
        if (responseObj != null) {
            Log.d("response", "processResponse: " + responseObj);
            String code = responseObj.get("Code").toString();
            String message = responseObj.get("Message").toString();

            if (code != null && message != null) {

                if (code.equalsIgnoreCase("208")) {
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(this, ProfileActivity.class);
                    startActivity(in);
                    return;
                }

                JSONArray product_details = responseObj.getJSONArray("product_details");
                ImageView imageView = findViewById(R.id.imageView);
                TextView textView = findViewById(R.id.tvLoginMsg1);
                TextView textView1 = findViewById(R.id.tvPantryMsg1);


                if(product_details == null ||product_details.length() == 0){
                    textView.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }else{
                    textView.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    Log.d("response", "processResponse: " + responseObj);
                    ArrayList<UserProduct> exampleList = new ArrayList<>();

                    for(int i=0; i< product_details.length(); i++) {
                        JSONObject object =  product_details.getJSONObject(i);
                        boolean isAllergic = false;
                        if(object.has("is_allergic")){
                            isAllergic = true;
                        }
                        exampleList.add(new UserProduct(object.getString("product_name"),object.getString("manufacturer"),object.getString("expiry_date"), object.getInt("count"), object.getString("image"), object.getString("ingredients"), object.getString("serving_size"), isAllergic));
                    }
                    mRecyclerView = findViewById(R.id.recycleView);
                    StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

                    mAdapter = new ProductAdapter(this, exampleList);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                    boolean includeEdge = true;
                    mRecyclerView.addItemDecoration(new SpacesItemDecorator(1, 50, includeEdge));

                }

            }
        }
    }

}