package com.pantrybuddy.activity;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pantrybuddy.R;
import com.pantrybuddy.server.Server;
import com.pantrybuddy.stubs.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private ArrayList<UserProduct> mExampleList;
        private Context context;
    private final int ORANGE = 0xFFFF743E;



    public static class ProductViewHolder extends RecyclerView.ViewHolder implements IWebService {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public ImageView warning;
        public ImageView delete;
        public TextView mTextView8;
        public TextView mTextView9;
        public TextView mTextView10;
        public TextView mTextView11;
        public TextView mTextView12;
        public TextView mTextView13;
        public String Manufacturer;
        public String Ingredients;
        public String servingSize;
        public String title;
        View itemView;
        public Context context;


        public ImageView imageView;
        public View view;
        public com.google.android.material.card.MaterialCardView card;
        public TextView expiredLabel;
        public ConstraintLayout layout;


        public ProductViewHolder(Context context, View itemView) {

            super(itemView);
            this.itemView = itemView;
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            mTextView3 = itemView.findViewById(R.id.textView6);
            expiredLabel = itemView.findViewById(R.id.textView7);
            imageView = itemView.findViewById(R.id.imageView5);
            layout = itemView.findViewById(R.id.constraintLayout);

            mTextView8 = itemView.findViewById(R.id.textView8);
            mTextView9 = itemView.findViewById(R.id.textView9);
            mTextView10 = itemView.findViewById(R.id.textView10);
            mTextView11 = itemView.findViewById(R.id.textView11);
            mTextView12 = itemView.findViewById(R.id.textView12);
            mTextView13 = itemView.findViewById(R.id.textView13);
            warning = itemView.findViewById(R.id.imageView3);
            delete = itemView.findViewById(R.id.imageView8);
            this.context = context;

            card = itemView.findViewById(R.id.cardview);
            view = itemView.findViewById(R.id.view5);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Ingredients.equalsIgnoreCase("null") || Ingredients.trim().isEmpty()) {
                        if (Manufacturer.equalsIgnoreCase("null") || Manufacturer.trim().isEmpty()) {
                            if (servingSize.equalsIgnoreCase("null") || servingSize.trim().isEmpty()) {
                                return;
                            }
                        }
                    }
                    Intent intent = new Intent(context, PopUpActivity.class);
                    intent.putExtra("Manufacturer", Manufacturer);
                    intent.putExtra("Ingredients", Ingredients);
                    intent.putExtra("servingSize", servingSize);
                    intent.putExtra("title", title);

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void processResponse(JSONObject responseObj) throws JSONException {
            if (responseObj != null) {
                String code = responseObj.get("Code").toString();
                String message = responseObj.get("Message").toString();

            }
        }
    }

        public ProductAdapter(Context context, ArrayList<UserProduct> exampleList) {
            mExampleList = exampleList;
            this.context = context;
        }
        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_product, parent, false);
            ProductViewHolder evh = new ProductViewHolder(context, v);
            return evh;
        }


        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {

            UserProduct currentItem = mExampleList.get(position);
            holder.Manufacturer = currentItem.getManufacturer();
            holder.Ingredients = currentItem.getIngredients();
            holder.servingSize = currentItem.getServingSize();
            holder.title = currentItem.getProductName();
            holder.mTextView1.setText(String.valueOf(currentItem.getProductName()));
            holder.mTextView2.setText(String.valueOf(currentItem.getExpiryDate()));
            holder.mTextView3.setText("Count : " + String.valueOf(currentItem.getCount()));
            if(currentItem.isAllergic()) {
                holder.warning.setVisibility(View.VISIBLE);
                holder.warning.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "This product has contents that you may be allergic to. Please check the ingredients before consuming", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Server server = new Server(v.getContext());
                    server.deleteProduct(currentItem.getProductName(), currentItem.getExpiryDate());
                }
            });

            String name = String.valueOf(currentItem.getImage());
            holder.view.setBackground(MainActivity.globalVariables.getDrawable(name));
            Date date = getDate(String.valueOf(currentItem.getExpiryDate()));
            String currentDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Log.d("INFO", "onBindViewHolder: currentdate" + currentDateString);
            Date currentDate = getDate(currentDateString);


               if(currentDate.getYear() < date.getYear()){
                   return;
               }
            Log.d(TAG, "onBindViewHolder: curr : " + currentDate.getMonth() + ",exp: " +  date.getMonth());
               int difference = date.getDate() - currentDate.getDate();
            Log.d(TAG, "onBindViewHolder: diff is " + difference);
            holder.expiredLabel.setTextColor(Color.parseColor("green"));
            holder.expiredLabel.setText("");
               if(currentDate.getYear() > date.getYear() ){
                   holder.expiredLabel.setTextColor(Color.parseColor("red"));
                   holder.expiredLabel.setText("Expired");
               }else if(currentDate.getYear() == date.getYear()){
                   if(currentDate.getMonth() < date.getMonth()){
                       return;
                   }
                   if(currentDate.getMonth() > date.getMonth() || (currentDate.getMonth() ==  date.getMonth() && difference<=0)){
                       holder.expiredLabel.setTextColor(Color.parseColor("red"));
                       holder.expiredLabel.setText("Expired");
                   }else if(difference <= 3){
                       holder.expiredLabel.setTextColor(ORANGE);
                       holder.expiredLabel.setText("About to Expire");
                   }
               }

            Log.d("INFO", "onBindViewHolder: " + String.valueOf(currentItem.getExpiryDate()));
        }

        private Date getDate(String dateString) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                 date = format.parse(dateString);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }

        @Override
        public int getItemCount() {
            return mExampleList.size();
        }


    }

