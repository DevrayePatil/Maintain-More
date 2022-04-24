package com.example.maintainmore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivityInfo";

    Toolbar toolbar;
    ListView listViewSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        listViewSettings = findViewById(R.id.listViewSettings);

        SettingsList();
    }

    private void SettingsList() {
        String[] cities = {"Themes"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, cities);
        listViewSettings.setAdapter(adapter);

        listViewSettings.setOnItemClickListener((adapterView, view12, i, l) ->
        {

            if ( i == 0){
                Log.i(TAG, "Choose theme");
                ChooseTheme();
            }

        });
    }

    private void ChooseTheme() {
        final String[] themes = {"Dark Theme", "Light Theme", "System Default"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Theme");
        builder.setItems(themes, (dialog, theme) -> {
            
            int getTheme;

            switch (theme){
                case 0:
                    Log.i(TAG, "Dark Theme");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case 1:
                    Log.i(TAG, "Light Theme");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case 3:
                    Log.i(TAG, "System Default");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }

            if (theme == 0){
                Log.i(TAG, "Dark Theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                getTheme = AppCompatDelegate.MODE_NIGHT_YES;
            }
            else if (theme == 1){
                Log.i(TAG, "Light Theme");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                getTheme = AppCompatDelegate.MODE_NIGHT_NO;
            }
            else {
                Log.i(TAG, "System Default");
                getTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

            }
            AppCompatDelegate.setDefaultNightMode(getTheme);

        });

//        Display Alert Dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}