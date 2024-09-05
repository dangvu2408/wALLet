package com.example.walletapp.Model;

public class UserInfoModel {
    private String title_info;
    private String content_info;

    public UserInfoModel(String title_info, String content_info) {
        this.title_info = title_info;
        this.content_info = content_info;
    }

    public String getTitle_info() {
        return title_info;
    }

    public void setTitle_info(String title_info) {
        this.title_info = title_info;
    }

    public String getContent_info() {
        return content_info;
    }

    public void setContent_info(String content_info) {
        this.content_info = content_info;
    }
}
