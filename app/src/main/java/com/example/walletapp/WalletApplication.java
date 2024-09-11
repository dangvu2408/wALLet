package com.example.walletapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.walletapp.Activity.LoginActivity;

public class WalletApplication extends Application {
    private Handler handler = new Handler();
    private Runnable logoutRunnable;
    private AlertDialog logoutDialog;
    private static final long TIMEOUT = 1 * 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                resetUserInactivityHandler(activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                stopUserInactivityHandler();
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    private void resetUserInactivityHandler(Activity currentActivity) {
        stopUserInactivityHandler();
        logoutRunnable = new Runnable() {
            @Override
            public void run() {
                showLogoutDialog(currentActivity);
            }
        };
        handler.postDelayed(logoutRunnable, TIMEOUT);
    }

    private void stopUserInactivityHandler() {
        handler.removeCallbacks(logoutRunnable);
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss(); // Đóng dialog nếu đang hiển thị
        }
    }

    private void showLogoutDialog(Activity currentActivity) {
        if (logoutDialog != null && logoutDialog.isShowing()) return;

        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle("Đã đăng xuất")
                .setMessage("Bạn đã bị đăng xuất do không hoạt động. Vui lòng đăng nhập lại.")
                .setCancelable(false)
                .setPositiveButton("Đăng nhập lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        forceLogout(currentActivity);
                    }
                });

        logoutDialog = builder.create();
        logoutDialog.show();
    }

    private void forceLogout(Activity currentActivity) {
        Intent intent = new Intent(currentActivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
