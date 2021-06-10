package com.pantrybuddy.activity;

import org.json.JSONException;
import org.json.JSONObject;

public interface IWebService {

     void processResponse(JSONObject responseObj) throws JSONException;

}
