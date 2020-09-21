package com.iot.iotproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;

public class ElementListActivity extends AppCompatActivity {

    private LinkedList<String> listRes;
    //private String elementName;
    private String tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        listRes = new LinkedList<>();


        String url= getIntent().getStringExtra("url");

        tab = getIntent().getStringExtra("tab");


        rest(url, this);

    }

    public void rest(final String url, final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("lunghezza", ""+response.length());
                        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);

                        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        //        LinearLayout.LayoutParams.MATCH_PARENT,
                        //        LinearLayout.LayoutParams.WRAP_CONTENT);

                        for(int i = 0; i<response.length() ; i++) {
                            try {
                                Log.d("responsella", response.getString(i));
                                listRes.add(response.getString(i));
                                Log.d("mamma", listRes.get(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        for (int j = 0; j < listRes.size(); j++) {

                            Button btn = new Button(context);
                            btn.setId(j);
                            final String elementName = listRes.get(j);
                            btn.setText(elementName);
                            btn.setBackgroundColor(Color.rgb(255, 255, 255));
                            buttonContainer.addView(btn);
                            btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    Toast.makeText(view.getContext(),
                                            "Button clicked index = " + elementName, Toast.LENGTH_SHORT)
                                            .show();
                                    Intent intent = new Intent(ElementListActivity.this, ElementActivity.class);
                                    intent.putExtra("elementName", elementName);
                                    intent.putExtra("tab", tab);
                                    startActivity(intent);
                                }
                            });
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("errorResponse", ""+error);
                    }
                }
        );
        requestQueue.add(objectRequest);
    }
}
