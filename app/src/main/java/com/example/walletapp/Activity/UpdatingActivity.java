package com.example.walletapp.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class UpdatingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.updating_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_1));
        }
        new Update().execute(new Void[0]);
    }

    private class Update extends AsyncTask<Void, Void, Void> {
        private Update() {}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String user = getIntent().getStringExtra("key_update_username");
            String pass = getIntent().getStringExtra("key_update_password");
            Log.d("UPDATING DATA PARAMS", "Params = " + user + " - " + pass);
            login(Constants.BASE_URL_LOGIN, user, pass);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Intent intent = new Intent(UpdatingActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
        }
    }

    private void login(String url, String str1, String str2) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Success")) {
                            Intent intent = new Intent(UpdatingActivity.this, MainActivity.class);
                            intent.putExtra("key_data", str1);
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoom_out, R.anim.zoom_in);
                        } else {
                            Toast.makeText(UpdatingActivity.this, "Cập nhật dữ liệu thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DEBUG", error.toString());
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
}
