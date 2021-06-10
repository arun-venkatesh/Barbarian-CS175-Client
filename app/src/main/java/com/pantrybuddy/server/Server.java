package com.pantrybuddy.server;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pantrybuddy.activity.IWebService;
import com.pantrybuddy.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

public class Server {

    //private static final String SERVER_NAME = "128.195.27.49";
    //private static final String SERVER_NAME = "192.168.1.151";
    private static final String SERVER_NAME = "192.168.0.133";
    private static final String SERVER_PORT = "8080";
    public static final String URL_SIGNUP = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/user/create?";
    public static final String URL_LOGIN = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/user/login?";
    public static final String URL_GEN_OTP = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/user/otp?";
    public static final String URL_VER_OTP = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/user/otp?";
    public static final String URL_RES_PWD = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/user/password?";
    public static final String URL_ALLERG = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/user/allergy?";
    private static final String URL_EDIT_PROFILE = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/user/edit?";
    public static final String URL_SAVE_PROD = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/product?";
    public static final String URL_SAVE_PROD_MANUAL = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/productManual?";
    public static final String URL_FETCH_USER_PRODUCTS = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/product?";
    public static final String URL_FETCH_USER_DETAILS = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/user/fetch?";
    public static final String URL_FETCH_EXPIRED_PRODUCTS = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/expired/products/fetch?";
    public static final String URL_DELETE_PRODUCT = "http://" + SERVER_NAME + ":" + SERVER_PORT + "/api/product/delete?";

    private static final String ERROR_TAG = "Web Service Error";
    private static final String INFO_TAG = "Web Service INFO";

    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;

    Context context;

    public Server(Context context) {
        this.context = context;
    }

    //http://3.89.143.237:8080/api/user/create?firstName=Varshini&lastName=Bhaskaran&emailId=bhaskarv@uci.edu&phoneNumber=+919710439153&password=varshini
    public void signUp(String email, String mobile, String firstName, String lastName, String password) {

        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_SIGNUP + "firstName=" + firstName + "&lastName=" + lastName + "&emailId=" + email + "&phoneNumber=" + mobile + "&password=" + password;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " firstname:" + firstName + " lastName:" + lastName + " email:" + email + " phoneNumber:" + mobile + " password:" + password);

                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot sign up user.Error: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot sign up user.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void editProfile(String email, String mobile, String firstName, String lastName, String password) {

        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_EDIT_PROFILE + "firstName=" + firstName + "&lastName=" + lastName + "&emailId=" + email + "&phoneNumber=" + mobile + "&password=" + password;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    response.put("Type", "SignUp");
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " firstname:" + firstName + " lastName:" + lastName + " email:" + email + " phoneNumber:" + mobile + " password:" + password);

                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot edit up user.Error: " + e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot sign up user.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void loginEmailPwd(String email, String password) {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_LOGIN + "emailId=" + email + "&password=" + password;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + email + " password:" + password);
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot log in user.Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot log in user.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void generateOtp(String emailFP) {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_GEN_OTP + "&emailId=" + emailFP;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    response.put("Type", "Generate");
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + emailFP);
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot gen OTP.Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot gen OTP.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void verifyOtp(String otp, String emailFP) {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_VER_OTP + "&otp=" + otp + "&emailId=" + emailFP;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    response.put("Type", "Verify");
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + emailFP);
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot verify OTP.Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot verify OTP.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void resetPassword(String emailFP, String newPwd) {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_RES_PWD + "&emailId=" + emailFP + "&password=" + newPwd;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    response.put("Type", "Reset");
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + emailFP);
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot reset password.Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot reset password.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void saveProduct(String emailId, String productId, String expDate) {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_SAVE_PROD + "&emailId=" + emailId + "&productId=" + productId + "&expiryDate=" + expDate;
        Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + emailId);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + emailId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot save product.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void saveProductManual(String emailId, String itemName, String expDate) {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_SAVE_PROD_MANUAL + "&emailId=" + emailId + "&itemName=" + itemName + "&expiryDate=" + expDate;
        Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + emailId);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + emailId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot save product.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void fetchUserProducts(String email) {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_FETCH_USER_PRODUCTS + "emailId=" + email;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + email);
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot fetch user products.Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot sign up user.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }


    public void saveAllergyDetails(String emailId, StringBuilder productNames) throws Exception {
        requestQueue = Volley.newRequestQueue(context);
        String query = URLEncoder.encode(productNames.toString(), "utf-8");
        String FinalURL = URL_ALLERG + "&emailId=" + emailId + "&commaSeparatedAllergy=" + query;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + emailId);
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot save allergy details.Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot save allergy details.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }


    public void fetchUserDetails() {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_FETCH_USER_DETAILS + "&emailId=" + MainActivity.globalVariables.getEmail();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + MainActivity.globalVariables.getEmail());
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot fetch user details. Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot fetch user details. Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void fetchExpiredProducts() {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_FETCH_EXPIRED_PRODUCTS + "&emailId=" + MainActivity.globalVariables.getEmail();
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + MainActivity.globalVariables.getEmail());
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot fetch expired products.Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot fetch expired products.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void deleteProduct(String productName, String expiryDate) {
        requestQueue = Volley.newRequestQueue(context);
        String FinalURL = URL_DELETE_PRODUCT + "&emailId=" + MainActivity.globalVariables.getEmail() + "&productName=" + productName + "&expiryDate=" + expiryDate;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FinalURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((IWebService) context).processResponse(response);
                    Log.d(INFO_TAG, "Webservice called :" + FinalURL + " : with :" + " email:" + MainActivity.globalVariables.getEmail());
                } catch (JSONException e) {
                    Log.d(ERROR_TAG, "Cannot fetch expired products.Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ERROR_TAG, "Cannot fetch expired products.Error: " + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
}