package com.pantrybuddy.activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.pantrybuddy.Interfaces.ProductsListener;
import com.pantrybuddy.R;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdaptor extends RecyclerView.Adapter<ProductsAdaptor.ProductHolder>  {


    private List<Product> products;
    private ProductsListener productsListener;

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdaptor.ProductHolder holder, int position) {
        holder.bindProduct(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public List<Product> getSelectedProducts(){
        List<Product> selectedProducts= new ArrayList<>();
        for(Product product : products){
            if(product.isSelected){
                if(product.name == "Cow's milk"){
                    product.name = "Cow milk";
                }
                selectedProducts.add(product);
            }
        }
        return selectedProducts;
    }

    public ProductsAdaptor(List<Product> products, ProductsListener productsListener) {
        this.products = products;
        this.productsListener = productsListener;
    }


    class ProductHolder extends RecyclerView.ViewHolder{
        ConstraintLayout layoutProduct;
        View viewBackground;
        RoundedImageView imageProduct;
        TextView textName;
//        TextView      ,manufacturedBy,ingredients;
//        RatingBar ratingBar;

        ImageView imageSelected;


         ProductHolder(@NonNull View itemView) {
            super(itemView);
            layoutProduct=itemView.findViewById(R.id.layoutProducts);
            viewBackground=itemView.findViewById(R.id.viewBackground);
            imageProduct=itemView.findViewById(R.id.imageProducts);
            textName=itemView.findViewById(R.id.itemName);
             String name=textName.getText().toString();
//            manufacturedBy=itemView.findViewById(R.id.textManufacturedBy);
//            ingredients=itemView.findViewById(R.id.textIngredients);
//            ratingBar=itemView.findViewById(R.id.ratingBar);
            imageSelected=itemView.findViewById(R.id.imageSelected);
        }

        void bindProduct(final Product product){
            imageProduct.setImageResource(product.image);
            textName.setText(product.name);
            String name=textName.getText().toString();
            Log.d("DEBUG",name);
//            manufacturedBy.setText(product.manufacturedBy);
//            ingredients.setText(product.ingredients);
//            ratingBar.setRating(product.rating);
            if(product.isSelected){
                viewBackground.setBackgroundResource(R.drawable.products_selected_background);
                imageSelected.setVisibility(View.VISIBLE);
            }else{
                viewBackground.setBackgroundResource(R.drawable.products_background);
                imageSelected.setVisibility(View.GONE);
            }

            layoutProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(product.isSelected) {
                        viewBackground.setBackgroundResource(R.drawable.products_background);
                        imageSelected.setVisibility(View.GONE);
                        product.isSelected = false;

                        if (getSelectedProducts().size() == 0) {
                            productsListener.onProductsAction(false);
                        }

                    }else{
                        viewBackground.setBackgroundResource(R.drawable.products_selected_background);
                        imageSelected.setVisibility(View.VISIBLE);
                        product.isSelected=true;
                        productsListener.onProductsAction(true);

                    }
                }
            });
        }

    }
}
