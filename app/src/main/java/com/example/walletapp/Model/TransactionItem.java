package com.example.walletapp.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class TransactionItem {
    private String dateTrans, moneyTrans, typeTrans, detailTypeTrans, descriptionTrans;
    public TransactionItem(String dateTrans, String moneyTrans, String typeTrans, String detailTypeTrans, String descriptionTrans) {
        this.dateTrans = dateTrans;
        this.moneyTrans = moneyTrans;
        this.typeTrans = typeTrans;
        this.detailTypeTrans = detailTypeTrans;
        this.descriptionTrans = descriptionTrans;
    }

    public String getDateTrans() {
        return dateTrans;
    }

    public void setDateTrans(String dateTrans) {
        this.dateTrans = dateTrans;
    }

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

    public String getDescriptionTrans() {
        return descriptionTrans;
    }

    public void setDescriptionTrans(String descriptionTrans) {
        this.descriptionTrans = descriptionTrans;
    }

    public float getMoneyTransFloat() {
        return Float.parseFloat(moneyTrans.replace(",", ""));
    }
    public LocalDate getDateAsLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));
        try {
            return LocalDate.parse(dateTrans, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null; // Xử lý lỗi nếu có vấn đề khi parse
        }
    }
}
