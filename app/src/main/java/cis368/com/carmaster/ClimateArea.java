package cis368.com.carmaster;

import android.widget.ImageButton;

/**
 * Created by mike on 11/10/2015.
 */
public class ClimateArea {

    public enum VentState{
        VENT_OFF,
        HEAD_VENT,
        FEET_VENT,
        BOTH_VENT
    }

    public enum Name {
        ALL,
        DRIVER,
        PASSENGER,
        REAR
    }

    private Name name;
    private boolean selected;
    private int tempVal;
    private int blowVal;
    private boolean headVent;
    private boolean feetVent;

    private ImageButton btn;

    public ClimateArea(Name n)
    {
        name = n;
        selected = false;
        tempVal = 10;
        blowVal = 10;
        headVent = false;
        feetVent = false;
        btn = null;
    }

    public Name getName() {
        return name;
    }

    public void setBtn(ImageButton btn) {
        this.btn = btn;
    }

    public VentState setSelected(boolean s) {
        if (s) {
            selected = true;
            btn.setBackgroundResource(R.drawable.blue_btn);

            return getVentState();

        } else {
            selected = false;
            btn.setBackgroundResource(R.drawable.black_btn);
            return null;
        }
    }

    public VentState getVentState() {
        if (headVent && feetVent) {
            return VentState.BOTH_VENT;
        } else if (headVent) {
            return VentState.HEAD_VENT;
        } else if (feetVent) {
            return VentState.FEET_VENT;
        } else {
            return VentState.VENT_OFF;
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setTemp(int t) {
        tempVal = t;
    }
    public void setBlow(int b) {
        blowVal = b;
    }
    public int getTemp() {
        return tempVal;
    }
    public int getBlow() {
        return blowVal;
    }


    public void headVentPushed() {
        if (headVent) {
            headVent = false;
        } else {
            headVent = true;
        }
    }

    public void setHeadVent(boolean b) {
        headVent = b;
    }

    public boolean getHeadVent() {
        return headVent;
    }

    public void feetVentPushed() {
        if (feetVent) {
            feetVent = false;
        } else {
            feetVent = true;
        }
    }

    public void setFeetVent(boolean b) {
        feetVent = b;
    }

    public boolean getFeetVent() {
        return feetVent;
    }

}
