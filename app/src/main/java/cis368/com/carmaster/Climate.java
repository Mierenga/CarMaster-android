package cis368.com.carmaster;


import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class Climate extends Activity implements View.OnClickListener {

    private ImageButton homeBtn;
    private ImageButton climateBtn;
    private ImageButton audioBtn;
    private ImageButton navBtn;
    private ImageButton phoneBtn;

    private LinearLayout deviceConnectedLayout;
    private TextView phoneName;

    private ImageButton allBtn;
    private ImageButton driverBtn;
    private ImageButton passengerBtn;
    private ImageButton rearBtn;
    private RelativeLayout positionMenu;

    private ImageButton defrostBtn;
    private ImageButton rearDefrostBtn;
    private ImageButton autoBtn;
    private ImageButton ventHeadBtn;
    private ImageButton ventFeetBtn;

    private boolean frontDefrostOn = false;
    private boolean rearDefrostOn = false;
    private boolean autoBtnOn = false;

    private ClimateArea all = new ClimateArea(ClimateArea.Name.ALL);
    private ClimateArea driver = new ClimateArea(ClimateArea.Name.DRIVER);
    private ClimateArea passenger = new ClimateArea(ClimateArea.Name.PASSENGER);
    private ClimateArea rear = new ClimateArea(ClimateArea.Name.REAR);

    private TextView tempValue;
    private SeekBar tempSeekBar;
    private SeekBar blowSeekBar;

    private boolean allTempInvalid = false;
    private boolean allBlowInvalid = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_climate);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        deviceConnectedLayout = (LinearLayout) findViewById(R.id.deviceConnectedLayout);

        setMenuButtonListeners();
        setClimateButtonListeners();

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
                Intent intent = new Intent(Climate.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        climateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Climate.this, Audio.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Climate.this, Nav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Climate.this, Phone.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

    }

    private void setClimateButtonListeners() {
        allBtn = (ImageButton) findViewById(R.id.allBtn);
        driverBtn = (ImageButton) findViewById(R.id.driverBtn);
        passengerBtn = (ImageButton) findViewById(R.id.passengerBtn);
        rearBtn = (ImageButton) findViewById(R.id.rearBtn);

        positionMenu = (RelativeLayout) findViewById(R.id.positionSelectLayout);

        defrostBtn = (ImageButton) findViewById(R.id.defrostBtn);
        rearDefrostBtn = (ImageButton) findViewById(R.id.rearDefrostButton);

        autoBtn = (ImageButton) findViewById(R.id.autoBtn);

        ventHeadBtn = (ImageButton) findViewById(R.id.ventHead);
        ventFeetBtn = (ImageButton) findViewById(R.id.ventFeet);

        tempValue = (TextView) findViewById(R.id.tempValue);
        tempSeekBar = (SeekBar) findViewById(R.id.tempSeekBar);
        blowSeekBar = (SeekBar) findViewById(R.id.seekBar);

        tempValue.setVisibility(View.INVISIBLE);

        tempSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempValue.setText(String.valueOf(progress+60)+"\u00B0F");
                ClimateArea area = getClimateArea();
                area.setTemp(progress);

                if (!area.getHeadVent() && !area.getFeetVent()) {
                    pushHeadVentButton();
                }

                if (area.getName() == ClimateArea.Name.ALL && fromUser) {
                    allTempInvalid = false;

                    driver.setTemp(progress);
                    passenger.setTemp(progress);
                    rear.setTemp(progress);
                } else {
                    all.setTemp(10);
                    allTempInvalid = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        blowSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ClimateArea area = getClimateArea();
                area.setBlow(progress);
                blowSeekBar.setAlpha(1);
                autoBtn.setBackgroundResource(0);
                tempValue.setVisibility(View.INVISIBLE);
                autoBtnOn = false;


                if (!area.getHeadVent() && !area.getFeetVent()) {
                    pushHeadVentButton();
                }

                if (area.getName() == ClimateArea.Name.ALL && fromUser) {
                    allBlowInvalid = false;
                    driver.setBlow(progress);
                    passenger.setBlow(progress);
                    rear.setBlow(progress);
                } else {
                    all.setBlow(10);
                    allBlowInvalid = true;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        allBtn.setOnClickListener(this);
        driverBtn.setOnClickListener(this);
        passengerBtn.setOnClickListener(this);
        rearBtn.setOnClickListener(this);
        defrostBtn.setOnClickListener(this);
        rearDefrostBtn.setOnClickListener(this);
        autoBtn.setOnClickListener(this);
        ventHeadBtn.setOnClickListener(this);
        ventFeetBtn.setOnClickListener(this);

        all.setBtn(allBtn);
        driver.setBtn(driverBtn);
        passenger.setBtn(passengerBtn);
        rear.setBtn(rearBtn);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch(id) {

            case R.id.allBtn:

                if (allTempInvalid) {
                }
                if (allBlowInvalid) {
                }
                selectArea(all);
                break;
            case R.id.driverBtn:
                selectArea(driver);
                break;
            case R.id.passengerBtn:
                selectArea(passenger);
                break;
            case R.id.rearBtn:
                selectArea(rear);
                break;
            case R.id.defrostBtn:
                if (frontDefrostOn) {
                    defrostBtn.setBackgroundResource(0);
                    frontDefrostOn = false;
                } else {
                    defrostBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
                    frontDefrostOn = true;
                }
                break;
            case R.id.rearDefrostButton:
                if (rearDefrostOn) {
                    rearDefrostBtn.setBackgroundResource(0);
                    rearDefrostOn = false;
                } else {
                    rearDefrostBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
                    rearDefrostOn = true;
                }
                break;

            case R.id.ventHead:
                pushHeadVentButton();
                break;
            case R.id.ventFeet:
                pushFeetVentButton();
                break;
            case R.id.autoBtn:
                if (autoBtnOn) {
                    autoBtn.setBackgroundResource(0);
                    blowSeekBar.setAlpha(1);
                    tempValue.setVisibility(View.INVISIBLE);
                    autoBtnOn = false;
                } else {
                    autoBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
                    blowSeekBar.setAlpha(0.2f);
                    tempValue.setVisibility(View.VISIBLE);
                    autoBtnOn = true;
                }
                break;

            default :



        }
    }

    private ClimateArea getClimateArea() {
        if (all.isSelected()) {
            return all;
        } else if (driver.isSelected()) {
            return driver;
        } else if (passenger.isSelected()) {
            return passenger;
        } else if (rear.isSelected()) {
            return rear;
        } else {
            all.setSelected(true);
            return all;
        }
    }

    private void selectArea(ClimateArea ca) {
        all.setSelected(false);
        driver.setSelected(false);
        passenger.setSelected(false);
        rear.setSelected(false);

        ClimateArea.VentState ventState = ca.setSelected(true);

        if (ca.getName() == ClimateArea.Name.ALL) {
            positionMenu.setBackgroundColor(getResources().getColor(R.color.blue_menu_background));
        } else {
            positionMenu.setBackgroundColor(getResources().getColor(R.color.gray_menu_background));
        }

        tempSeekBar.setProgress(ca.getTemp());
        blowSeekBar.setProgress(ca.getBlow());

        switch (ventState)
        {
            case VENT_OFF:
                ventHeadBtn.setBackgroundResource(0);
                ventFeetBtn.setBackgroundResource(0);
                break;
            case FEET_VENT:
                ventHeadBtn.setBackgroundResource(0);
                ventFeetBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
                break;
            case HEAD_VENT:
                ventHeadBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
                ventFeetBtn.setBackgroundResource(0);
                break;
            case BOTH_VENT:
                ventHeadBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
                ventFeetBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
                break;
            default:

        }

    }

    private void pushHeadVentButton() {
        ClimateArea area = getClimateArea();

        if (area.getName() == ClimateArea.Name.ALL) {
            boolean b = !(area.getHeadVent());
            driver.setHeadVent(b);
            passenger.setHeadVent(b);
            rear.setHeadVent(b);
        } else {
            all.setHeadVent(false);
        }

        ClimateArea.VentState ventState = area.getVentState();

        if (ventState == ClimateArea.VentState.BOTH_VENT || ventState == ClimateArea.VentState.HEAD_VENT) {
            ventHeadBtn.setBackgroundResource(0);
        } else {
            ventHeadBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
        }

        area.headVentPushed();
    }

    private void pushFeetVentButton() {
        ClimateArea area = getClimateArea();

        if (area.getName() == ClimateArea.Name.ALL) {
            boolean b = !(area.getFeetVent());
            driver.setFeetVent(b);
            passenger.setFeetVent(b);
            rear.setFeetVent(b);
        } else {
            all.setFeetVent(false);
        }

        ClimateArea.VentState ventState = area.getVentState();

        if (ventState == ClimateArea.VentState.BOTH_VENT || ventState == ClimateArea.VentState.FEET_VENT) {
            ventFeetBtn.setBackgroundResource(0);
        } else {
            ventFeetBtn.setBackgroundResource(R.drawable.yellow_circle_sm);
        }

        area.feetVentPushed();
    }
}
