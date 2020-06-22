package com.example.astrocalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Astro extends AppCompatActivity {

    private TextView time;
    private TextView location;
    private TextView sunriseTime;
    private TextView sunriseAzimuth;
    private TextView sunsetTime;
    private TextView sunsetAzimuth;
    private TextView twilightMorning;
    private TextView twilightEvening;
    private TextView moonriseTime;
    private TextView moonsetTime;
    private TextView nextNewMoon;
    private TextView nextFullMoon;
    private TextView moonIllumination;
    private TextView moonAge;
    private ViewPager viewPager;
    private TabLayout tabs;
    private ViewPagerAdapter viewPagerAdapter;
    private double longitude;
    private double latitude;
    private int refreshTime;
    private boolean enabled;
    Handler mHandler = new Handler();
    DataMenager dataMenager = new DataMenager();
    AstroCalculator astroCalculator;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astro);
        enabled = true;

        time = findViewById(R.id.time);
        location = findViewById(R.id.location);

        longitude = getIntent().getBundleExtra("bundle").getDouble("longitude");
        latitude = getIntent().getBundleExtra("bundle").getDouble("latitude");
        location.setText("Longitude: " + longitude + "   Latitude: " + latitude);

        refreshTime = getIntent().getBundleExtra("bundle").getInt("refreshTime");

        Calendar c = Calendar.getInstance();
        AstroDateTime dateTime = new AstroDateTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND),
                -(c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)) / (60 * 1000), c.getTimeZone().inDaylightTime(c.getTime()));

        astroCalculator = new AstroCalculator(dateTime, new AstroCalculator.Location(latitude, longitude));

        if(!isTablet(this)){
            viewPager = findViewById(R.id.pager);
            tabs = findViewById(R.id.tabs);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), astroCalculator, refreshTime);
            viewPager.setAdapter(viewPagerAdapter);
            tabs.setupWithViewPager(viewPager);
        }
        else{
            init();

            sunriseTime.setText(dataMenager.timeToString(astroCalculator.getSunInfo().getSunrise()));
            sunriseAzimuth.setText(Double.toString(astroCalculator.getSunInfo().getAzimuthRise()).substring(0, Double.toString(astroCalculator.getSunInfo().getAzimuthRise()).lastIndexOf(".") + 3));
            sunsetTime.setText(dataMenager.timeToString(astroCalculator.getSunInfo().getSunset()));
            sunsetAzimuth.setText(Double.toString(astroCalculator.getSunInfo().getAzimuthSet()).substring(0, Double.toString(astroCalculator.getSunInfo().getAzimuthSet()).lastIndexOf(".") + 3));
            twilightMorning.setText(dataMenager.timeToString(astroCalculator.getSunInfo().getTwilightMorning()));
            twilightEvening.setText(dataMenager.timeToString(astroCalculator.getSunInfo().getTwilightEvening()));

            moonriseTime.setText(dataMenager.timeToString(astroCalculator.getMoonInfo().getMoonrise()));
            moonsetTime.setText(dataMenager.timeToString(astroCalculator.getMoonInfo().getMoonset()));
            nextNewMoon.setText(dataMenager.dateToString(astroCalculator.getMoonInfo().getNextNewMoon()));
            nextFullMoon.setText(dataMenager.dateToString(astroCalculator.getMoonInfo().getNextFullMoon()));
            moonIllumination.setText(Double.toString(astroCalculator.getMoonInfo().getIllumination() * 100.0).substring(0, Double.toString(astroCalculator.getMoonInfo().getIllumination()).lastIndexOf(".") + 1) + " %");
            moonAge.setText(Double.toString(astroCalculator.getMoonInfo().getAge()/29.53).substring(0, Double.toString(astroCalculator.getMoonInfo().getAge()).lastIndexOf("." ) - 1) + " days");

            refresh(refreshTime);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();
        enabled = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        enabled = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        timer(dateFormat);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble("longitude", longitude);
        outState.putDouble("latitude", latitude);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        location.setText("Longitude: " + savedInstanceState.getDouble("longitude") + "   Latitude: " + savedInstanceState.getDouble("latitude"));
    }

    public void init(){
        sunriseTime = findViewById(R.id.sunrise_time);
        sunriseAzimuth = findViewById(R.id.sunrise_azimuth);
        sunsetTime = findViewById(R.id.sunset_time);
        sunsetAzimuth = findViewById(R.id.sunset_azimuth);
        twilightMorning = findViewById(R.id.twilight_morning);
        twilightEvening = findViewById(R.id.twilight_evening);

        moonriseTime = findViewById(R.id.moonrise_time);
        moonsetTime = findViewById(R.id.moonset_time);
        nextNewMoon = findViewById(R.id.next_new_moon);
        nextFullMoon = findViewById(R.id.next_full_moon);
        moonIllumination = findViewById(R.id.moon_illumination);
        moonAge = findViewById(R.id.moon_age);
    }

    public void timer(final SimpleDateFormat dateFormat){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(enabled){
                    try {
                        Thread.sleep(1000);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String formattedDate = dateFormat.format(Calendar.getInstance().getTime());
                                time.setText(formattedDate);
                                //Toast.makeText(getApplicationContext(), "refreshed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    public void refresh(final int refreshTime){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(enabled){
                    try {
                        Thread.sleep(refreshTime*60*1000);
                        mHandler.post(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {

                                sunriseTime.setText(dataMenager.timeToString(astroCalculator.getSunInfo().getSunrise()));
                                sunriseAzimuth.setText(Double.toString(astroCalculator.getSunInfo().getAzimuthRise()).substring(0, Double.toString(astroCalculator.getSunInfo().getAzimuthRise()).lastIndexOf(".") + 3));
                                sunsetTime.setText(dataMenager.timeToString(astroCalculator.getSunInfo().getSunset()));
                                sunsetAzimuth.setText(Double.toString(astroCalculator.getSunInfo().getAzimuthSet()).substring(0, Double.toString(astroCalculator.getSunInfo().getAzimuthSet()).lastIndexOf(".") + 3));
                                twilightMorning.setText(dataMenager.timeToString(astroCalculator.getSunInfo().getTwilightMorning()));
                                twilightEvening.setText(dataMenager.timeToString(astroCalculator.getSunInfo().getTwilightEvening()));

                                moonriseTime.setText(dataMenager.timeToString(astroCalculator.getMoonInfo().getMoonrise()));
                                moonsetTime.setText(dataMenager.timeToString(astroCalculator.getMoonInfo().getMoonset()));
                                nextNewMoon.setText(dataMenager.dateToString(astroCalculator.getMoonInfo().getNextNewMoon()));
                                nextFullMoon.setText(dataMenager.dateToString(astroCalculator.getMoonInfo().getNextFullMoon()));
                                moonIllumination.setText(Double.toString(astroCalculator.getMoonInfo().getIllumination() * 100.0).substring(0, Double.toString(astroCalculator.getMoonInfo().getIllumination()).lastIndexOf(".") + 1) + " %");
                                moonAge.setText(Double.toString(astroCalculator.getMoonInfo().getAge()/29.53).substring(0, Double.toString(astroCalculator.getMoonInfo().getAge()).lastIndexOf("." ) - 1) + " days");
                                //Toast.makeText(getContext(), "REFRESHED moon", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e) {
                    }
                }
            }
        }).start();
    }

    private boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
