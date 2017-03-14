package com.example.nejcvesel.pazikjehodis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import com.example.nejcvesel.pazikjehodis.Walkthrough.WalkthroughActivity;

/*
TODO: splash screen
TODO: loader when calling main activity
 */
public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.main_menu);

    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(R.style.BackgroundDefault, true);
        // you could also use a switch if you have many themes that could apply
        return theme;
    }

    public void startMap(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(MainMenuActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Nalagam zemeljevid");
        progressDialog.show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void startExplore(View v) {
        //FragmentManager fm = getFragmentManager();
        //fm.beginTransaction().replace(R.id.mainMenuFrame, new LocationFragment(), "LocationFragment").commit();


    }

    public void startWalkthrough(View v) {

        Intent intent = new Intent(getApplicationContext(), WalkthroughActivity.class);
        startActivity(intent);

    }


}
