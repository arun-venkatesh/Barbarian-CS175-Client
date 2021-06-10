package com.pantrybuddy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.text.TextRecognizer;
import com.pantrybuddy.R;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.CameraSource;
import com.pantrybuddy.server.Server;
import com.pantrybuddy.stubs.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity implements IWebService{

    private Button bScanBarcode;
    private TextView eItemName;

    private TextView eDate;
    private Button bDone;

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    private TextRecognizer textRecognizer;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String expiryDate;


    RequestQueue queue;
    private JsonObjectRequest jsonObjectRequest;
    Server server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        bScanBarcode=findViewById(R.id.btnScanBarCode);
        eItemName=findViewById(R.id.mltItemName);

        eDate=findViewById(R.id.etDate);
        bDone=findViewById(R.id.btnDone);
        surfaceView=findViewById(R.id.svBarcodeScan);
        queue = Volley.newRequestQueue(this);
        barcodeText = findViewById(R.id.mltItemName);
        server = new Server(this);


       eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal= Calendar.getInstance();
                int year= cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day= cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog= new DatePickerDialog(AddItemActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                 expiryDate=Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+Integer.toString(dayOfMonth);
                eDate.setText(expiryDate);
            }
        };

        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eItemName.getText() == null || eItemName.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "The product Name cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(eDate.getText() == null || eDate.getText().toString().trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please select the expiry date of the product", Toast.LENGTH_SHORT).show();
                    return;
                }
                String productId=barcodeData;
                String emailId = MainActivity.globalVariables.getEmail();
                Log.d("INFO", "Email of user :" + emailId);
                //Calling the product API to store the product details.
                if(productId!=null && !productId.isEmpty()) {
                    server.saveProduct(emailId, productId, expiryDate);
                }else{
                    String itemName=barcodeText.getText().toString();
                    server.saveProductManual(emailId, itemName, expiryDate);
                }

            }
        });

        bScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                surfaceView.setVisibility(View.VISIBLE);
                toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                initialiseDetectorsAndSources(queue);
            }
        });

    }

   private void initialiseDetectorsAndSources(RequestQueue queue) {
        Toast.makeText(getApplicationContext(), getString(R.string.msg_point_camera), Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(AddItemActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (cameraSource != null) {
                    cameraSource.release();
                    cameraSource = null;
                }
                ;
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("debug", "barcodeText: "+barcodeText.getText());
                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                eItemName.setText(barcodeText.getText().toString());
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                eItemName.setText(barcodeText.getText().toString());
                                getAPIResult(queue);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }
                        }
                    });
                }
            }
        });
    }

    private void getAPIResult(RequestQueue queue) {
        //String url = "https://api.upcdatabase.org/product/" + barcodeText.getText().toString() + "?apikey=5A7E28020FB2A4F78A8DE783FF2B3444\n";
        String url = "https://go.littlebunch.com/v1/food/" + barcodeText.getText().toString();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("INFO", "Webservice called :" + url );
                    JSONObject food_details = response.getJSONArray("items").getJSONObject(0);
                    String description = food_details.getString("foodDescription");
                    if(description!=null && !description.isEmpty())
                        barcodeText.setText(description);

                } catch (JSONException e) {
                    Log.d("ERROR", "ERROR in upc webservice " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "ERROR in upc webservice " + error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();
        initialiseDetectorsAndSources(queue);
    }

    @Override
    public void processResponse(JSONObject responseObj) throws JSONException {
        if (responseObj != null) {
            String code = responseObj.get("Code").toString();
            String message = responseObj.get("Message").toString();
            if (code.equalsIgnoreCase("200")) {
                startActivity(new Intent(AddItemActivity.this, ProfileActivity.class));
            }
        }
    }
}