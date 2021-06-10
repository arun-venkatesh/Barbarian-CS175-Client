package com.pantrybuddy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pantrybuddy.Interfaces.ProductsListener;
import com.pantrybuddy.R;
import com.pantrybuddy.server.Server;
import com.pantrybuddy.stubs.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllergyDetailsActivity extends AppCompatActivity implements ProductsListener, IWebService {

    private Button next;
    Server server;
    StringBuilder productNames=new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergy_details);
        getSupportActionBar().setTitle("Food Preference");
        getSupportActionBar().setLogo(R.drawable.logo);
        RecyclerView productRecyclerView= findViewById(R.id.productsRecyclerView2);
        next= findViewById(R.id.btnAllergNext);
        String allergy = MainActivity.globalVariables.getAllergy();
        String[] allergyArr = new String[0];
        if(allergy!=null &&allergy!="") {
            allergyArr= allergy.split(",");
        }
            List<String> allergyList = Arrays.asList(allergyArr);

        List<Product> products= new ArrayList<>();


        Product almonds = new Product();
        almonds.image=R.drawable.almonds;
        almonds.name="Almonds";
//        almonds.rating=5f;
//        almonds.manufacturedBy="---";
//        almonds.ingredients="Almonds";
        if(allergyList.contains(almonds.name)){
            almonds.isSelected = true;
        }
        products.add(almonds);



        Product chocolate = new Product();
        chocolate.image=R.drawable.chocolate;
        chocolate.name="Chocolate";
//        chocolate.rating=4f;
//        chocolate.manufacturedBy="---";
//        chocolate.ingredients="Cocoa";
        if(allergyList.contains(chocolate.name)){
            chocolate.isSelected = true;
        }
        products.add(chocolate);


        Product eggs = new Product();
        eggs.image=R.drawable.eggs;
        eggs.name="Eggs";
//        eggs.rating=4f;
//        eggs.manufacturedBy="---";
//        eggs.ingredients="Eggs";
        if(allergyList.contains(eggs.name)){
            eggs.isSelected = true;
        }
        products.add(eggs);

        Product fish = new Product();
        fish.image=R.drawable.fish;
        fish.name="Fish";
//        fish.rating=2f;
//        fish.manufacturedBy="---";
//        fish.ingredients="Fish";
        if(allergyList.contains(fish.name)){
            fish.isSelected = true;
        }
        products.add(fish);

        Product cowmilk = new Product();
        cowmilk.image=R.drawable.milk;
        cowmilk.name="Cow Milk";
        if(allergyList.contains(cowmilk.name)){
            cowmilk.isSelected = true;
        }
//        cowmilk.rating=5f;
//        cowmilk.manufacturedBy="---";
//        cowmilk.ingredients="Milk";
        products.add(cowmilk);

        Product walnuts = new Product();
        walnuts.image=R.drawable.walnuts;
        walnuts.name="Walnuts";
//        walnuts.rating=5f;
//        walnuts.manufacturedBy="---";
//        walnuts.ingredients="Walnuts";
        if(allergyList.contains(walnuts.name)){
            walnuts.isSelected = true;
        }
        products.add(walnuts);

        Product wheat = new Product();
        wheat.image=R.drawable.wheat;
        wheat.name="Wheat";
//        wheat.rating=2f;
//        wheat.manufacturedBy="---";
//        wheat.ingredients="Wheat";
        if(allergyList.contains(wheat.name)){
            wheat.isSelected = true;
        }
        products.add(wheat);

        final ProductsAdaptor productsAdaptor= new ProductsAdaptor( products,this);
        productRecyclerView.setAdapter(productsAdaptor);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> selectedProducts = productsAdaptor.getSelectedProducts();


                for(int i=0;i<selectedProducts.size();i++){
                    if(i==0){
                        productNames.append(selectedProducts.get(i).name);
                    }else{
                        productNames.append(",").append(selectedProducts.get(i).name);
                    }
                }


                String regEmail = MainActivity.globalVariables.getEmail();
                Log.d("debug", "onClick: email is " + regEmail);
                MainActivity.globalVariables.setAllergy(productNames.toString());
                try {
                    saveAllergyDetails(regEmail, productNames);
                    Toast.makeText(v.getContext(),"User Preference updated successfully", Toast.LENGTH_SHORT).show();

                }catch(Exception e){
                    Toast.makeText(v.getContext(),"Unable to add products to preference. Please contact support!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onProductsAction(Boolean isSelected) {

    }

    public void saveAllergyDetails(String emailId,StringBuilder productNames) throws Exception{
        if(!productNames.equals("")) {
            server = new Server(this);
                        server.saveAllergyDetails(emailId, productNames);
        }
    }

    public void processResponse(JSONObject responseObj) throws JSONException {
        if (responseObj != null) {
            String code = responseObj.get("Code").toString();
            String message = responseObj.get("Message").toString();

            if (code != null && message != null) {
                if (code.equalsIgnoreCase("200")) {
                    startActivity(new Intent(AllergyDetailsActivity.this, ProfileActivity.class));
                } else {
                    Toast.makeText(AllergyDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                }
        }
            }

    }

}