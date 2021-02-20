package io.agora.tutorials1v1vcall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                chooseActivity();
             //   Intent mainIntent = new Intent(SplashActivity.this, RegisterUserActivity.class);
              //  startActivity(mainIntent);
            //    finish();
            }
        }, 1500);
    }
    private void chooseActivity(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("Id", -100);
        String role = preferences.getString("role", "");
        Log.d("API", role + userId);
        if (userId!= -100){
            if (role == "Student"){
                Intent mainIntent = new Intent(SplashActivity.this, MainActivityStudent.class);
                startActivity(mainIntent);
                finish();
            }else{
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }

        }else{
            Intent mainIntent = new Intent(SplashActivity.this, RegisterUserActivity.class);
            startActivity(mainIntent);
            finish();
        }

    }
}
