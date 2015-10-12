package net.suool.igdufe.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.suool.igdufe.R;
import net.suool.igdufe.util.GlobalDataUtil;

public class SettingActivity extends AppCompatActivity {

    private Button btnSwitchLogin, btnCheckUpdate;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.toolbarSet);
        toolbar.setTitle("设置");
        toolbar.setBackgroundColor(getResources().getColor(R.color.light_toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSwitchLogin = (Button) findViewById(R.id.switchLogin);
        btnCheckUpdate = (Button) findViewById(R.id.checkUpdate);

        btnSwitchLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToLogin = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intentToLogin);
                finish();
            }
        });
        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "暂无新版", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 选项的事件监听器
     */
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }
}
