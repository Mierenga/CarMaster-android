package cis368.com.carmaster;

import cis368.com.carmaster.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Home extends Activity {

    private ImageButton homeBtn;
    private ImageButton climateBtn;
    private ImageButton audioBtn;
    private ImageButton navBtn;
    private ImageButton phoneBtn;

    private LinearLayout deviceConnectedLayout;
    private TextView phoneName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        deviceConnectedLayout = (LinearLayout) findViewById(R.id.deviceConnectedLayout);

        setMenuButtonListeners();

        checkForDeviceConnected();

    }

    private void checkForDeviceConnected() {
        phoneName = (TextView) findViewById(R.id.phoneName);
        if (((CarMasterApplication) this.getApplication()).getPhoneConnectState() == CarMasterApplication.PhoneConnectState.CONNECTED) {
            deviceConnectedLayout.setVisibility(View.VISIBLE);
            phoneName.setText(((CarMasterApplication) this.getApplication()).getConnectedPhone());
        } else {
            deviceConnectedLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void setMenuButtonListeners() {

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        climateBtn = (ImageButton) findViewById(R.id.climateBtn);
        audioBtn = (ImageButton) findViewById(R.id.audioBtn);
        navBtn = (ImageButton) findViewById(R.id.navBtn);
        phoneBtn = (ImageButton) findViewById(R.id.phoneBtn);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        climateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Climate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Audio.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Nav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Phone.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });




    }
}
