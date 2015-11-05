package com.taru.taru.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Shiran Maor on 11/3/15.
 */
public class TextsClickListener implements View.OnClickListener {

    private View dailyDetails;
    private View enterAmount;
    private View enterAmountDaily;
    private EditText toClear;

    public TextsClickListener(View dailyDetails, View enterAmount, View enterAmountDaily, EditText toClear) {
        this.dailyDetails=  dailyDetails;
        this.enterAmount = enterAmount;
        this.enterAmountDaily = enterAmountDaily;
        this.toClear = toClear;
    }

    @Override
    public void onClick(View v) {
        dailyDetails.setVisibility(View.GONE);
        enterAmount.setVisibility(View.VISIBLE);
        enterAmountDaily.setVisibility(View.GONE);
        toClear.setText("");

    }
}
