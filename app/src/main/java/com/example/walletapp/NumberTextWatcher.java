package com.example.walletapp;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.widget.EditText;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;


public class NumberTextWatcher implements TextWatcher {

    private static final String LOG_TAG = "AndroidExample";

    private final int numDecimals;
    private String groupingSeparator;
    private String decimalSeparator;
    private boolean nonUsFormat;

    private DecimalFormat decimalFormatDec;
    private DecimalFormat decimalFormatInt;

    private boolean hasFractionalPart;

    private EditText editText;
    private String value;

    public NumberTextWatcher(EditText editText, Locale locale, int numDecimals) {
        this.editText = editText;
        this.numDecimals = numDecimals;
        this.hasFractionalPart = false;

        this.editText.setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

        char gs = symbols.getGroupingSeparator();
        char ds = symbols.getDecimalSeparator();
        this.groupingSeparator = String.valueOf(gs);
        this.decimalSeparator = String.valueOf(ds);

        String patternInt = "#,###";
        this.decimalFormatInt = new DecimalFormat(patternInt, symbols);

        String patternDec = patternInt + "." + replicate('#', this.numDecimals);
        this.decimalFormatDec = new DecimalFormat(patternDec, symbols);
        this.decimalFormatDec.setDecimalSeparatorAlwaysShown(true);
        this.decimalFormatDec.setRoundingMode(RoundingMode.DOWN);

        this.nonUsFormat = !this.decimalSeparator.equals(".");
        this.value = null;
    }


    @Override
    public void afterTextChanged(Editable s) {
        Log.d(LOG_TAG, "afterTextChanged");
        this.editText.removeTextChangedListener(this);

        try {
            int initLeng = this.editText.getText().length();

            String v = this.value.replace(this.groupingSeparator, "");

            Number n = this.decimalFormatDec.parse(v);

            int selectionStart = this.editText.getSelectionStart();
            if (this.hasFractionalPart) {
                int decPos = v.indexOf(this.decimalSeparator) + 1;
                int decLen = v.length() - decPos;
                if (decLen > this.numDecimals) {
                    v = v.substring(0, decPos + this.numDecimals);
                }
                int trz = countTrailingZeros(v);

                StringBuilder fmt = new StringBuilder(this.decimalFormatDec.format(n));
                while (trz-- > 0) {
                    fmt.append("0");
                }
                this.editText.setText(fmt.toString());
            } else {
                this.editText.setText(this.decimalFormatInt.format(n));
            }

            int endLeng = this.editText.getText().length();
            int selection = (selectionStart + (endLeng - initLeng));
            if (selection > 0 && selection <= this.editText.getText().length()) {
                this.editText.setSelection(selection);
            } else {
                // Place cursor at the end?
                this.editText.setSelection(this.editText.getText().length() - 1);
            }
        } catch (NumberFormatException | ParseException nfe) {
            // Do nothing?
        }
        this.editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(LOG_TAG, "beforeTextChanged");
        this.value = this.editText.getText().toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(LOG_TAG, "onTextChanged");

        String newValue = s.toString();
        String change = newValue.substring(start, start + count);
        String prefix = this.value.substring(0, start);
        String suffix = this.value.substring(start + before);

        if (".".equals(change) && this.nonUsFormat) {
            change = this.decimalSeparator;
        }

        this.value = prefix + change + suffix;
        this.hasFractionalPart = this.value.contains(this.decimalSeparator);

        Log.d(LOG_TAG, "VALUE: " + this.value);
    }

    private int countTrailingZeros(String str) {
        int count = 0;

        for (int i = str.length() - 1; i >= 0; i--) {
            char ch = str.charAt(i);
            if ('0' == ch) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private String replicate(char ch, int n) {
        return new String(new char[n]).replace("\0", "" + ch);
    }

}