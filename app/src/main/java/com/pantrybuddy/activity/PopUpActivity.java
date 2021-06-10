package com.pantrybuddy.activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pantrybuddy.R;

public class PopUpActivity extends AppCompatActivity {
    private TextView Manufacturer;
    private TextView ingredients;
    private TextView servingSize;
    private TextView header;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);
        getSupportActionBar().hide();
        Manufacturer = (TextView) findViewById(R.id.textView9);
        ingredients = (TextView) findViewById(R.id.textView11);
        servingSize = (TextView) findViewById(R.id.textView13);
        header = (TextView) findViewById(R.id.textView14);
        button = (Button) findViewById(R.id.button2);



        DisplayMetrics dn = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dn);
        int width = dn.widthPixels;
        int height = dn.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.7));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
        Manufacturer.setText(getIntent().getStringExtra("Manufacturer").toLowerCase());
        ingredients.setText(getIntent().getStringExtra("Ingredients").toLowerCase());
        servingSize.setText(getIntent().getStringExtra("servingSize").toLowerCase());
        header.setText(getIntent().getStringExtra("title"));

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Enables Always-on
    }
}