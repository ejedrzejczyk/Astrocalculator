package com.example.astrocalculator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;

public class SunFragment extends Fragment {

    private TextView mSunriseTime;
    private TextView mSunriseAzimuth;
    private TextView mSunsetTime;
    private TextView mSunsetAzimuth;
    private TextView mTwilightMorning;
    private TextView mTwilightEvening;

    private boolean enabled;
    private int refreshTime;
    private String sunriseTime;
    private String sunriseAzimuth;
    private String sunsetTime;
    private String sunsetAzimuth;
    private String twilightMorning;
    private String twilightEvening;
    private Handler mHandler = new Handler();

    public SunFragment() {
        // Required empty public constructor
    }

    public static SunFragment newInstance(AstroCalculator astroCalculator, int refreshTime){
        DataMenager dataMenager = new DataMenager();
        SunFragment sunFragment = new SunFragment();
        Bundle bundle = new Bundle();
        bundle.putString("sunriseTime", dataMenager.timeToString(astroCalculator.getSunInfo().getSunrise()));
        bundle.putString("sunriseAzimuth", Double.toString(astroCalculator.getSunInfo().getAzimuthRise()).substring(0, Double.toString(astroCalculator.getSunInfo().getAzimuthRise()).lastIndexOf(".") + 3));
        bundle.putString("sunsetTime", dataMenager.timeToString(astroCalculator.getSunInfo().getSunset()));
        bundle.putString("sunsetAzimuth", Double.toString(astroCalculator.getSunInfo().getAzimuthSet()).substring(0, Double.toString(astroCalculator.getSunInfo().getAzimuthSet()).lastIndexOf(".") + 3));
        bundle.putString("twilightMorning", dataMenager.timeToString(astroCalculator.getSunInfo().getTwilightMorning()));
        bundle.putString("twilightEvening", dataMenager.timeToString(astroCalculator.getSunInfo().getTwilightEvening()));
        bundle.putInt("refreshTime", refreshTime);
        sunFragment.setArguments(bundle);

        return sunFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sun, container, false);
        enabled = true;

        mSunriseTime = view.findViewById(R.id.sunrise_time);
        mSunriseAzimuth = view.findViewById(R.id.sunrise_azimuth);
        mSunsetTime = view.findViewById(R.id.sunset_time);
        mSunsetAzimuth = view.findViewById(R.id.sunset_azimuth);
        mTwilightMorning = view.findViewById(R.id.twilight_morning);
        mTwilightEvening = view.findViewById(R.id.twilight_evening);

        if(this.getArguments() != null){
            refreshTime = this.getArguments().getInt("refreshTime");
            sunriseTime = this.getArguments().getString("sunriseTime");
            sunriseAzimuth = this.getArguments().getString("sunriseAzimuth");
            sunsetTime = this.getArguments().getString("sunsetTime");
            sunsetAzimuth = this.getArguments().getString("sunsetAzimuth");
            twilightMorning = this.getArguments().getString("twilightMorning");
            twilightEvening = this.getArguments().getString("twilightEvening");
        }

        mSunriseTime.setText(sunriseTime);
        mSunriseAzimuth.setText(sunriseAzimuth);
        mSunsetTime.setText(sunsetTime);
        mSunsetAzimuth.setText(sunsetAzimuth);
        mTwilightMorning.setText(twilightMorning);
        mTwilightEvening.setText(twilightEvening);

        refresh(refreshTime);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        enabled = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        enabled = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        enabled = true;
    }

    public void refresh(final int refreshTime){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(enabled){
                    try {
                        Thread.sleep(refreshTime*60*1000);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mSunriseTime.setText(sunriseTime);
                                mSunriseAzimuth.setText(sunriseAzimuth);
                                mSunsetTime.setText(sunsetTime);
                                mSunsetAzimuth.setText(sunsetAzimuth);
                                mTwilightMorning.setText(twilightMorning);
                                mTwilightEvening.setText(twilightEvening);
                                //Toast.makeText(getContext(), "REFRESHED sun", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch (Exception e) {
                    }
                }
            }
        }).start();
    }
}
