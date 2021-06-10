package com.pantrybuddy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pantrybuddy.Interfaces.ProductsListener;
import com.pantrybuddy.R;

import java.util.ArrayList;
import java.util.List;

public class FoodPreferencesActivity extends AppCompatActivity implements ProductsListener {

    private Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_preferences);

        RecyclerView productRecyclerView= findViewById(R.id.productsRecyclerView);
        next= findViewById(R.id.btnFoodPrefNext);

        List<Product> products= new ArrayList<>();

        Product almonds = new Product();
        almonds.image=R.drawable.almonds;
        almonds.name="Almonds";
//        almonds.rating=5f;
//        almonds.manufacturedBy="---";
//        almonds.ingredients="Almonds";
        products.add(almonds);



        Product chocolate = new Product();
        chocolate.image=R.drawable.chocolate;
        chocolate.name="Chocolate";
//        chocolate.rating=4f;
//        chocolate.manufacturedBy="---";
//        chocolate.ingredients="Cacao";
        products.add(chocolate);

        Product eggs = new Product();
        eggs.image=R.drawable.eggs;
        eggs.name="Eggs";
//        eggs.rating=4f;
//        eggs.manufacturedBy="---";
//        eggs.ingredients="Eggs";
        products.add(eggs);

        Product fish = new Product();
        fish.image=R.drawable.fish;
        fish.name="Fish";
//        fish.rating=2f;
//        fish.manufacturedBy="---";
//        fish.ingredients="Fish";
        products.add(fish);

        Product cowmilk = new Product();
        cowmilk.image=R.drawable.milk;
        cowmilk.name="Cow's Milk";
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
        products.add(walnuts);

        Product wheat = new Product();
        wheat.image=R.drawable.wheat;
        wheat.name="Wheat";
//        wheat.rating=2f;
//        wheat.manufacturedBy="---";
//        wheat.ingredients="Wheat";
        products.add(wheat);

        final ProductsAdaptor productsAdaptor= new ProductsAdaptor( products,this);
        productRecyclerView.setAdapter(productsAdaptor);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Product> selectedProducts = productsAdaptor.getSelectedProducts();
                StringBuilder productNames=new StringBuilder();

                for(int i=0;i<selectedProducts.size();i++){
                    if(i==0){
                        productNames.append(selectedProducts.get(i).name);
                    }else{
                        productNames.append("\n").append(selectedProducts.get(i).name);
                    }
                }
                //TODO : Save details to DB
                Toast.makeText(FoodPreferencesActivity.this,productNames.toString(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FoodPreferencesActivity.this, AllergyDetailsActivity.class));

            }
        });




    }

    @Override
    public void onProductsAction(Boolean isSelected) {

    }
}