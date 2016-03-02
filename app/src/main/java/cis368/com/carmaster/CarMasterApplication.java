package cis368.com.carmaster;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by mike on 11/18/2015.
 */
public class CarMasterApplication extends Application {

    enum PhoneConnectState {
        NOT_CONNECTED,
        CONNECTED,
        NEVER_CONNECTED
    }

    private ArrayList<String> phoneList = new ArrayList<String>();
    private int phoneCount = 0;
    private String connectedPhone = "";

    private PhoneConnectState phoneConnectState = PhoneConnectState.NEVER_CONNECTED;

    public PhoneConnectState getPhoneConnectState() {
        return phoneConnectState;
    }

    public void setPhoneConnectState(PhoneConnectState state) {
        phoneConnectState = state;
    }

    public void saveContacts(ArrayList<String> list) {

        phoneList = list;

    }

    public ArrayList<String> loadContacts() {
        return phoneList;
    }

    public int getPhoneCount() {
        return phoneCount;
    }

    public void incrementPhoneCount() {
        phoneCount++;
    }

    public void decrementPhoneCount() {
        if (phoneCount > 0){
            phoneCount--;
        }
    }

    public void setConnectedPhone(String phone) {
        connectedPhone = phone;
    }

    public String getConnectedPhone() {
        return connectedPhone;
    }
}
