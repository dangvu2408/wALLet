package com.example.walletapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class TransModel implements Parcelable {
    private String typeTrans, detailTypeTrans, moneyTrans, dateTrans, descriptionTrans, userID, userFullname, transID;

    public TransModel(String typeTrans, String detailTypeTrans, String moneyTrans, String dateTrans, String descriptionTrans, String userID, String userFullname, String transID) {
        this.typeTrans = typeTrans;
        this.detailTypeTrans = detailTypeTrans;
        this.moneyTrans = moneyTrans;
        this.dateTrans = dateTrans;
        this.descriptionTrans = descriptionTrans;
        this.userID = userID;
        this.userFullname = userFullname;
        this.transID = transID;
    }

    protected TransModel(Parcel in) {
        typeTrans = in.readString();
        detailTypeTrans = in.readString();
        moneyTrans = in.readString();
        dateTrans = in.readString();
        descriptionTrans = in.readString();
        userID = in.readString();
        userFullname = in.readString();
        transID = in.readString();
    }

    public static final Creator<TransModel> CREATOR = new Creator<TransModel>() {
        @Override
        public TransModel createFromParcel(Parcel in) {
            return new TransModel(in);
        }

        @Override
        public TransModel[] newArray(int size) {
            return new TransModel[size];
        }
    };

    public String getTypeTrans() {
        return typeTrans;
    }

    public void setTypeTrans(String typeTrans) {
        this.typeTrans = typeTrans;
    }

    public String getDetailTypeTrans() {
        return detailTypeTrans;
    }

    public void setDetailTypeTrans(String detailTypeTrans) {
        this.detailTypeTrans = detailTypeTrans;
    }

    public String getMoneyTrans() {
        return moneyTrans;
    }

    public void setMoneyTrans(String moneyTrans) {
        this.moneyTrans = moneyTrans;
    }

    public String getDateTrans() {
        return dateTrans;
    }

    public void setDateTrans(String dateTrans) {
        this.dateTrans = dateTrans;
    }

    public String getDescriptionTrans() {
        return descriptionTrans;
    }

    public void setDescriptionTrans(String descriptionTrans) {
        this.descriptionTrans = descriptionTrans;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public float getMoneyTransFloat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
        format.setParseBigDecimal(true);

        try {
            BigDecimal bigDecimal = (BigDecimal) format.parse(moneyTrans);
            return bigDecimal.floatValue();
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing number", e);
        }
    }
    public LocalDate getDateAsLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));
        try {
            return LocalDate.parse(dateTrans, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(typeTrans);
        dest.writeString(detailTypeTrans);
        dest.writeString(moneyTrans);
        dest.writeString(dateTrans);
        dest.writeString(descriptionTrans);
        dest.writeString(userID);
        dest.writeString(userFullname);
        dest.writeString(transID);
    }
}
