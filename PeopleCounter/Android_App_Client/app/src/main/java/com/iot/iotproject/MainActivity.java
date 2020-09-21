package com.iot.iotproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String res = null;
    private LinkedList<String> listRes;
    private String url = null;
    private String tab = null;
    private ImageView imageView;
    private LinearLayout buttonContainer;
    private LinearLayout topButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);

        //imageView = (ImageView)findViewById(R.id.imageView2);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        buttonContainer.removeAllViews();


        if (id == R.id.nav_university) {
            url = "http://iotdrwebserver.azurewebsites.net/api/getaule";
            tab = "University";
            //
            //
            // imageView.setVisibility(View.INVISIBLE);

            //View view= (View)findViewById(R.id.constraintLayout);
            //imageView.setVisibility(View.INVISIBLE);
            //ConstraintLayout v = (ConstraintLayout) findViewById(R.id.constraintLayout);



        } else if (id == R.id.nav_bus) {
            url = "http://iotdrwebserver.azurewebsites.net/api/getbus";
            tab = "Bus";
        } else if (id == R.id.nav_city) {
            url = "http://iotdrwebserver.azurewebsites.net/api/getevents";
            tab = "City";
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }

        rest(url, this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void rest(final String url, final Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        listRes = new LinkedList<String>();
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i = 0; i<response.length() ; i++) {
                            try {
                                listRes.add(response.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                        for (int j = 0; j < listRes.size(); j++) {

                            Button btn = new Button(context);
                            btn.setId(j);
                            final String elementName = listRes.get(j);
                            btn.setText(elementName);
                            btn.setBackground(getDrawable(R.drawable.roundedbutton_unselected));
                            buttonContainer.addView(btn);

                            Log.d("iotdr","button created");
                            btn.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    Intent intent = new Intent(MainActivity.this, ElementActivity.class);
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
