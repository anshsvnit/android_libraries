package com.example.anshul.opinion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.BasicPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText userName;
    EditText password;

    String strUserName;
    String strPassword;
    String errorStr;
    Button submit;
    NetworkInfo[] info;
    private MyApplication mInstance;

    String url;

    //EditText item_et;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setHasOptionsMenu(true);
        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        url = "http://192.168.42.107/take_order.php";

        initialize();

        ConnectivityManager check = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        info = check.getAllNetworkInfo();

        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // getText();

        for (int i = 0; i<info.length; i++){
            if (info[i].getState() == NetworkInfo.State.CONNECTED){
                Toast.makeText(getBaseContext(), "Internet is connected",
                        Toast.LENGTH_SHORT).show();
            }
        }}
        });
*/
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getText();
                PD.show();
                Log.v("value of question", strUserName);
                Log.v("value of password", strPassword);

                StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {


                            @Override
                            public void onResponse(String response) {
                                PD.dismiss();
                                /*try {
                                    errorStr = response.
                                }catch(JSONException e) {
                                    e.printStackTrace();
                                }
                                userName.setText("");
                                password.setText("");
                                // CharSequence charerror = error;
                                Toast.makeText(getApplicationContext(), "Successful",
                                        Toast.LENGTH_SHORT).show();
*/
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        PD.dismiss();
                        Log.e("TAG Error", error.toString());
                        Toast.makeText(getApplicationContext(),
                                "failed to insert", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        Log.v("Reached", "inside get params");
                        params.put("username", strUserName);
                        params.put("password", strPassword);
                        Log.v("Tag", "json: " + params.toString());

                        return params;
                    }
                    /*@Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

                        if (response.data == null || response.data.length == 0) {
                            return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
                        } else {
                            return super.parseNetworkResponse(response);
                        }
                    }*/
                    //private Response.Listener mListener ;

                   /* @Override
                    protected void deliverResponse(Integer statusCode) {
                        mListener.onResponse();
                    }*/

                };

                // Adding request to request queue
                Log.v("Reached", "REached");
                //Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
                MyApplication.getInstance().addToReqQueue(jsonObjectRequest);
            }});


}




    public void initialize(){
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);

        submit = (Button) findViewById(R.id.submit);
    }

    public void getText(){
        strUserName = userName.getText().toString().trim();
        strPassword = password.getText().toString().trim();
    }

}
