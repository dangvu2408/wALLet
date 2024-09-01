package com.example.walletapp.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.walletapp.Constants;
import com.example.walletapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdatingActivity extends AppCompatActivity {
    private Boolean isSuccess;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.updating_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }
        new Update().execute(new Void[0]);
    }

    private class Update extends AsyncTask<Void, Void, Boolean> {
        private String user, pass;
        private Update() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            user = getIntent().getStringExtra("key_update_username");
            pass = getIntent().getStringExtra("key_update_password");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d("UPDATING DATA PARAMS", "Params = " + user + " - " + pass);
            final boolean[] result = {false};
            login(Constants.BASE_URL_LOGIN, user, pass, new DataCallbackSTR() {
                @Override
                public void onDataLoaded(Boolean data) {
                    result[0] = data;
                    Log.d("UPDATING DATA PARAMS", "Params = " + result[0]);
                }
            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result[0];
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            Log.d("DEBUG ERROR UPDATE", "YorN post :vvvv: " + isSuccess);

            int toastLayoutId = (isSuccess) ? R.layout.custom_toast_12 : R.layout.custom_toast_13;
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(toastLayoutId, null);
            Toast toast = new Toast(UpdatingActivity.this);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

            Intent intent = new Intent(UpdatingActivity.this, MainActivity.class);
            intent.putExtra("key_data", user);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
            finish();
        }
    }

    private void login(String url, String str1, String str2, DataCallbackSTR callback) {

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Success")) {
                            isSuccess = true;
                        } else {
                            isSuccess = false;
                        }
                        callback.onDataLoaded(isSuccess);
                        Log.d("DEBUG ERROR UPDATE", "YorN" + isSuccess);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DEBUG ERROR UPDATE", error.toString());
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usernameSignin", str1);
                params.put("passwordSignin", str2);
                return params;
            }
        };
        queue.add(strRequest);
    }

    public interface DataCallbackSTR {
        void onDataLoaded(Boolean data);
    }
}
