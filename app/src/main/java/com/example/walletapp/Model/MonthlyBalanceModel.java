package com.example.walletapp.Model;

public class MonthlyBalanceModel {
    private String monthly, moneymonth;

    public MonthlyBalanceModel(String monthly, String moneymonth) {
        this.monthly = monthly;
        this.moneymonth = moneymonth;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public String getMoneymonth() {
        return moneymonth;
    }

    public void setMoneymonth(String moneymonth) {
        this.moneymonth = moneymonth;
    }
}
