package cis368.com.carmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Nav extends Activity {

    // Main menu
    private ImageButton homeBtn;
    private ImageButton climateBtn;
    private ImageButton audioBtn;
    private ImageButton navBtn;
    private ImageButton phoneBtn;

    // Phone connected
    private LinearLayout deviceConnectedLayout;
    private TextView phoneName;

    // back button
    private Button navBackBtn;

    // splash page
    private LinearLayout splashPageLayout;
    private ImageButton enterDestBtn;
    private ImageButton pointsOfIntBtn;
    private ImageButton showMapBtn;

    // enter destination page
    private RelativeLayout searchDestinationLayout;
    private EditText searchBar;
    private FrameLayout searchBtn;

    // points of interest page
    private RelativeLayout pointsOfInterestLayout;
    private RelativeLayout confirmationBox;
    private TextView confirmationText;
    private Button confirmOk;
    private Button confirmCancel;
    private GridLayout categoryGrid;
    private ImageButton foodBtn;
    private ImageButton gasBtn;
    private ImageButton hotelBtn;
    private ImageButton coffeeBtn;
    private ImageButton barsBtn;
    private ImageButton parksBtn;
    private Button poiBackBtn;
    private FrameLayout station1Select;
    private FrameLayout station2Select;
    private FrameLayout station3Select;

    // show map page
    private RelativeLayout showMapLayout;
    private TextView directionsText;


    enum NavPage {
        SPLASH,
        ENTER_DEST,
        POINTS_OF_INT,
        SHOW_MAP
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        deviceConnectedLayout = (LinearLayout) findViewById(R.id.deviceConnectedLayout);

        checkForDeviceConnected();
        setMenuButtonListeners();
        setNavButtonListeners();

        showPage(NavPage.SPLASH);

    }

    private void setNavButtonListeners() {
        navBackBtn = (Button) findViewById(R.id.navBackBtn);
        navBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(NavPage.SPLASH);
            }
        });

        splashPageLayout = (LinearLayout) findViewById(R.id.splashPageLayout);
        enterDestBtn = (ImageButton) findViewById(R.id.enterDestBtn);
        enterDestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(NavPage.ENTER_DEST);
            }
        });
        pointsOfIntBtn = (ImageButton) findViewById(R.id.pointsOfIntBtn);
        pointsOfIntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(NavPage.POINTS_OF_INT);
            }
        });
        showMapBtn = (ImageButton) findViewById(R.id.showMapBtn);
        showMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPage(NavPage.SHOW_MAP);
            }
        });

        searchDestinationLayout = (RelativeLayout) findViewById(R.id.searchDestinationLayout);
        searchBar = (EditText) findViewById(R.id.searchBar);
        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    notYetImplemented();
                }
                return false;
            }
        });
        searchBtn = (FrameLayout) findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notYetImplemented();
            }
        });

        pointsOfInterestLayout = (RelativeLayout) findViewById(R.id.pointsOfInterestLayout);
        categoryGrid = (GridLayout) findViewById(R.id.categoryGrid);
        foodBtn = (ImageButton) findViewById(R.id.foodBtn);
        foodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryGrid();
                pointsOfInterestLayout.setBackgroundResource(R.drawable.food_map);
            }
        });
        gasBtn = (ImageButton) findViewById(R.id.gasBtn);
        gasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryGrid();
                pointsOfInterestLayout.setBackgroundResource(R.drawable.gas_map_bg);
                station1Select.setVisibility(View.VISIBLE);
                station2Select.setVisibility(View.VISIBLE);
                station3Select.setVisibility(View.VISIBLE);
            }
        });
        hotelBtn = (ImageButton) findViewById(R.id.hotelBtn);
        hotelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryGrid();
                pointsOfInterestLayout.setBackgroundResource(R.drawable.hotels_map);
            }
        });
        coffeeBtn = (ImageButton) findViewById(R.id.coffeeBtn);
        coffeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryGrid();
                pointsOfInterestLayout.setBackgroundResource(R.drawable.coffee_map);
            }
        });
        barsBtn = (ImageButton) findViewById(R.id.barsBtn);
        barsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryGrid();
                pointsOfInterestLayout.setBackgroundResource(R.drawable.bars_map);
            }
        });
        parksBtn = (ImageButton) findViewById(R.id.parksBtn);
        parksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCategoryGrid();
                pointsOfInterestLayout.setBackgroundResource(R.drawable.parks_map);
            }
        });
        poiBackBtn = (Button) findViewById(R.id.poiBackBtn);
        poiBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryGrid.setVisibility(View.VISIBLE);
                poiBackBtn.setVisibility(View.INVISIBLE);
                navBackBtn.setVisibility(View.VISIBLE);
                station1Select.setVisibility(View.INVISIBLE);
                station2Select.setVisibility(View.INVISIBLE);
                station3Select.setVisibility(View.INVISIBLE);
                pointsOfInterestLayout.setBackgroundResource(R.drawable.black_dot_bg);
            }
        });

        confirmationText = (TextView) findViewById(R.id.confirmationString);
        confirmationBox = (RelativeLayout) findViewById(R.id.confirmationBox);
        confirmOk = (Button) findViewById(R.id.confirmOK);
        confirmOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickConfirmOk();
            }
        });
        confirmCancel = (Button) findViewById(R.id.confirmCancel);
        confirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickConfirmCancel();
            }
        });

        station1Select = (FrameLayout) findViewById(R.id.station1Select);
        station1Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationBox.setVisibility(View.VISIBLE);
                confirmationText.setText("Navigate to Mobil Gas Station?");
            }
        });

        station2Select = (FrameLayout) findViewById(R.id.station2Select);
        station2Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationBox.setVisibility(View.VISIBLE);
                confirmationText.setText("Navigate to Shell Gas Station?");
            }
        });

        station3Select = (FrameLayout) findViewById(R.id.station3Select);
        station3Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmationBox.setVisibility(View.VISIBLE);
                confirmationText.setText("Navigate to Marathon Gas Station?");
            }
        });

        showMapLayout = (RelativeLayout) findViewById(R.id.showMapLayout);
        showMapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notYetImplemented();
            }
        });


        directionsText = (TextView) findViewById(R.id.directionsText);
    }

    private void hideCategoryGrid() {
        categoryGrid.setVisibility(View.INVISIBLE);
        navBackBtn.setVisibility(View.INVISIBLE);
        poiBackBtn.setVisibility(View.VISIBLE);
    }

    private void showPage(NavPage page) {

        navBackBtn.setVisibility(View.VISIBLE);
        confirmationBox.setVisibility(View.INVISIBLE);

        searchDestinationLayout.setVisibility(View.INVISIBLE);
        pointsOfInterestLayout.setVisibility(View.INVISIBLE);
        showMapLayout.setVisibility(View.INVISIBLE);
        splashPageLayout.setVisibility(View.INVISIBLE);

        switch (page) {
            case SPLASH:
                splashPageLayout.setVisibility(View.VISIBLE);
                navBackBtn.setVisibility(View.INVISIBLE);
                break;
            case ENTER_DEST:
                searchDestinationLayout.setVisibility(View.VISIBLE);
                break;
            case POINTS_OF_INT:
                pointsOfInterestLayout.setVisibility(View.VISIBLE);
                break;
            case SHOW_MAP:
                showMapLayout.setVisibility(View.VISIBLE);
                break;
        }
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
                Intent intent = new Intent(Nav.this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        climateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Nav.this, Climate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Nav.this, Audio.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Do nothing
            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Nav.this, Phone.class);
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

    public void clickConfirmCancel() {
        confirmationBox.setVisibility(View.INVISIBLE);
        showBigToast("Navigation cancelled");
    }

    public void clickConfirmOk() {
        confirmationBox.setVisibility(View.INVISIBLE);
        showBigToast("Head North on Campus Dr");
        pointsOfInterestLayout.setBackgroundResource(R.drawable.start_navigation);
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
