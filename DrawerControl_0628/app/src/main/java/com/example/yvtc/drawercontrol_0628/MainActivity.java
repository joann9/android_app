package com.example.yvtc.drawercontrol_0628;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private ImageView planetImage;
    private TextView planettext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_ID);
        setSupportActionBar(toolbar);
        planetImage = (ImageView) findViewById(R.id.imageView2_ID);
        planettext = (TextView) findViewById(R.id.textView_ID);
        planetImage.setVisibility(View.INVISIBLE);
        planettext.setText("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlayout_ID);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        //另一種方法
        View header = navigationView.getHeaderView(0);

        TextView txtHeader = (TextView) header.findViewById(R.id.textView);
        txtHeader.setText("new Header");

        navigationView.setItemIconTintList(null); //icon我們給圖片, 不須要顏色, 所以填null

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {

                    case R.id.nav_earth:
                        planetImage.setVisibility(View.VISIBLE);
                        planetImage.setImageResource(R.drawable.earth);
                        planettext.setText("Earth");
                        break;

                    case R.id.nav_jupiter:
                        planetImage.setVisibility(View.VISIBLE);
                        planetImage.setImageResource(R.drawable.jupiter);
                        planettext.setText("Jupiter");

                        break;

                    case R.id.nav_mars:
                        planetImage.setVisibility(View.VISIBLE);
                        planetImage.setImageResource(R.drawable.mars);
                        planettext.setText("Mars");

                        break;


                    case R.id.nav_mercury:
                        planetImage.setVisibility(View.VISIBLE);
                        planetImage.setImageResource(R.drawable.mercury);
                        planettext.setText("Mercury");

                        break;

                    case R.id.nav_neptune:
                        planetImage.setVisibility(View.VISIBLE);
                        planetImage.setImageResource(R.drawable.neptune);
                        planettext.setText("Neptune");

                        break;

                    case R.id.nav_saturn:
                        planetImage.setVisibility(View.VISIBLE);
                        planetImage.setImageResource(R.drawable.saturn);
                        planettext.setText("Saturn");

                        break;

                    case R.id.nav_uranus:
                        planetImage.setVisibility(View.VISIBLE);
                        planetImage.setImageResource(R.drawable.uranus);
                        planettext.setText("Uranus");

                        break;

                }

                DrawerLayout drawer1 = (DrawerLayout) findViewById(R.id.drawerlayout_ID);
                drawer1.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }
}
