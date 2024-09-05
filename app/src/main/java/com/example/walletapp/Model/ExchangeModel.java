package com.example.walletapp.Model;

public class ExchangeModel {
    String currencyCode, buy, transfer, sell;

    public ExchangeModel(String currencyCode, String buy, String transfer, String sell) {
        this.currencyCode = currencyCode;
        this.buy = buy;
        this.transfer = transfer;
        this.sell = sell;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }
}
