package cis368.com.carmaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cis368.com.carmaster.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class Phone extends Activity implements View.OnClickListener {

    // Main menu
    private ImageButton homeBtn;
    private ImageButton climateBtn;
    private ImageButton audioBtn;
    private ImageButton navBtn;
    private ImageButton phoneBtn;

    private LinearLayout deviceConnectedLayout;
    private TextView phoneName;

    // First button
    private Button pairFirstPhoneBtn;

    // Dial pad
    private GridLayout dialPadLayout;
    private Button[] dialPad = new Button[12];
    private EditText dialText;
    private Button callBtn;
    private Button delBtn;

    // contacts list
    private RelativeLayout contactsListLayout;
    private ListView contactsList;
    private Button managePhoneBtn;

    ArrayList<String> contactsListItems = new ArrayList<String>();
    ArrayAdapter<String> contactsAdapter;

    // phones list
    private RelativeLayout phoneListLayout;
    private ListView phoneList;
    private Button pairNewPhoneBtn;
    private String selectedPhone = "";
    private String connectedPhone = "";
    private Button backBtn;

    ArrayList<String> phoneListItems = new ArrayList<String>();
    ArrayAdapter<String> phoneAdapter;

    // manage phones
    private LinearLayout managePhoneMenu;
    private Button connectPhoneBtn;
    private Button removePhoneBtn;

    // in call
    private RelativeLayout inCallLayout;
    private TextView callingText;
    private Button endCallBtn;
    private ImageButton volUp;
    private TextView volText;
    private ImageButton volDown;
    private ImageButton muteBtn;
    private int volume = 10;
    private boolean muteOn= false;

    CarMasterApplication application = (CarMasterApplication) this.getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        deviceConnectedLayout = (LinearLayout) findViewById(R.id.deviceConnectedLayout);
        checkForDeviceConnected();

        initializeArrayLists();

        setMenuButtonListeners();
        setPhoneButtonListeners();
        setupInitialVisibility();

        contactsAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_textview, contactsListItems);
        contactsList.setAdapter(contactsAdapter);

        phoneAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_textview, phoneListItems);
        phoneList.setAdapter(phoneAdapter);
        connectedPhone = ((CarMasterApplication) this.getApplication()).getConnectedPhone();

    }

    @Override
    protected void onPause() {
        super.onPause();
        ((CarMasterApplication) this.getApplication()).saveContacts(phoneListItems);
        ((CarMasterApplication) this.getApplication()).setConnectedPhone(connectedPhone);
    }

    private void initializeArrayLists() {
        contactsListItems.add("Angela");
        contactsListItems.add("Chuck");
        contactsListItems.add("Dad");
        contactsListItems.add("Elmer");
        contactsListItems.add("Francis");
        contactsListItems.add("Margo");
        contactsListItems.add("Milly");

        phoneListItems = ((CarMasterApplication) this.getApplication()).loadContacts();

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

    void setupInitialVisibility() {
        managePhoneMenu.setVisibility(View.INVISIBLE);
        phoneListLayout.setVisibility(View.INVISIBLE);
        contactsListLayout.setVisibility(View.INVISIBLE);
        dialPadLayout.setVisibility(View.INVISIBLE);
        pairFirstPhoneBtn.setVisibility(View.INVISIBLE);
        inCallLayout.setVisibility(View.INVISIBLE);

        CarMasterApplication.PhoneConnectState state = ((CarMasterApplication) this.getApplication()).getPhoneConnectState();

        switch (state) {
            case NEVER_CONNECTED:
                pairFirstPhoneBtn.setVisibility(View.VISIBLE);
                break;
            case NOT_CONNECTED:
                phoneListLayout.setVisibility(View.VISIBLE);
                break;
            case CONNECTED:
                contactsListLayout.setVisibility(View.VISIBLE);
                dialPadLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }


    }

    private void setPhoneButtonListeners() {

        pairFirstPhoneBtn = (Button) findViewById(R.id.pairFirstPhoneBtn);

        pairFirstPhoneBtn.setOnClickListener(this);

        managePhoneMenu = (LinearLayout) findViewById(R.id.managePhoneMenu);
        connectPhoneBtn = (Button) findViewById(R.id.connectBtn);
        removePhoneBtn = (Button) findViewById(R.id.removeBtn);

        connectPhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectedPhone == connectedPhone) {
                    disconnectPhone();
                } else {
                    connectPhone();
                }
            }
        });

        removePhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhone();
            }
        });

        phoneListLayout = (RelativeLayout) findViewById(R.id.phoneListLayout);
        phoneList = (ListView) findViewById(R.id.phoneList);
        pairNewPhoneBtn = (Button) findViewById(R.id.pairPhoneBtn);
        backBtn = (Button) findViewById(R.id.backToContactsBtn);

        backBtn.setOnClickListener(this);

        phoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                selectedPhone = phoneAdapter.getItem(position);

                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (i == position) {
                        parent.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.blue_menu_background));
                    } else {
                        parent.getChildAt(i).setBackgroundColor(0);
                    }
                }

                managePhoneMenu.setVisibility(View.VISIBLE);

                if (selectedPhone == connectedPhone) {
                    connectPhoneBtn.setText("Disconnect Phone");
                } else {
                    connectPhoneBtn.setText("Connect Phone");
                }


            }
        });

        pairNewPhoneBtn.setOnClickListener(this);

        contactsListLayout = (RelativeLayout) findViewById(R.id.contactListLayout);
        contactsList = (ListView) findViewById(R.id.contactsList);
        managePhoneBtn = (Button) findViewById(R.id.managePhoneBtn);

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                String selectedContact = contactsAdapter.getItem(position);
                dialText.setText(selectedContact);


            }
        });
        managePhoneBtn.setOnClickListener(this);

        inCallLayout = (RelativeLayout) findViewById(R.id.inCallLayout);
        callingText = (TextView) findViewById(R.id.callingText);
        endCallBtn = (Button) findViewById(R.id.endCallBtn);

        volUp = (ImageButton) findViewById(R.id.phoneVolUp);
        volText = (TextView) findViewById(R.id.phoneVolText);
        volDown = (ImageButton) findViewById(R.id.phoneVolDown);
        muteBtn = (ImageButton) findViewById(R.id.phoneMuteBtn);

        endCallBtn.setOnClickListener(this);
        volUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume < 20 && !muteOn) {
                    volume++;
                    if (volume == 20) {
                        volText.setTextSize(20);
                        volText.setText("MAX");
                    } else {
                        volText.setTextSize(40);
                        volText.setText(String.valueOf(volume));
                    }
                }
            }
        });
        volDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume > 0 && !muteOn) {
                    volume--;
                    volText.setTextSize(40);
                    volText.setText(String.valueOf(volume));
                }
            }
        });
        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        // uses own click listeners
        dialPadLayout = (GridLayout) findViewById(R.id.dialPad);
        dialPad[0] = (Button) findViewById(R.id.dial0);
        dialPad[1] = (Button) findViewById(R.id.dial1);
        dialPad[2] = (Button) findViewById(R.id.dial2);
        dialPad[3] = (Button) findViewById(R.id.dial3);
        dialPad[4] = (Button) findViewById(R.id.dial4);
        dialPad[5] = (Button) findViewById(R.id.dial5);
        dialPad[6] = (Button) findViewById(R.id.dial6);
        dialPad[7] = (Button) findViewById(R.id.dial7);
        dialPad[8] = (Button) findViewById(R.id.dial8);
        dialPad[9] = (Button) findViewById(R.id.dial9);
        dialPad[10] = (Button) findViewById(R.id.dialStar);
        dialPad[11] = (Button) findViewById(R.id.dialPound);
        dialText = (EditText) findViewById(R.id.dialText);
        delBtn = (Button) findViewById(R.id.delBtn);
        callBtn = (Button) findViewById(R.id.callBtn);

        for (int i = 0; i < 12; i++) {
            dialPad[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialPadPressed(v);
                }
            });
        }
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPadPressed(v);
            }
        });
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialText.getText().toString().isEmpty()) {
                    showBigToast("Please enter a number or contact name");
                } else {
                    contactsListLayout.setVisibility(View.INVISIBLE);
                    inCallLayout.setVisibility(View.VISIBLE);
                    callBtn.setVisibility(View.INVISIBLE);
                    callingText.setText("Calling " + dialText.getText() + "...");
                    dialText.setText("");

                }

            }
        });


        contactsList.setBackgroundColor(Color.GRAY);
        phoneList.setBackgroundColor(Color.GRAY);

    }

    public void dialPadPressed(View v) {
        switch (v.getId()) {
            case R.id.dial0:
                dialText.setText(dialText.getText()+"0");
                break;
            case R.id.dial1:
                dialText.setText(dialText.getText()+"1");
                break;
            case R.id.dial2:
                dialText.setText(dialText.getText()+"2");
                break;
            case R.id.dial3:
                dialText.setText(dialText.getText()+"3");
                break;
            case R.id.dial4:
                dialText.setText(dialText.getText()+"4");
                break;
            case R.id.dial5:
                dialText.setText(dialText.getText()+"5");
                break;
            case R.id.dial6:
                dialText.setText(dialText.getText()+"6");
                break;
            case R.id.dial7:
                dialText.setText(dialText.getText()+"7");
                break;
            case R.id.dial8:
                dialText.setText(dialText.getText()+"8");
                break;
            case R.id.dial9:
                dialText.setText(dialText.getText()+"9");
                break;
            case R.id.dialStar:
                dialText.setText(dialText.getText()+"*");
                break;
            case R.id.dialPound:
                dialText.setText(dialText.getText()+"#");
                break;
            case R.id.delBtn:
                if (dialText.getText().length() > 0) {
                    dialText.setText(dialText.getText().subSequence(0, dialText.getText().length() - 1));
                }
                break;
        }
    }

    private void setMenuButtonListeners() {

        homeBtn = (ImageButton) findViewById(R.id.homeBtn);
        climateBtn = (ImageButton) findViewById(R.id.climateBtn);
        audioBtn = (ImageButton) findViewById(R.id.audioBtn);
        navBtn = (ImageButton) findViewById(R.id.navBtn);
        phoneBtn = (ImageButton) findViewById(R.id.phoneBtn);

        application = (CarMasterApplication) this.getApplication();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Phone.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        climateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Phone.this, Climate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Phone.this, Audio.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Phone.this, Nav.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (application != null) {
                    if (application.getPhoneConnectState() == CarMasterApplication.PhoneConnectState.CONNECTED) {
                        backToContacts();
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pairFirstPhoneBtn:
                pairFirstPhone();
                break;
            case R.id.pairPhoneBtn:
                pairPhone();
                break;
            case R.id.managePhoneBtn:
                openPhoneManager();
                break;
            case R.id.endCallBtn:
                endCall();
                break;
            case R.id.backToContactsBtn:
                backToContacts();
                break;
            default:

                break;
        }
    }

    private void backToContacts() {

        phoneListLayout.setVisibility(View.INVISIBLE);
        managePhoneMenu.setVisibility(View.INVISIBLE);
        contactsListLayout.setVisibility(View.VISIBLE);
        dialPadLayout.setVisibility(View.VISIBLE);

    }

    private void unselectAllPhones() {

        for (int i = 0; i < phoneList.getChildCount(); i++) {
            phoneList.getChildAt(i).setBackgroundResource(0);
        }

        managePhoneMenu.setVisibility(View.INVISIBLE);

    }

    private void openPhoneManager() {
        dialPadLayout.setVisibility(View.INVISIBLE);
        contactsListLayout.setVisibility(View.INVISIBLE);
        phoneListLayout.setVisibility(View.VISIBLE);
        managePhoneMenu.setVisibility(View.INVISIBLE);

        if (((CarMasterApplication) this.getApplication()).getPhoneConnectState() == CarMasterApplication.PhoneConnectState.CONNECTED) {
            backBtn.setVisibility(View.VISIBLE);
        }

    }

    private void endCall() {
        inCallLayout.setVisibility(View.INVISIBLE);
        contactsListLayout.setVisibility(View.VISIBLE);
        dialText.setText("");
        callBtn.setVisibility(View.VISIBLE);
    }

    private void pairFirstPhone() {

        pairFirstPhoneBtn.setVisibility(View.INVISIBLE);

        pairPhone();

        contactsListLayout.setVisibility(View.VISIBLE);
        dialPadLayout.setVisibility(View.VISIBLE);

    }

    private void pairPhone() {

        ((CarMasterApplication) this.getApplication()).incrementPhoneCount();
        ((CarMasterApplication) this.getApplication()).setPhoneConnectState(CarMasterApplication.PhoneConnectState.CONNECTED);
        int count = ((CarMasterApplication) this.getApplication()).getPhoneCount();

        String newPhoneStr = "Phone_" + String.valueOf(count);

        phoneListItems.add(newPhoneStr);
        phoneAdapter.notifyDataSetChanged();

        showBigToast("New Device \"" + newPhoneStr + "\" paired and connected");


        deviceConnectedLayout.setVisibility(View.VISIBLE);
        connectedPhone = newPhoneStr;
        phoneName.setText(connectedPhone);
        backBtn.setVisibility(View.VISIBLE);
        unselectAllPhones();

    }

    private void connectPhone() {
        ((CarMasterApplication) this.getApplication()).setPhoneConnectState(CarMasterApplication.PhoneConnectState.CONNECTED);

        showBigToast("\"" + selectedPhone + "\" connected");
        connectPhoneBtn.setText("Disconnect Phone");

        deviceConnectedLayout.setVisibility(View.VISIBLE);
        connectedPhone = selectedPhone;
        phoneName.setText(connectedPhone);
        backBtn.setVisibility(View.VISIBLE);

    }

    private void disconnectPhone() {

        ((CarMasterApplication) this.getApplication()).setPhoneConnectState(CarMasterApplication.PhoneConnectState.NOT_CONNECTED);
        showBigToast("\"" + selectedPhone + "\" disconnected");
        connectPhoneBtn.setText("Connect Phone");
        deviceConnectedLayout.setVisibility(View.INVISIBLE);
        connectedPhone = "";
        backBtn.setVisibility(View.INVISIBLE);

    }

    private void removePhone() {

        if (selectedPhone == connectedPhone) {
            disconnectPhone();
        }
        phoneAdapter.remove(selectedPhone);
        phoneAdapter.notifyDataSetChanged();
        showBigToast("\"" + selectedPhone + "\" removed");

        unselectAllPhones();

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


