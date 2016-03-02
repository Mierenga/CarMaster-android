package cis368.com.carmaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import android.os.Handler;


public class Audio extends Activity implements View.OnClickListener {

    // bottom menu
    private ImageButton homeBtn;
    private ImageButton climateBtn;
    private ImageButton audioBtn;
    private ImageButton navBtn;
    private ImageButton phoneBtn;

    private LinearLayout deviceConnectedLayout;
    private TextView phoneName;

    // side menu
    private Button audioAmBtn;
    private Button audioFmBtn;
    private Button audioAuxBtn;
    private Button audioPhoneBtn;

    // screen content
    private RelativeLayout amScreen;
    private RelativeLayout fmScreen;
    private RelativeLayout auxScreen;
    private RelativeLayout phoneScreen;

    // volume
    private final int REPEAT_DELAY = 100;

    private ImageButton volUp;
    private TextView volText;
    private int volume = 0;
    private ImageButton volDown;
    private ImageButton muteBtn;
    private boolean muteOn = false;

    private Handler volHandler = new Handler();
    private boolean volAutoIncrement = false;
    private boolean volAutoDecrement = false;


    class VolRepeatUpdater implements Runnable {
        public void run() {
            if (volAutoIncrement) {
                incrementVolume();
                volHandler.postDelayed(new VolRepeatUpdater(), REPEAT_DELAY);
            } else if (volAutoDecrement) {
                decrementVolume();
                volHandler.postDelayed(new VolRepeatUpdater(), REPEAT_DELAY);
            }
        }
    }


    private Handler seekHandler = new Handler();
    private boolean seekAutoIncrement = false;
    private boolean seekAutoDecrement = false;
    class SeekRepeatUpdater implements Runnable {
        public void run() {
            if (seekAutoIncrement) {
                switch (audioState) {
                    case AM:
                        nextAmStation();
                        break;
                    case FM:
                        nextFmStation();
                        break;
                }
                seekHandler.postDelayed(new SeekRepeatUpdater(), REPEAT_DELAY);

            } else if (seekAutoDecrement) {
                switch (audioState) {
                    case AM:
                        prevAmStation();
                        break;
                    case FM:
                        prevFmStation();
                        break;
                }
                seekHandler.postDelayed(new SeekRepeatUpdater(), REPEAT_DELAY);

            }
        }
    }


    // am buttons
    private ImageButton amBackward;
    private TextView amStation;
    private ImageButton amForward;
    private ImageButton amStepBack;
    private Button amScan;
    private ImageButton amStepForward;
    private Button[] amPreset = new Button[6];
    private int amStationValue = 860;

    private Handler presetHandler = new Handler();
    private boolean presetHolding = false;
    private int presetCounter = 0;

    class PresetHoldUpdater implements Runnable {

        private Button button;
        public PresetHoldUpdater(Button btn) {
            button = btn;
        }

        public void run() {
            incrementCounter(button);
            if (presetHolding) {
                presetHandler.postDelayed(new PresetHoldUpdater(button), REPEAT_DELAY);
            } else {
                button.setBackgroundResource(R.drawable.black_btn);
            }
        }
    }
    private void incrementCounter(Button button) {
        presetCounter++;
        if (presetCounter >= 5) {
            // set the preset here
            switch (audioState) {
                case AM:
                    button.setText(amStation.getText());
                    break;
                case FM:
                    button.setText(fmStation.getText());
                    break;
            }
            button.setBackgroundResource(R.drawable.blue_btn);
        }
    }

    // fm buttons
    private ImageButton fmBackward;
    private TextView fmStation;
    private ImageButton fmForward;
    private ImageButton fmStepBack;
    private Button fmScan;
    private ImageButton fmStepForward;
    private Button[] fmPreset = new Button[6];
    private float fmStationValue = 104.7f;
    DecimalFormat tenthFormatter = new DecimalFormat("0.0");

    // phone buttons
    private ImageButton prevTrack;
    private ImageButton nextTrack;
    private ImageButton playPauseBtn;
    private ImageButton shuffleBtn;
    private ImageButton repeatBtn;

    public enum AudioState {
        AM,
        FM,
        AUX,
        PHONE
    }

    private AudioState audioState = AudioState.AM;
    final private int MAX_VOLUME = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        deviceConnectedLayout = (LinearLayout) findViewById(R.id.deviceConnectedLayout);

        setMenuButtonListeners();
        setAudioButtonListeners();

        amScreen = (RelativeLayout) findViewById(R.id.amPageContent);
        fmScreen = (RelativeLayout) findViewById(R.id.fmPageContent);
        auxScreen = (RelativeLayout) findViewById(R.id.auxPageContent);
        phoneScreen = (RelativeLayout) findViewById(R.id.phonePageContent);
        checkForDeviceConnected();
        displayAudioScreen(amScreen, audioAmBtn);

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

    private void setAudioButtonListeners() {

        audioAmBtn = (Button) findViewById(R.id.audioAmBtn);
        audioFmBtn = (Button) findViewById(R.id.audioFmBtn);
        audioAuxBtn = (Button) findViewById(R.id.audioAuxBtn);
        audioPhoneBtn = (Button) findViewById(R.id.audioPhoneBtn);

        audioAmBtn.setOnClickListener(this);
        audioFmBtn.setOnClickListener(this);
        audioAuxBtn.setOnClickListener(this);
        audioPhoneBtn.setOnClickListener(this);

        // volume
        volUp = (ImageButton) findViewById(R.id.volumeUp);
        volText = (TextView) findViewById(R.id.volumeValue);
        volDown = (ImageButton) findViewById(R.id.volumeDown);
        muteBtn = (ImageButton) findViewById(R.id.muteBtn);

        volUp.setOnClickListener(this);
        volUp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                volAutoIncrement = true;
                volHandler.post(new VolRepeatUpdater());
                return false;
            }
        });
        volUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL) && volAutoIncrement) {
                    volAutoIncrement = false;
                }
                return false;
            }
        });
        volDown.setOnClickListener(this);
        volDown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                volAutoDecrement = true;
                volHandler.post(new VolRepeatUpdater());
                return false;
            }
        });
        volDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL) && volAutoDecrement) {
                    volAutoDecrement = false;
                }
                return false;
            }
        });
        muteBtn.setOnClickListener(this);

        // am buttons
        amBackward = (ImageButton) findViewById(R.id.amBackward);
        amStation = (TextView) findViewById(R.id.amStation);
        amForward = (ImageButton) findViewById(R.id.amForward);
        amStepBack = (ImageButton) findViewById(R.id.amStepBack);
        amScan = (Button) findViewById(R.id.amScan);
        amStepForward = (ImageButton) findViewById(R.id.amStepForward);
        amPreset[0] = (Button) findViewById(R.id.amPreset1);
        amPreset[1] = (Button) findViewById(R.id.amPreset2);
        amPreset[2] = (Button) findViewById(R.id.amPreset3);
        amPreset[3] = (Button) findViewById(R.id.amPreset4);
        amPreset[4] = (Button) findViewById(R.id.amPreset5);
        amPreset[5] = (Button) findViewById(R.id.amPreset6);

        amBackward.setOnClickListener(this);
        amBackward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                seekAutoDecrement = true;
                seekHandler.post(new SeekRepeatUpdater());
                return false;
            }
        });
        amBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL) && seekAutoDecrement) {
                    seekAutoDecrement = false;
                }
                return false;
            }
        });
        amForward.setOnClickListener(this);
        amForward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                seekAutoIncrement = true;
                seekHandler.post(new SeekRepeatUpdater());
                return false;
            }
        });
        amForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL) && seekAutoIncrement) {
                    seekAutoIncrement = false;
                }
                return false;
            }
        });



        amStepBack.setOnClickListener(this);
        amScan.setOnClickListener(this);
        amStepForward.setOnClickListener(this);


        // fm buttons
        fmBackward = (ImageButton) findViewById(R.id.fmBackward);
        fmStation = (TextView) findViewById(R.id.fmStation);
        fmForward = (ImageButton) findViewById(R.id.fmForward);
        fmStepBack = (ImageButton) findViewById(R.id.fmStepBack);
        fmScan = (Button) findViewById(R.id.fmScan);
        fmStepForward = (ImageButton) findViewById(R.id.fmStepForward);
        fmPreset[0] = (Button) findViewById(R.id.fmPreset1);
        fmPreset[1] = (Button) findViewById(R.id.fmPreset2);
        fmPreset[2] = (Button) findViewById(R.id.fmPreset3);
        fmPreset[3] = (Button) findViewById(R.id.fmPreset4);
        fmPreset[4] = (Button) findViewById(R.id.fmPreset5);
        fmPreset[5] = (Button) findViewById(R.id.fmPreset6);

        fmBackward.setOnClickListener(this);
        fmBackward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                seekAutoDecrement = true;
                seekHandler.post(new SeekRepeatUpdater());
                return false;
            }
        });
        fmBackward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL) && seekAutoDecrement) {
                    seekAutoDecrement = false;
                }
                return false;
            }
        });
        fmForward.setOnClickListener(this);
        fmForward.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                seekAutoIncrement = true;
                seekHandler.post(new SeekRepeatUpdater());
                return false;
            }
        });
        fmForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL) && seekAutoIncrement) {
                    seekAutoIncrement = false;
                }
                return false;
            }
        });
        fmStepBack.setOnClickListener(this);
        fmScan.setOnClickListener(this);
        fmStepForward.setOnClickListener(this);
        for (int i = 0; i < 6; i++) {
            amPreset[i].setOnClickListener(this);
            amPreset[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    presetHolding = true;
                    presetHandler.post(new PresetHoldUpdater((Button) v));
                    return false;
                }
            });
            amPreset[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL) && presetHolding) {
                        presetCounter = 0;
                        presetHolding = false;
                    }
                    return false;
                }
            });
            fmPreset[i].setOnClickListener(this);
            fmPreset[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    presetHolding = true;
                    presetHandler.post(new PresetHoldUpdater((Button) v));
                    return false;
                }
            });
            fmPreset[i].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if ((event.getAction() == MotionEvent.ACTION_UP) || (event.getAction() == MotionEvent.ACTION_CANCEL) && presetHolding) {
                        presetCounter = 0;
                        presetHolding = false;
                    }
                    return false;
                }
            });
        }

        // phone buttons
        prevTrack = (ImageButton) findViewById(R.id.phonePrevBtn);
        playPauseBtn = (ImageButton) findViewById(R.id.phonePlayBtn);
        nextTrack = (ImageButton) findViewById(R.id.phoneNextBtn);
        shuffleBtn = (ImageButton) findViewById(R.id.shuffleBtn);
        repeatBtn = (ImageButton) findViewById(R.id.repeatBtn);

        prevTrack.setOnClickListener(this);
        playPauseBtn.setOnClickListener(this);
        nextTrack.setOnClickListener(this);
        shuffleBtn.setOnClickListener(this);
        repeatBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        // First, check if it was an audio menu button

        switch (v.getId()) {
            case R.id.audioAmBtn:
                audioState = AudioState.AM;
                displayAudioScreen(amScreen, audioAmBtn);
                break;
            case R.id.audioFmBtn:
                audioState = AudioState.FM;
                displayAudioScreen(fmScreen, audioFmBtn);
                break;
            case R.id.audioAuxBtn:
                audioState = AudioState.AUX;
                displayAudioScreen(auxScreen, audioAuxBtn);
                break;
            case R.id.audioPhoneBtn:
                audioState = AudioState.PHONE;
                displayAudioScreen(phoneScreen, audioPhoneBtn);
                break;
            case R.id.volumeUp:
                incrementVolume();
                break;
            case R.id.volumeDown:
                decrementVolume();
                break;
            case R.id.muteBtn:
                muteClicked();
                break;
            default:
                // then call the function for the current page
                switch (audioState) {
                    case AM:
                        amButtonSwitch(v);
                        break;
                    case FM:
                        fmButtonSwitch(v);
                        break;
                    case AUX:
                        auxButtonSwitch(v);
                        break;
                    case PHONE:
                        phoneButtonSwitch(v);
                        break;
                }

        }


    }

    private void incrementVolume() {
        if (volume < MAX_VOLUME && !muteOn) {
            volume++;
            if (volume == MAX_VOLUME) {
                volText.setTextSize(20);
                volText.setText("MAX");
            } else {
                volText.setTextSize(40);
                volText.setText(String.valueOf(volume));
            }
        }
    }

    private void decrementVolume() {
        if (volume > 0 && !muteOn) {
            volume--;
            volText.setTextSize(40);
            volText.setText(String.valueOf(volume));
        }
    }

    private void muteClicked() {
        if (muteOn) {
            muteOn = false;
            muteBtn.setBackgroundResource(R.drawable.black_btn);
            volText.setTextSize(40);
            volText.setText(String.valueOf(volume));
            volDown.setVisibility(View.VISIBLE);
            volUp.setVisibility(View.VISIBLE);
        } else {
            muteOn = true;
            muteBtn.setBackgroundResource(R.drawable.blue_btn);
            volText.setTextSize(20);
            volText.setText("MUTE");
            volDown.setVisibility(View.INVISIBLE);
            volUp.setVisibility(View.INVISIBLE);
        }
    }

    private void amButtonSwitch(View v) {
        switch (v.getId()) {
            case R.id.amBackward:
                prevAmStation();
                break;
            case R.id.amForward:
                nextAmStation();
                break;
            case R.id.amStepBack:
                moveAmDown();
                break;
            case R.id.amStepForward:
                moveAmUp();
                break;
            case R.id.amScan:
                notYetImplemented();
                break;
            default:
                try {
                    if (!((Button) v).getText().toString().contains("hold to set")){
                        amStation.setText(((Button) v).getText());
                        amStationValue = Integer.valueOf(amStation.getText().toString());
                    }
                } catch (NumberFormatException E) {

                }

        }
    }

    private void fmButtonSwitch(View v) {
        switch (v.getId()) {
            case R.id.fmBackward:
                prevFmStation();
                break;
            case R.id.fmForward:
                nextFmStation();
                break;
            case R.id.fmStepBack:
                moveFmDown();
                break;
            case R.id.fmStepForward:
                moveFmUp();
                break;
            case R.id.fmScan:
                notYetImplemented();
                break;
            default:
                try {
                    if (!((Button) v).getText().toString().contains("hold to set")) {
                        fmStation.setText(((Button) v).getText());
                        fmStationValue = Float.valueOf(fmStation.getText().toString());
                    }
                } catch (NumberFormatException E) {

                }

        }
    }

    private void auxButtonSwitch(View v) {

    }

    private void phoneButtonSwitch(View v) {
        notYetImplemented();
    }

    private void moveAmDown() {
        amStationValue -= 10;
        if (amStationValue < 540) {
            amStationValue = 1610;
        }
        amStation.setText(String.valueOf(amStationValue));
    }

    private void moveAmUp() {
        amStationValue += 10;
        if (amStationValue > 1610) {
            amStationValue = 540;
        }
        amStation.setText(String.valueOf(amStationValue));
    }

    private void nextAmStation() {
        amStationValue += 80;
        if (amStationValue > 1610) {
            amStationValue = 540;
        }
        amStation.setText(String.valueOf(amStationValue));
    }

    private void prevAmStation() {
        amStationValue -= 80;
        if (amStationValue < 540) {
            amStationValue = 1610;
        }
        amStation.setText(String.valueOf(amStationValue));
    }


    private void moveFmDown() {
        fmStationValue -= 0.1f;
        if (fmStationValue < 87.8f) {
            fmStationValue = 108.0f;
        }

        fmStation.setText(String.valueOf(tenthFormatter.format(fmStationValue)));
    }

    private void moveFmUp() {
        fmStationValue += 0.1f;
        if (fmStationValue > 108.0f) {
            fmStationValue = 87.8f;
        }
        fmStation.setText(String.valueOf(tenthFormatter.format(fmStationValue)));
    }

    private void nextFmStation() {
        fmStationValue += 1.2f;
        if (fmStationValue > 108.0f) {
            fmStationValue = 87.8f;
        }
        fmStation.setText(String.valueOf(tenthFormatter.format(fmStationValue)));
    }

    private void prevFmStation() {
        fmStationValue -= 1.2f;
        if (fmStationValue < 87.8f) {
            fmStationValue = 108.0f;
        }
        fmStation.setText(String.valueOf(tenthFormatter.format(fmStationValue)));
    }

    private void displayAudioScreen(RelativeLayout screen, Button btn) {
        amScreen.setVisibility(View.INVISIBLE);
        fmScreen.setVisibility(View.INVISIBLE);
        auxScreen.setVisibility(View.INVISIBLE);
        phoneScreen.setVisibility(View.INVISIBLE);
        audioAmBtn.setBackgroundResource(R.drawable.black_btn);
        audioFmBtn.setBackgroundResource(R.drawable.black_btn);
        audioAuxBtn.setBackgroundResource(R.drawable.black_btn);
        audioPhoneBtn.setBackgroundResource(R.drawable.black_btn);

        screen.setVisibility(View.VISIBLE);
        btn.setBackgroundResource(R.drawable.blue_btn);

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
                Intent intent = new Intent(Audio.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        climateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Audio.this, Climate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Do nothing
            }
        });

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Audio.this, Nav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Audio.this, Phone.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });



    }

    public void notYetImplemented() {
        showBigToast("Feature Not Yet Implemented");
    }

    private void showBigToast(String str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(35);
        toast.show();
    }
}
