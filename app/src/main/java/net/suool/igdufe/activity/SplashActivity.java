package net.suool.igdufe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import net.suool.igdufe.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((ImageView) findViewById(R.id.bg)).postDelayed(new Runnable() {

            @Override
            public void run() {
                // 每次打开重新登录
                Log.d("zafu", "...........");
                Intent intent = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
