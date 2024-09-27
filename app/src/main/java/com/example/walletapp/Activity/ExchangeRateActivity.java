package com.example.walletapp.Activity;

import android.app.DownloadManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.walletapp.Adapter.ExchangeRateAdapter;
import com.example.walletapp.Constants;
import com.example.walletapp.Model.ExchangeModel;
import com.example.walletapp.R;
import com.example.walletapp.Utils.HeightUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class ExchangeRateActivity extends AppCompatActivity {
    private ListView exchange_list;
    private ImageView back_btn;
    private ArrayList<ExchangeModel> mainList;
    private ExchangeRateAdapter adapter;
    private ProgressBar progress_exchange;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_exchange_rate);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_theme_2));
        }
        exchange_list = findViewById(R.id.exchange_list);
        back_btn = findViewById(R.id.back_btn);
        progress_exchange = findViewById(R.id.progress_exchange);
        mainList = new ArrayList<>();
        progress_exchange.setVisibility(View.VISIBLE);
        exchangeData(Constants.BASE_URL_API_EXCHANGE);
        adapter = new ExchangeRateAdapter(this, mainList);
        exchange_list.setAdapter(adapter);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.close_in, R.anim.close_out);
            }
        });
    }

    private void exchangeData(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseXMLResponse(response);
                        } catch (XmlPullParserException | IOException e) {
                            Log.d("EXCHANGE RATE", "Error 1: " + e.getMessage());
                            LayoutInflater inflater = getLayoutInflater();
                            View layout = inflater.inflate(R.layout.custom_toast_15, null);
                            Toast toast = new Toast(ExchangeRateActivity.this);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                        progress_exchange.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("EXCHANGE RATE", "Error 2: " + error.getMessage());
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast_14, null);
                        Toast toast = new Toast(ExchangeRateActivity.this);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                }
            );
        queue.add(stringRequest);
    }

    private void parseXMLResponse(String xmlString) throws XmlPullParserException, IOException {
        if (xmlString.startsWith("\uFEFF")) {
            xmlString = xmlString.substring(1);
        }
        xmlString = xmlString.replaceAll("[^\\x20-\\x7E]", "");

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new StringReader(xmlString));
        int eventType = parser.getEventType();
        String currencyCode = null;
        String buyRate = null;
        String transferRate = null;
        String sellRate = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if ("Exrate".equals(tagName)) {
                        currencyCode = parser.getAttributeValue(null, "CurrencyCode");
                        buyRate = parser.getAttributeValue(null, "Buy");
                        transferRate = parser.getAttributeValue(null, "Transfer");
                        sellRate = parser.getAttributeValue(null, "Sell");
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("Exrate".equals(tagName)
                            && currencyCode != null && sellRate != null
                            && buyRate != null && transferRate != null) {
                        Log.d("EXCHANGE RATE", "No: " + currencyCode + " - " + buyRate
                                + " - " + transferRate + " - " + sellRate);
                        this.mainList.add(new ExchangeModel(currencyCode, buyRate, transferRate, sellRate));
                    }
                    break;
            }
            eventType = parser.next();
        }
        adapter.notifyDataSetChanged();
        HeightUtils.setListViewHeight(exchange_list);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.close_in, R.anim.close_out);
    }
}
