package com.example.walletapp.Model;

public class GridItem {
    private int resouceIcon;
    private String text;
    public GridItem(int resouceIcon, String text) {
        this.resouceIcon = resouceIcon;
        this.text = text;
    }

    public int getResouceIcon() {
        return resouceIcon;
    }

    public void setResouceIcon(int resouceIcon) {
        this.resouceIcon = resouceIcon;
    }

    public String getReText() {
        return text;
    }

    public void setReText(String text) {
        this.text = text;
    }

}
