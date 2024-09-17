package com.example.walletapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.walletapp.Activity.LoginActivity;
import com.example.walletapp.Activity.PasswordActivity;

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
            logoutDialog.dismiss();
        }
    }

    private void showLogoutDialog(Activity currentActivity) {
        if (currentActivity instanceof LoginActivity) return;
        if (logoutDialog != null && logoutDialog.isShowing()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialog = inflater.inflate(R.layout.dialog_logout_timeout, null);
        builder.setView(dialog);
        builder.setCancelable(false);
        logoutDialog = builder.create();

        if (currentActivity != null && !currentActivity.isFinishing()) {
            logoutDialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent_background);
            ImageView close_dialog = dialog.findViewById(R.id.close_dialog);
            Button back_to_login_btn = dialog.findViewById(R.id.back_to_login_btn);

            close_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutDialog.dismiss();
                    forceLogout(currentActivity);
                }
            });
            back_to_login_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutDialog.dismiss();
                    forceLogout(currentActivity);
                }
            });

            logoutDialog.show();
        }
    }

    private void forceLogout(Activity currentActivity) {
        Intent intent = new Intent(currentActivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        currentActivity.overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
    }
}
