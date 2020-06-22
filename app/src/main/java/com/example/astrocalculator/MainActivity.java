package com.example.astrocalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private int refreshTime;
    private boolean isNull = true;
    private EditText mLongitude;
    private EditText mLatitude;
    private Spinner refresh;
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLongitude = findViewById(R.id.editLongitude);
        mLatitude = findViewById(R.id.editLatitude);
        refresh = findViewById(R.id.selectRefresh);
        enter = findViewById(R.id.enter);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.list, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        refresh.setAdapter(adapter);
        refresh.setOnItemSelectedListener(this);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mLatitude.getText().length() != 0 && mLongitude.getText().length() != 0){

                    Bundle bundle = new Bundle();
                    bundle.putDouble("longitude", Double.parseDouble(mLongitude.getText().toString()));
                    bundle.putDouble("latitude", Double.parseDouble(mLatitude.getText().toString()));
                    bundle.putInt("refreshTime", refreshTime);
                    Intent intent = new Intent(MainActivity.this, Astro.class);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                refreshTime = 1;
                break;
            case 1:
                refreshTime = 2;
                break;
            case 2:
                refreshTime = 5;
                break;
            case 3:
                refreshTime = 10;
                break;
            case 4:
                refreshTime = 15;
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mLatitude.getText().length() != 0 && mLongitude.getText().length() != 0){
            outState.putDouble("longitude", Double.parseDouble(mLongitude.getText().toString()));
            outState.putDouble("latitude", Double.parseDouble(mLatitude.getText().toString()));
            isNull = false;
        }
        outState.putInt("spinnerPosition", refresh.getSelectedItemPosition());

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(!isNull){
            mLongitude.setText(String.valueOf(savedInstanceState.getDouble("longitude")));
            mLatitude.setText(String.valueOf(savedInstanceState.getDouble("latitude")));
        }
        refresh.setSelection(savedInstanceState.getInt("spinnerPosition"));
    }
}
