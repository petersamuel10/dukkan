package com.example.dukkanexpress;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SpalshScreen extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);

        bgThread();
    }

    /**
     * thread of some time interval delay for next screen launch
     */
    private void bgThread() {
        int SPLASH_WAIT = 3000;
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getDataFromIntent();
            }
        }, SPLASH_WAIT);
    }

    /**
     * get data from intent
     */
    private void getDataFromIntent() {
        if (getIntent() != null) {
            Intent mainIntent = new Intent(SpalshScreen.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(mainIntent);
//            getParent().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }
}
