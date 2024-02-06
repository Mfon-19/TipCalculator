package com.example.tipcalculator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar bar = findViewById(R.id.tipSeekBar);

        //set the progress on the seekbar to 15
        bar.setProgress(15);
        TextView rating = findViewById(R.id.ratingTextView);
        rating.setText(R.string.acceptableTip); //set appropriate text
        rating.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.acceptableTip)); //set appropriate color
        TextView tipAmt = findViewById(R.id.tipAmtTextView);
        EditText amount = findViewById(R.id.baseTextBox);
        TextView tipOutput = findViewById(R.id.tipTextView);
        TextView total = findViewById(R.id.totalTextView);

        //add a listener for whenever text changes
        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double totalAmt, tipAmt;
                int tip, baseAmt;
                String tipStr, baseStr;

                //get the base amount as a string
                baseStr = amount.getText().toString();

                //if the base amount string is empty, show error message, then do nothing more
                if(baseStr.isEmpty()){
                    Toast.makeText(MainActivity.this, "Cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if base amount string is not numeric, show error message, then do nothing more
                if(isNumeric(baseStr)){
                    Toast.makeText(MainActivity.this, "Input must be a number", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if both conditionals above are false, convert the base amount to an int
                else
                    baseAmt = Integer.parseInt(baseStr);

                //get the progress from the seekbar
                tip = bar.getProgress();

                //calculate the tip amount and convert it to a formatted string
                tipAmt = ((double)tip / 100) * baseAmt;
                tipStr = String.format("%.2f", tipAmt);

                //set the tip amount in appropriate textview
                tipOutput.setText(tipStr);

                //calculate the total amount and convert it to a formatted string
                totalAmt = ((double)tip / 100 * baseAmt) + baseAmt;
                total.setText(String.format("%.2f", totalAmt));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //get the base amount as a string
                String baseStr = amount.getText().toString();

                //if the base amount string is empty do nothing
                if(baseStr.isEmpty()) return;

                //if the base amount string is not numeric, delete the most recent input
                if(isNumeric(baseStr)){
                    s.delete(baseStr.length() - 1, baseStr.length());
                }
            }
        });

        //add a seek bar listener for when the seekbar changes
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double baseAmt, totalAmt;
                String baseStr;

                //set the tip percentage in appropriate text view
                tipAmt.setText(progress + "%");

                //get the base amount as a string
                baseStr = amount.getText().toString();

                //if the user has not entered a base amount but changes the seekbar set the base amount to zero
                if(baseStr.isEmpty()){
                    baseAmt = 0;
                }

                //else convert the base amount string to an integer
                else {
                    baseAmt = Integer.parseInt(baseStr);
                }

                //calculate the tip amount, convert it to a formatted string and set it in the appropriate textview
                double a = ((double)progress / 100) * baseAmt;
                tipOutput.setText(String.format("%.2f", a));

                //calculate the total amount, convert it to a formatted string and set it in the appropriate textview
                totalAmt = ((double)progress/100 * baseAmt) + baseAmt;
                total.setText(String.format("%.2f", totalAmt));

                //add conditionals to set appropriate text ratings and text colors for the tip percentage
                if(progress < 10){
                    rating.setText(R.string.poorTip);
                    rating.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.poorTip));
                }
                else if(progress < 15){
                    rating.setText(R.string.acceptableTip);
                    rating.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.acceptableTip));
                }
                else if(progress < 20){
                    rating.setText(R.string.goodTip);
                    rating.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.goodTip));
                }else if(progress > 20){
                    rating.setText(R.string.amazingTip);
                    rating.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.amazingTip));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //checks if str matches the regex expression in variable regex
    public static boolean isNumeric(String str) {
        String regex = "[0-9]+";
        return !str.matches(regex);
    }
}